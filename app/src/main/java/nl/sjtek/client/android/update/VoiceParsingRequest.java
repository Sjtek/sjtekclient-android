package nl.sjtek.client.android.update;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Wouter Habets on 7-11-15.
 */
public class VoiceParsingRequest extends Request<String> {

    private static final String BASE_URL = "http://www.3ddev.nl//emma/core/filter.php?text=%s";

    private Response.Listener<String> listener;

    public VoiceParsingRequest(String text, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        super(Method.GET, buildUrl(text), errorListener);
        listener = responseListener;
    }

    private static String buildUrl(String text) {
        JSONObject jsonText = new JSONObject();
        try {
            jsonText.put("text", text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            return String.format(BASE_URL, URLEncoder.encode(jsonText.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return String.format(BASE_URL, "");
        }
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            JSONObject jsonResponse = new JSONObject(data);
            String url = jsonResponse.getString("parameterURL");
            return Response.success(url, null);
        } catch (UnsupportedEncodingException | JSONException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(String response) {
        listener.onResponse(response);
    }
}
