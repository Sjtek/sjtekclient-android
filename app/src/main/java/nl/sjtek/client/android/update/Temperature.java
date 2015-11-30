package nl.sjtek.client.android.update;

import org.json.JSONException;
import org.json.JSONObject;

public class Temperature {

    private final int inside;
    private final int outside;

    public Temperature(JSONObject jsonObject) throws JSONException {
        this.inside = jsonObject.getInt("inside");
        this.outside = jsonObject.getInt("outside");
    }

    public int getInside() {
        return inside;
    }

    public int getOutside() {
        return outside;
    }
}
