//@@author laichengyu

package seedu.address.logic.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.JsonObject;

import seedu.address.commons.util.FetchUtil;
import seedu.address.commons.util.UrlBuilderUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.coin.exceptions.CoinNotFoundException;
import seedu.address.model.coin.exceptions.DuplicateCoinException;

/**
 * Updates all coins in the coin book with latest cryptocurrency data
 */
public class SyncCommand extends Command {

    public static final String COMMAND_WORD = "sync";
    public static final String COMMAND_ALIAS = "sy";

    public static final String MESSAGE_SUCCESS = "Synced all coins with latest cryptocurrency data";

    private String cryptoCompareApiUrl = "https://min-api.cryptocompare.com/data/pricemulti";

    /**
     * Creates and returns a {@code List<NameValuePair>} with two key-value pairs, coin symbols and currency.
     *
     * @param coinList cannot be null
     * @return parameters for CryptoCompare API call
     */
    private List<NameValuePair> getParams(Set<String> coinList) {
        List<NameValuePair> params = new ArrayList<>();
        String coinCodes = String.join(",", coinList);
        params.add(new BasicNameValuePair("fsyms", coinCodes));
        params.add(new BasicNameValuePair("tsyms", "USD"));
        return params;
    }

    /**
     * Adds parameters to the CryptoCompare API URL.
     *
     * @param params cannot be null
     */
    private void buildApiUrl(List<NameValuePair> params) {
        cryptoCompareApiUrl = UrlBuilderUtil.buildUrl(cryptoCompareApiUrl, params);
    }

    @Override
    public CommandResult execute() throws CommandException {
        try {
            List<NameValuePair> params = getParams(model.getCodeList());
            buildApiUrl(params);
            JsonObject newData = FetchUtil.fetchAsJson(cryptoCompareApiUrl);
            model.syncAll(newData);
            return new CommandResult(MESSAGE_SUCCESS);
        } catch (DuplicateCoinException dpe) {
            throw new CommandException("Unexpected code path!");
        } catch (CoinNotFoundException cnfe) {
            throw new AssertionError("The target coin cannot be missing");
        }
    }
}
