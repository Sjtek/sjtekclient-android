package nl.sjtek.client.android.update;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Wouter Habets on 27-10-15.
 */
public class Minecraft {

    private final boolean isReactorActive;
    private final float energyStored;
    private final float energyProducing;

    public Minecraft(JSONObject jsonObject) throws JSONException {
        this.isReactorActive = jsonObject.getBoolean("reactorRunning");
        this.energyStored = jsonObject.getLong("energyStored");
        this.energyProducing = jsonObject.getLong("energyProducing");
    }

    public boolean isReactorActive() {
        return isReactorActive;
    }

    public int getEnergyStored() {
        return (int) energyStored;
    }

    public int getEnergyProducing() {
        return (int) energyProducing;
    }
}
