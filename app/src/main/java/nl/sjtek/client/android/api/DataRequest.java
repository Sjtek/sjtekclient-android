package nl.sjtek.client.android.api;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

import nl.sjtek.control.data.settings.DataCollection;

/**
 * Created by wouter on 15-11-16.
 */

class DataRequest extends Request<DataCollection> {

    private final Response.Listener<DataCollection> listener;

    DataRequest(Response.Listener<DataCollection> listener, Response.ErrorListener errorListener) {
        super(Method.GET, Action.DATA.toString(), errorListener);
        this.listener = listener;
    }

    @Override
    protected Response<DataCollection> parseNetworkResponse(NetworkResponse response) {
        try {
            String data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            DataCollection dataCollection = DataCollection.fromJson(data);
            return Response.success(dataCollection, null);
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(DataCollection response) {
        listener.onResponse(response);
    }
}
