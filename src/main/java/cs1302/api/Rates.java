package cs1302.api;

import java.util.Map;

/**
 * Represents a result inside a response from the
 * currency API. This is used by This is
 * used by Gson to create an object from the JSON response body.
 */
public class Rates {
    public Map<String, Double> rates;
}
