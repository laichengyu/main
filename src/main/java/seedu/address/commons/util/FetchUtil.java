//@@author laichengyu

package seedu.address.commons.util;

import static org.asynchttpclient.Dsl.asyncHttpClient;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.concurrent.Future;

import org.asynchttpclient.AsyncHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.HttpResponseBodyPart;
import org.asynchttpclient.HttpResponseStatus;
import org.asynchttpclient.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.netty.handler.codec.http.HttpHeaders;

/**
 * Retrieves data in JSON format from a specified URL
 */
public class FetchUtil {

    private static AsyncHttpClient myAsyncHttpClient = asyncHttpClient();

    /**
     * Returns a Future object, future from the specific url asynchronously.
     * The HTTP request Response can be retrieved using future.get().
     * All operations queued before future.get() are performed async and the application
     * will be thread-blocked at future.get() to wait for the return Response.
     * @param url cannot be null
     * @return a Future object that can retrieve a Response which contains HTTP request data
     * in its responseBody
     */
    public static Future<Response> asyncFetch(String url) {
        //Send HTTP request asynchronously
        BoundRequestBuilder boundReqBuilder = myAsyncHttpClient.prepareGet(url);
        AsyncHandler<Response> asyncHandler = getResponseAsyncHandler();
        return boundReqBuilder.execute(asyncHandler);
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
    public static JsonObject parseStringToJsonObj(String str) {
        JsonObject jsonObject;

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(str);
        jsonObject = jsonElement.getAsJsonObject();

        return jsonObject;
    }

    /**
     * Parses a file at {@code filepath} as an array of JsonObjects
     * @param fw cannot be null
     * @return JsonArray that is contained in the file at {@code filepath}
     */
    public static JsonArray parseFileToJsonObj(InputStreamReader fw) throws FileNotFoundException {
        JsonObject jsonObject;

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(fw);
        return jsonElement.getAsJsonArray();
    }
}
