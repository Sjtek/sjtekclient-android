package nl.sjtek.client.android.api;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by wouter on 15-11-16.
 */

class MealRequest extends Request<String> {

    private static final String URL = "https://sjtekfood.habets.io/api/dinners/next";
    private final Response.Listener<String> listener;

    MealRequest(Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, URL, errorListener);
        this.listener = listener;
    }


    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            JSONObject jsonObject = new JSONObject(data);
            return Response.success(jsonObject.getString("name"), null);
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(String response) {
        listener.onResponse(response);
    }
}
