package org.graylog.task.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

@Component
public class HttpRequest {
    public String requestUrl;

    public String requestMethod;

    public static final String GELF_URL = "http://127.0.0.1:12201/gelf";

    public final String REQUEST_METHOD = "POST";

    private final Logger logger = LogManager.getLogger(HttpRequest.class.getName());

    public HttpRequest() {
        requestUrl = GELF_URL;
        requestMethod = REQUEST_METHOD;
    }

    public HttpRequest(String url) {
        requestUrl = url;
        requestMethod = REQUEST_METHOD;
    }

    public HttpRequest(String url, String method) {
        requestUrl = url;
        requestMethod = method;
    }

    public HttpURLConnection openConnection() {
        HttpURLConnection con = null;
        try {
            logger.info(String.format("Opening connection %s request to URL: %s.",
                    requestMethod, requestUrl));
            URL url = new URL(requestUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("ContentType", "application/json");
            con.setRequestProperty("Accept", "application/json");
        } catch (MalformedURLException mue) {
            logger.error("Malformed URL exception: " + requestUrl);
            mue.printStackTrace();
            return null;
        } catch (IOException ioe) {
            logger.error("Error opening connection to URL: " + requestUrl);
            ioe.printStackTrace();
            return null;
        }

        try {
            con.setRequestMethod(requestMethod);
            con.setDoOutput(true);
        } catch (ProtocolException pe) {
            logger.error(String.format("Error setting %s request method for URL: %s",
                    requestMethod, con.getURL()));
            pe.printStackTrace();
            return null;
        }

        return con;
    }

    public boolean sendGetRequest() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public boolean sendPostRequest(HttpURLConnection con, String data) {
        boolean isSuccessful = false;
        try {
            logger.info(String.format("Sending POST request to URL: %s with post body: \n\t%s", con.getURL(), data));
            con.getOutputStream().write(data.getBytes("UTF-8"));
        } catch (IOException ioe) {
            logger.error("IO Exception running POST request", ioe.toString());
            ioe.printStackTrace();
            return false;
        }

        try {
            int responseCode = con.getResponseCode();
            switch (responseCode) {
                case HttpURLConnection.HTTP_OK: // 200
                case HttpURLConnection.HTTP_CREATED: // 201
                case HttpURLConnection.HTTP_ACCEPTED: // 202
                case HttpURLConnection.HTTP_NO_CONTENT: // 204
                case HttpURLConnection.HTTP_RESET: // 205
                case HttpURLConnection.HTTP_PARTIAL: // 206
                    isSuccessful = true;
                    logger.info(String.format("%d: %s - Successfully ran POST request to URL: %s.",
                            responseCode, con.getResponseMessage(), con.getURL()));
                    break;
                case HttpURLConnection.HTTP_BAD_REQUEST: // 400
                    logger.error(String.format("%d: %s - Bad HTTP request to URL: %s.",
                            responseCode, con.getResponseMessage(), con.getURL()));
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND: // 404
                    logger.error(String.format("%d: %s - Request not found for URL: %s",
                            responseCode, con.getResponseMessage(), con.getURL()));
                    break;
                case HttpURLConnection.HTTP_ENTITY_TOO_LARGE: // 413
                    logger.error(String.format("%d: %s - Request POST body too large.",
                            responseCode, con.getResponseMessage()));
                    break;
                case HttpURLConnection.HTTP_INTERNAL_ERROR: // 500
                    logger.error(String.format("%d: %s - Internal error, please try again.",
                            responseCode, con.getResponseMessage()));
                    break;
                case HttpURLConnection.HTTP_BAD_GATEWAY: // 502
                    logger.error(String.format("%d: %s - Bad gateway, please try again later.",
                            responseCode, con.getResponseMessage()));
                    break;
                default: // everything else
                    logger.error(String.format("%d: %s - Error running HTTP request to URL: %s.",
                            responseCode, con.getResponseMessage(), con.getURL()));

            }
        } catch (IOException ioe) {
            logger.error("IO Exception running POST request to URL: " + con.getURL(), ioe.toString());
            ioe.printStackTrace();
            return false;
        }

        return isSuccessful;
    }
}
