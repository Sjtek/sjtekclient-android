package nl.sjtek.client.android.update;

import org.json.JSONException;
import org.json.JSONObject;

public class Lights {

    private final boolean unit1;
    private final boolean unit2;
    private final boolean unit3;

    public Lights(JSONObject jsonObject) throws JSONException {
        this.unit1 = jsonObject.getBoolean("1");
        this.unit2 = jsonObject.getBoolean("2");
        this.unit3 = jsonObject.getBoolean("3");
    }

    public boolean isUnit1() {
        return unit1;
    }

    public boolean isUnit2() {
        return unit2;
    }

    public boolean isUnit3() {
        return unit3;
    }
}
