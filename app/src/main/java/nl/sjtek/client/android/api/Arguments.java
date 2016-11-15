package nl.sjtek.client.android.api;

import android.content.Context;
import android.text.TextUtils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import nl.sjtek.client.android.storage.Preferences;


public class Arguments implements Serializable {

    private static final String ENCODING = "UTF-8";

    private boolean useVoice;
    private String user;
    private String url;

    public Arguments() {

    }

    public static Arguments empty() {
        return new Arguments();
    }

    public boolean isUseVoice() {
        return useVoice;
    }

    public Arguments setUseVoice(boolean useVoice) {
        this.useVoice = useVoice;
        return this;
    }

    public String getUser() {
        try {
            return URLDecoder.decode(user, ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Arguments setUser(String user) {
        try {
            this.user = URLEncoder.encode(user, ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Arguments setDefaultUser(Context context) {
        String user = Preferences.getInstance(context).getUsername();
        if (TextUtils.isEmpty(user)) return this;
        setUser(user.toLowerCase());
        return this;
    }

    public String getUrl() {
        try {
            return URLDecoder.decode(url, ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Arguments setUrl(String url) {
        try {
            this.url = URLEncoder.encode(url, ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Arguments setDefaultPlaylist(Context context) {
        setUrl(Preferences.getInstance(context).getDefaultPlaylist());
        return this;
    }

    public String build() {
        List<String> argumentsList = new ArrayList<>();
        if (useVoice) argumentsList.add("voice");
        if (!TextUtils.isEmpty(url)) argumentsList.add("url=" + url);
        argumentsList.add("user=" + (TextUtils.isEmpty(user) ? "default" : user));
        return "?" + TextUtils.join("&", argumentsList);
    }

    @Override
    public String toString() {
        return build();
    }
}
