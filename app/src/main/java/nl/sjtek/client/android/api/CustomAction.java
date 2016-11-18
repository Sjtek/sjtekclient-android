package nl.sjtek.client.android.api;

/**
 * Created by wouter on 14-11-16.
 */

public class CustomAction implements ActionInterface {

    private final String url;

    public CustomAction(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return url;
    }
}
