package cs1302.api;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.net.URLEncoder.encode;

import java.net.http.HttpClient;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Map;
import java.lang.reflect.Type;
import javafx.scene.text.Text;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.scene.layout.Pane;
import javafx.scene.control.ProgressIndicator;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Allows users to select a country on a map and shows
 * a visual representation of how other
 * world currencies compare.
 */
public class ApiApp extends Application {
    Stage stage;
    Scene scene;
    VBox root;

    /** Top Layer. */
    private HBox selectLayer;
    private List<String> countriesList;
    private static ComboBox<String> countryBox1;
    private static ComboBox<String> countryBox2;
    private Button loadButton;
    private Label toCurrency;


    /** Info Layer. */
    private HBox infoLayer;
    private Label exchange;
    private Pane spacer;
    private ProgressIndicator spinner;


    /** Map Layer. */
    private HBox mapLayer;
    private ImageView worldMap;
    private static Map<String, Double> lonCache = new HashMap<>();
    private static Map<String, Double> latCache = new HashMap<>();
    private static Map<String, String> locationCache = new HashMap<>();


    private String defaultFile = "file:resources/white-background.png";


    /** HTTP client. */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)           // uses HTTP protocol version 2 where possible
        .followRedirects(HttpClient.Redirect.NORMAL)  // always redirects, except from HTTPS to HTTP
        .build();                                     // builds and returns a HttpClient object

    /** Google {@code Gson} object for parsing JSON-formatted strings. */
    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()                          // enable nice output when printing
        .create();                                    // builds and returns a Gson object

    private static String GEOAPIFYKEY;

    private static String CURRENCYKEY;




    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        root = new VBox();
        selectLayer = new HBox(6);
        countriesList = new ArrayList<>();

        loadButton = new Button("Load");
        toCurrency = new Label(" to ");

        infoLayer = new HBox(6);
        exchange = new Label("Please select two currencies to compare.");
        spacer = new Pane();
        spinner = new ProgressIndicator();

        mapLayer = new HBox(6);
        ApiApp.setKeys();

    } // ApiApp

    /** {@inheritDoc} */
    public void init() {
        // exchange.setMinHeight(40);
        exchange.setStyle("-fx-font-size: 15px;");

        System.out.println("init() called");
        try {
            addCountries();
        } catch (IOException e) {
            System.err.println(e);
        } // try
        countryBox1 = new ComboBox<String>(FXCollections.observableArrayList(countriesList));
        countryBox2 = new ComboBox<String>(FXCollections.observableArrayList(countriesList));

        countryBox1.setMaxWidth(310);
        countryBox2.setMaxWidth(310);

        selectLayer.getChildren().addAll(countryBox1,toCurrency, countryBox2,loadButton);
        selectLayer.setAlignment(Pos.CENTER);
        selectLayer.setHgrow(countryBox1, Priority.ALWAYS);
        selectLayer.setHgrow(countryBox2, Priority.ALWAYS);
        selectLayer.setMaxWidth(1280);

        infoLayer.getChildren().addAll(exchange, spacer, spinner);
        spinner.setVisible(false);
        infoLayer.setHgrow(spacer, Priority.ALWAYS);
        spacer.setMaxWidth(600);
        infoLayer.setHgrow(spinner, Priority.ALWAYS);
        infoLayer.setMaxHeight(32);



        Image defaultImage = new Image(defaultFile, 800, 600, false, false);
        worldMap = new ImageView(defaultImage);

        mapLayer.getChildren().addAll(worldMap);
        mapLayer.setAlignment(Pos.CENTER);


        this.loadButton.setOnAction(event -> runNow(() -> this.loadButtonFunction()));

    } // init

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {


        this.stage = stage;

        // demonstrate how to load local asset using "file:resources/"
        // Image bannerImage = new Image("file:resources/readme-banner.png");
        // ImageView banner = new ImageView(bannerImage);
        // banner.setPreserveRatio(true);
        // banner.setFitWidth(640);

        // some labels to display information
        // Label notice = new Label("Modify the starter code to suit your needs.");

        // setup scene
        root.getChildren().addAll(selectLayer,infoLayer,mapLayer);
        scene = new Scene(root);


        // setup stage
        stage.setTitle("ApiApp!");
        stage.setScene(scene);
        stage.setMaxWidth(1280);
        stage.setMaxWidth(720);
        // System.out.println("scene height: " + root.getHeight());
        // System.out.println("scene width: " + root.getWidth());
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();

    } // start

    /** {@inheritDoc} */
    @Override
    public void stop() {
        // feel free to modify this method
        System.out.println("stop() called");
    } // stop

    /**
     * Sets API keys from config file.
     */
    private static void setKeys() {
        try (FileInputStream configFileStream
            = new FileInputStream("resources/config.properties")) {
            Properties config = new Properties();
            config.load(configFileStream);
            GEOAPIFYKEY = config.getProperty("geoapify.key");
            CURRENCYKEY = config.getProperty("currencybeacon.key");
        } catch (IOException ioe) {
            System.err.println(ioe);
            ioe.printStackTrace();
        }
    } // static

    /**
     * Creates and immediately starts a new daemon thread that executes
     * {@code target.run()}. This method, which may be called from any thread,
     * will return immediately to the caller.
     * @param target the object whose {@code run} method is invoked when this
     *               thread is started
     */
    private static void runNow(Runnable target) {
        Thread t = new Thread(target);
        t.setDaemon(true);
        t.start();
    } // runNow

    /**
     * Fetch a string from a URL usin an HTTP request and client.
     * @param url The URL to fetch from.
     * @return The body of the response.
     * @throws IOException If a problem is encountered while fetching.
     */
    private static String fetchString(String url) throws IOException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
            HttpResponse<String> response = HTTP_CLIENT
                .send(request, BodyHandlers.ofString());
            ApiApp.requireStatusCode(response, 200);
            String body = response.body().strip();
            return body;
        } catch (IOException | InterruptedException cause) {
            String message = "Unable to fetch URL: " + url;
            throw new IOException(message, cause);
        } // try
    } // fetchImage

    /**
     * Throw an {@link java.io.IOException} if the HTTP status code of the
     * {@link java.net.http.HttpResponse} supplied by {@code response} is
     * not the {@code expected} value (e.g., {@code 200}).
     * @param <T> The response body type.
     * @param response The response to check.
     * @param expected The expected status code.
     */
    private static <T> void requireStatusCode(HttpResponse<T> response, int expected)
        throws IOException {
        int actual = response.statusCode();
        if (actual != expected) {
            throw new IOException("Expected HTTP status code " + expected +
            " but received " + actual);
        } // if
    } // requireStatusCode


    // /**
    //  * Creates an ArrayList containing all the countries available
    //  * in the exchangerate API.
    //  * @return a List containing all the countries within the currency list.
    //  */
    // private static CountryResponse fetchCountry() throws IOException {
    //     try {
    //         String url =
    //             "https://raw.githubusercontent.com/fawazahmed0/exchange-api/main/country.json";
    //         String json = ApiApp.fetchString(url);
    //    CountryResponse countries = GSON.<CountryResponse>fromJson(json, CountryResponse.class);
    //         return countries;
    //     } catch (IOException e) {
    //         throw e;
    //     } // try


    // } // getCountryList


    /**
     * Adds countries to country array list.
     */
    private void addCountries() throws IOException {
        countriesList.clear();
        try {
            // CountryResponse countries = ApiApp.fetchCountry();

            // for (Country c : countries.countryResults) {
            //     String countryAndCurrency = c.countryName + " - " + c.currencyCode;
            //     countriesList.add(countryAndCurrency);
            // }
            String url =
                "https://raw.githubusercontent.com/fawazahmed0/exchange-api/main/country.json";
            Type countryMapType = new TypeToken<Map<String, Country>>(){}.getType();
            String json = ApiApp.fetchString(url);
            Map<String, Country> countryMap = GSON.fromJson(json, countryMapType);

            for (Country country : countryMap.values()) {
                String countryAndCurrency = country.countryName + " - "
                    + country.currencyCode.toUpperCase();
                countriesList.add(countryAndCurrency);
            } // for

        } catch (IOException e) {
            throw e;
        } // try


    } // addCountries

     /**
     * Show a modal error alert based on {@code cause}.
     * @param cause a {@link java.lang.Throwable Throwable} that caused the alert
     */
    public void alertError(Throwable cause) {
        TextArea text = new TextArea(cause.toString());
        text.setWrapText(true);
        text.setEditable(false);
        Alert alert = new Alert(AlertType.ERROR);
        alert.getDialogPane().setContent(text);
        alert.setResizable(true);
        alert.showAndWait();
    } // alertError

    // private static class ExchangeRatesResponse {
    //     public Rates rates;

    //     public static class Rates {
    //         // This is a map of currency codes to exchange rates
    //         public Map<String, Double> rates;
    //     }
    // }

    /**
     * Return the URL string for a query to the Currency-exchange
     * API.
     * @param baseCode The currency abbreviation for the base country.
     * @param compareCode The currency abbreviation for the compare country.
     * @return The final URL to be used to search.
     */
    private static String getCurrencyUrl(String baseCode, String compareCode) {
        String url = "https://api.currencybeacon.com/v1/convert?api_key=";
        url += CURRENCYKEY + "&from=" + baseCode + "&to=" + compareCode + "&amount=1";
        // System.out.println(url);
        return url;
    } // getCurrencyUrl

    /**
     * Gets the currency exchange rate from the first
     * selection box to the second selection box.
     * @return A string showing the exchange rate.
     */
    private static String compareCurrencies() throws Exception {
        //String exchangeInfo = new String();


        String baseCurrency = countryBox1.getValue();
        String compareCurrency = countryBox2.getValue();
        if (baseCurrency == null || compareCurrency == null) {
            throw new Exception("Two currencies must be selected to make a comparison.");
        } // if

        String baseCode = baseCurrency.substring(baseCurrency.length() - 3);

        String compareCode = compareCurrency.substring(compareCurrency.length() - 3);

        String currencyUrl = ApiApp.getCurrencyUrl(baseCode, compareCode);
        String currencyJson = ApiApp.fetchString(currencyUrl);

        // CurrencyResponse currencyResponse = new Gson().fromJson(currencyJson
        //     , CurrencyResponse.class);

        // double exchangeRate = currencyResponse.rates.get(compareCode);

        CurrencyResponse currencyResponse = GSON.fromJson(currencyJson, CurrencyResponse.class);

        double exchangeRate = currencyResponse.response.value;


        String exchangeInfo = "1 " + baseCode + " = " + exchangeRate + " " + compareCode
            + ", Rate Updated daily.";


        return exchangeInfo;

    } // compareCurrencies

     /**
     * Return the URL string for a query to the GeoApify geocoding
     * API.
     * @param countryName The name of the country
     * @return The final URL to be used to search.
     */
    private static String getCountryLocJson(String countryName) throws IOException {

        if (locationCache.containsKey(countryName)) {
            return locationCache.get(countryName);
        } // if

        String url = "https://api.geoapify.com/v1/geocode/search";
        url += String.format("?text=%s",  URLEncoder.encode(countryName.strip().toLowerCase()
            , UTF_8));
        url += "&format=json&apiKey=" + GEOAPIFYKEY;

        String json = "";

        try {
            json = ApiApp.fetchString(url + "&type=country");
            GeoapifyResponse response = GSON.fromJson(json, GeoapifyResponse.class);

            if (response.results.size()  == 0) {
                // If no country found, try searching as a state
                json = ApiApp.fetchString(url + "&type=state");
            }
            locationCache.put(countryName, json);
        } catch (IOException e) {
            throw e;
        } // try


        return json;
    } // getCountryLocUrl

    /**
     * Return the longitude of a country in the provided Json.
     * @param countryName The name of the country
     * @return The longitude of the country provided.
     */
    private static double fetchLon(String countryName) throws Exception {
        // String country1 = countryBox1.getValue();
        // String country2 = countryBox2.getValue();
        // if (country1 == null || country2 == null) {
        //     throw new Exception("Two currencies must be selected to make a comparison.");
        // } // ifString country1 = country

        if (lonCache.containsKey(countryName)) {
            return lonCache.get(countryName);
        } // if
        String countryJson = ApiApp.getCountryLocJson(countryName);

        if (countryJson.isEmpty()) {
            throw new Exception("Unable to fetch longitude for " + countryName);
        } // if

        GeoapifyResponse response = new Gson().fromJson(countryJson, GeoapifyResponse.class);


        if (response.results.size() > 0) {
            double countryLon = response.results.get(0).lon;
            lonCache.put(countryName, countryLon);
            return countryLon;

        } // if



        throw new Exception("No results found for " + countryName);



    } // fetchLon


    /**
     * Return the latitude of a country in the provided Json.
     * @param countryName The name of the country
     * @return The latitude of the country.
     */
    private static double fetchLat(String countryName) throws Exception {
        if (latCache.containsKey(countryName)) {
            return latCache.get(countryName);
        } // if

        String countryJson = ApiApp.getCountryLocJson(countryName);

        if (countryJson.isEmpty()) {
            throw new Exception("Unable to fetch longitude for " + countryName);
        } // if

        GeoapifyResponse response = new Gson().fromJson(countryJson, GeoapifyResponse.class);

        if (response.results.size() > 0) {
            double countryLat = response.results.get(0).lat;
            latCache.put(countryName, countryLat);
            return countryLat;
        } // if

        throw new Exception("No results found for " + countryName);

    } // fetchLat


    /**
     * Creates map url and fetches the static map from the url.
     * @return The static map image.
     */
    private static Image fetchMap() throws Exception {
        try {
            String country1String = countryBox1.getValue();
            String country2String = countryBox2.getValue();

            String country1Name = country1String.substring(0, country1String.length() - 6);
            String country2Name = country2String.substring(0, country2String.length() - 6);

            Double lon1 = ApiApp.fetchLon(country1Name);
            Double lat1 = ApiApp.fetchLat(country1Name);

            Double lon2 = ApiApp.fetchLon(country2Name);
            Double lat2 = ApiApp.fetchLat(country2Name);

            String markerDetails = ";color:%23ff0000;size:medium";

            String url = "https://maps.geoapify.com/v1/staticmap?style=osm-carto&width=800"
                + "&height=600&center=lonlat:4.5,0&zoom=0.809";

            url += "&marker=lonlat:" + lon1 + "," + lat1 + markerDetails + "|lonlat:" + lon2 + "," +
                lat2 + markerDetails + "&apiKey=" + GEOAPIFYKEY;

            Image mapImage = new Image(url);

            return mapImage;
        } catch (Exception e) {
            throw e;
        } // try



    } // fetchMap





    /**
     * Allows users to use the Load button to load
     * the currency conversion and corresponding graphic.
     */
    public void loadButtonFunction() {


        try {

            this.loadButton.setDisable(true);
            Platform.runLater(() -> spinner.setVisible(true));

            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(event -> {


                loadButton.setDisable(false);
                Platform.runLater(() -> {

                    try {
                        this.exchange.setText(ApiApp.compareCurrencies());
                        Image markerMap = ApiApp.fetchMap();
                        worldMap.setImage(markerMap);
                        spinner.setVisible(false);
                    } catch (Exception e) {
                        alertError(e);
                    } //try
                });

            });

            pause.play();





        } catch (Exception e) {
            // change this to alert later

            Platform.runLater(() -> {
                alertError(e);
                spinner.setVisible(false);
            });
        }



    } // loadButtonFunction


} // ApiApp
