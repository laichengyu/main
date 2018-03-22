package seedu.address.commons.util;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

import seedu.address.commons.core.LogsCenter;

/**
 * Builds a URL given a url and parameters
 */
public class UrlBuilderUtil {

    private static final Logger logger = LogsCenter.getLogger(UrlBuilderUtil.class);

    /**
     * Builds a URL given the url and parameters
     * @param url cannot be null
     * @param parameters are optional
     * @return String URL concatenated with parameters
     */
    public static String buildUrl(String url, Optional<List<NameValuePair>> parameters) {
        try {
            URIBuilder uri = new URIBuilder(url);
            parameters.ifPresent(uri::addParameters);
            return uri.build().toURL().toString();
        } catch (URISyntaxException e) {
            logger.info("Illegal characters found in url: " + url + " or parameters: " + parameters.toString());
        } catch (MalformedURLException e) {
            logger.info("Malformed URL: " + url + " provided");
        }
    }
}
