package seedu.address.logic.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.JsonObject;

import seedu.address.commons.util.FetchUtil;
import seedu.address.commons.util.UrlBuilderUtil;

/**
 * Updates all coins in the coin book with latest cryptocurrency data
 */
public class SyncCommand extends Command {

    public static final String COMMAND_WORD = "sync";
    public static final String COMMAND_ALIAS = "sy";

    public static final String MESSAGE_SUCCESS = "Synced all coins with latest cryptocurrency data";

    private static String CRYPTOCOMPARE_API_URL = "https://min-api.cryptocompare.com/data/pricemulti";

    private List<NameValuePair> getParams(Set<String> coinList) {
        List<NameValuePair> params = new ArrayList<>();
        String coinCodes = String.join(",", coinList);
        params.add(new BasicNameValuePair("fsyms", coinCodes));
        params.add(new BasicNameValuePair("tsyms", "USD"));
        return params;
    }

    private void buildApiUrl(List<NameValuePair> params) {
        CRYPTOCOMPARE_API_URL = UrlBuilderUtil.buildUrl(CRYPTOCOMPARE_API_URL, params);
    }

    @Override
    public CommandResult execute() {
        List<NameValuePair> params = getParams(model.getCodeList());
        buildApiUrl(params);
        JsonObject newData = FetchUtil.fetchAsJson(CRYPTOCOMPARE_API_URL);
        model.syncAll(newData);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
