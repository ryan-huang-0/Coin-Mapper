package cs1302.api;

import com.google.gson.annotations.SerializedName;


/**
 * Respresents a result inside of a response from
 * the currency API. This is used by This is
 * used by Gson to create an object from the JSON response body.
 */
public class Country {
    @SerializedName("country_name")
    String countryName;

    @SerializedName("currency_code")
    String currencyCode;



} // Country
