package nl.sjtek.client.android.api;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Wouter Habets on 25-3-16.
 */
public class WatsonRequest extends Request<WatsonResponse> {

    private static final String URL = "https://3ddev.nl/watson/core/filter.php?text=%s";
    private Response.Listener<WatsonResponse> responseListener;

    public WatsonRequest(String text,
                         Response.Listener<WatsonResponse> responseListener,
                         Response.ErrorListener errorListener)
            throws JSONException, UnsupportedEncodingException {

        super(Method.POST, buildUrl(text), errorListener);

        this.responseListener = responseListener;
        setShouldCache(false);
    }

    private static String buildUrl(String text) throws JSONException, UnsupportedEncodingException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text", text);
        String data = URLEncoder.encode(jsonObject.toString(), "UTF-8");
        return String.format(URL, data);
    }

    @Override
    protected Response<WatsonResponse> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }

        WatsonResponse watsonResponse = new Gson().fromJson(parsed, WatsonResponse.class);
        if (watsonResponse == null || watsonResponse.getUrl() == null || watsonResponse.getUrl().isEmpty()) {
            return Response.error(new ParseError());
        } else {
            return Response.success(watsonResponse, null);
        }
    }

    @Override
    protected void deliverResponse(WatsonResponse response) {
        responseListener.onResponse(response);
    }
}
