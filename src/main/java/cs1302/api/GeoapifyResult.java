package cs1302.api;

/**
 * Represents a result inside a response from the
 * Geoapify api. This is used by Gson to create
 * an object from a result inside the JSON response
 * body.
 */
public class GeoapifyResult {
    public double lon;
    public double lat;
} // GeoapifyResult
