//@@author laichengyu

package seedu.address.commons.util;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
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
     * Builds a URL given the url and params
     * @param url cannot be null
     * @param params are necessary
     * @return String URL concatenated with params
     */
    public static String buildUrl(String url, List<NameValuePair> params) {
        String parameterizedUrl = "";
        try {
            URIBuilder uri = new URIBuilder(url);
            uri.addParameters(params);
            parameterizedUrl = uri.build().toURL().toString();
        } catch (URISyntaxException e) {
            logger.info("Illegal characters found in url: " + url + " or params: " + params.toString());
        } catch (MalformedURLException e) {
            logger.info("Malformed URL: " + url + " provided");
        }
        return parameterizedUrl;
    }
}
