//@@author laichengyu

package seedu.address.commons.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class UrlBuilderUtilTest {
    private static final String TEST_BASE_URL = "https://min-api.cryptocompare.com/data/pricemulti";
    private static final List<NameValuePair> params = new ArrayList<>(Arrays.asList(
            new BasicNameValuePair("fsyms", "BTC,ETH"),
            new BasicNameValuePair("tsyms", "USD,EUR")));

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void buildUrl_noExceptionThrown() {

        // valid case
        assertEquals(TEST_BASE_URL + "?fsyms=BTC%2CETH&tsyms=USD%2CEUR",
                UrlBuilderUtil.buildUrl(TEST_BASE_URL, params));
    }

    @Test
    public void buildUrl_nullUrl_throwsNullPointerException() {

        // null url parameter -> throws NullPointerException
        thrown.expect(NullPointerException.class);
        UrlBuilderUtil.buildUrl(null, params);
    }

    @Test
    public void buildUrl_nullParams_throwsNullPointerException() {

        // null params parameter -> throws NullPointerException
        thrown.expect(NullPointerException.class);
        UrlBuilderUtil.buildUrl(TEST_BASE_URL, null);
    }

    @Test
    public void buildUrl_nullBoth_throwsNullPointerException() {

        // null both parameters -> throws NullPointerException
        thrown.expect(NullPointerException.class);
        UrlBuilderUtil.buildUrl(null, null);
    }
}
