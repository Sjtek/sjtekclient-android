package nl.sjtek.client.android.update;

import org.json.JSONException;
import org.json.JSONObject;

public class Update {

    private final Music music;
    private final Lights lights;
    private final Temperature temperature;
    private final TV tv;
    private final Minecraft minecraft;

    public Update(JSONObject jsonObject) throws JSONException {
        this.music = new Music(jsonObject.getJSONObject("music"));
        this.lights = new Lights(jsonObject.getJSONObject("lights"));
        this.temperature = new Temperature(jsonObject.getJSONObject("temperature"));
        this.tv = new TV(jsonObject.getJSONObject("tv"));
        this.minecraft = new Minecraft(jsonObject.getJSONObject("minecraft"));
    }

    public Music getMusic() {
        return music;
    }

    public Lights getLights() {
        return lights;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public TV getTv() {
        return tv;
    }

    public Minecraft getMinecraft() {
        return minecraft;
    }
}
