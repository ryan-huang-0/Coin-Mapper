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



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Allows users to select a country on a map and shows
 * a visual representation of how other
 * world currencies compare.
 */
public class ApiApp extends Application {
    Stage stage;
    Scene scene;
    VBox root;

    private ComboBox<String> countries;

    /** HTTP client. */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)           // uses HTTP protocol version 2 where possible
        .followRedirects(HttpClient.Redirect.NORMAL)  // always redirects, except from HTTPS to HTTP
        .build();                                     // builds and returns a HttpClient object

    /** Google {@code Gson} object for parsing JSON-formatted strings. */
    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()                          // enable nice output when printing
        .create();                                    // builds and returns a Gson object


    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        root = new VBox();
        countries = new ComboBox<String>();
    } // ApiApp

    /** {@inheritDoc} */
    public void init() {

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
        root.getChildren().addAll(countries);
        scene = new Scene(root);

        // setup stage
        stage.setTitle("ApiApp!");
        stage.setScene(scene);
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


} // ApiApp
