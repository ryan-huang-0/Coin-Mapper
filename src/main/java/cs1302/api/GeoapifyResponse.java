package cs1302.api;

import java.util.List;

/**
 * Represents a response from the Geoapify api. This is
 * used by Gson to create an object from a result inside the
 * JSON reponse body.
 */
public class GeoapifyResponse {
    public List<GeoapifyResult> results;

} // GeoapifyReponse
