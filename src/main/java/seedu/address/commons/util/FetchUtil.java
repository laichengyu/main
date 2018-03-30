//@@author laichengyu

package seedu.address.commons.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import seedu.address.commons.core.LogsCenter;

/**
 * Retrieves data in JSON format from a specified URL
 */
public class FetchUtil {

    private static final Logger logger = LogsCenter.getLogger(FetchUtil.class);

    /**
     * Retrieves data from the specified url
     * @param url cannot be null
     * @return File converted from the JSON obtained from the url
     */
    public static File fetch(String url) {
        URL urlObject;
        HttpURLConnection urlConnection = null;
        File serverResponse;

        try {
            urlObject = new URL(url);
            urlConnection = (HttpURLConnection) urlObject.openConnection();

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                serverResponse = readStream(urlConnection.getInputStream());
                logger.info("Fetch request from " + url + " is successful");
                return serverResponse;
            }
        } catch (MalformedURLException e) {
            logger.info("URL: " + url + " provided is malformed");
        } catch (IOException e) {
            logger.info("Invalid URL: " + url + " provided");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }

    /**
     * Retrieves data from the specified url and converts it into a JsonObject
     * @param url cannot be null
     * @return JsonObject converted from the File obtained from the url
     */
    public static JsonObject fetchAsJson(String url) {
        File serverResponse = fetch(url);
        JsonObject jsonObject = new JsonObject();

        try {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader(serverResponse));
            jsonObject = jsonElement.getAsJsonObject();
        } catch (FileNotFoundException e) {
            logger.info("File retrieved from URL not found");
        }

        return jsonObject;
    }

    /**
     * Reads the inputStream and outputs to a temporary file location
     * @param inputStream cannot be null
     * @return File read from inputStream
     */
    private static File readStream(InputStream inputStream) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        File serverData = new File("temp.json");
        //Do we want to keep the data of API calls? If yes, overwrite or make new copies?

        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            FileUtil.writeToFile(serverData, response.toString());
            return serverData;
        } catch (IOException e) {
            logger.info("There was an error reading the server data: " + e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.info("There was an error while closing connection with remote server: " + e);
                }
            }
        }

        return null;
    }

}
