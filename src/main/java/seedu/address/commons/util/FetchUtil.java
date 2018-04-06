//@@author laichengyu

package seedu.address.commons.util;

import static org.asynchttpclient.Dsl.asyncHttpClient;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.asynchttpclient.AsyncHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.HttpResponseBodyPart;
import org.asynchttpclient.HttpResponseStatus;
import org.asynchttpclient.Response;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.netty.handler.codec.http.HttpHeaders;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.LoadingEvent;

/**
 * Retrieves data in JSON format from a specified URL
 */
public class FetchUtil {

    private static AsyncHttpClient myAsyncHttpClient = asyncHttpClient();

    /**
     * Asynchronously fetches data from the specified url and returns it as a JsonObject
     * @param url cannot be null
     * @return data received as a JsonObject
     * @throws InterruptedException when there is a thread interrupt
     * @throws ExecutionException when there is an error in data fetching
     */
    public static JsonObject asyncFetch(String url)
            throws InterruptedException, ExecutionException {
        //Send HTTP request asynchronously
        BoundRequestBuilder boundReqBuilder = myAsyncHttpClient.prepareGet(url);
        AsyncHandler<Response> asyncHandler = getResponseAsyncHandler();
        Future<Response> whenResponse = boundReqBuilder.execute(asyncHandler);

        //Set loading UI
        EventsCenter.getInstance().post(new LoadingEvent(true));
        Response response = whenResponse.get();
        //Return UI to normal
        EventsCenter.getInstance().post(new LoadingEvent(false));
        return parseStringToJsonObj(response.getResponseBody());
    }

    /**
     * Creates a new async response handler
     * @return AsyncHandler for Response objects
     */
    private static AsyncHandler<Response> getResponseAsyncHandler() {
        return new AsyncHandler<Response>() {
                private Response.ResponseBuilder builder = new Response.ResponseBuilder();
                private Integer status;
                @Override
                public State onStatusReceived(HttpResponseStatus responseStatus) throws Exception {
                    status = responseStatus.getStatusCode();
                    builder.accumulate(responseStatus);
                    if (status != 200) {
                        return State.ABORT;
                    }
                    return State.CONTINUE;
                }
                @Override
                public State onHeadersReceived(HttpHeaders headers) throws Exception {
                    return State.CONTINUE;
                }
                @Override
                public State onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {
                    builder.accumulate(bodyPart);
                    return State.CONTINUE;
                }
                @Override
                public Response onCompleted() throws Exception {
                    return builder.build();
                }
                @Override
                public void onThrowable(Throwable t) {
                }
            };
    }

    /**
     * Parses a String into a JsonObject
     * @param str cannot be null
     * @return JsonObject converted from String
     */
    private static JsonObject parseStringToJsonObj(String str) {
        JsonObject jsonObject;

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(str);
        jsonObject = jsonElement.getAsJsonObject();

        return jsonObject;
    }
}
