package nl.sjtek.client.android.interfaces;

import android.support.v4.app.Fragment;

/**
 * Created by Wouter Habets on 22-10-15.
 */
public interface CardCallbackListener {
    void sendRequest(String requestUrl);

    void changeFragment(Fragment fragment);
}
