package nl.sjtek.client.android.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Wouter Habets on 25-3-16.
 */
public class WatsonResponse {
    @SerializedName("parameterUrl")
    private final String url;
    @SerializedName("response")
    private final String text;

    public WatsonResponse(String url, String text) {
        this.url = url;
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public String getText() {
        return text;
    }
}
