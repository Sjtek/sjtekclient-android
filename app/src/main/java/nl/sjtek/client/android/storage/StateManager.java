package nl.sjtek.client.android.storage;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import nl.sjtek.control.data.responses.ResponseCollection;
import nl.sjtek.control.data.settings.DataCollection;

/**
 * Created by wouter on 15-11-16.
 */

public class StateManager {

    private static final String RESPONSE_FILE = "response.dat";
    private static final String DATA_FILE = "data.dat";
    private static StateManager instance;
    private ResponseCollection responseCollection;
    private DataCollection dataCollection;

    private StateManager(Context context) {
        EventBus.getDefault().register(this);
        load(context);
    }

    public synchronized static StateManager getInstance(Context context) {
        if (instance == null) instance = new StateManager(context);
        return instance;
    }

    public synchronized static void stopInstance(Context context) {
        getInstance(context);
        instance.save(context);
        EventBus.getDefault().unregister(instance);
        instance = null;
    }

    @Subscribe
    public void onResponseUpdated(ResponseCollection responseCollection) {
        this.responseCollection = responseCollection;
    }

    @Subscribe
    public void onDataUpdated(DataCollection dataCollection) {
        this.dataCollection = dataCollection;
    }

    public boolean isReady() {
        return (responseCollection != null) && (dataCollection != null);
    }

    public ResponseCollection getResponseCollection() {
        return responseCollection;
    }

    public DataCollection getDataCollection() {
        return dataCollection;
    }

    public void save(Context context) {
        if (responseCollection != null) {
            try (ObjectOutputStream stream = new ObjectOutputStream(context.openFileOutput(RESPONSE_FILE, Context.MODE_PRIVATE))) {
                stream.writeObject(responseCollection);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (dataCollection != null) {
            try (ObjectOutputStream stream = new ObjectOutputStream(context.openFileOutput(DATA_FILE, Context.MODE_PRIVATE))) {
                stream.writeObject(dataCollection);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void load(Context context) {
        File responseFile = new File(context.getFilesDir(), RESPONSE_FILE);
        if (responseFile.exists()) {
            try (ObjectInputStream stream = new ObjectInputStream(context.openFileInput(RESPONSE_FILE))) {
                this.responseCollection = (ResponseCollection) stream.readObject();
            } catch (ClassNotFoundException | IOException e) {
                responseCollection = null;
            }
        }

        File dataFile = new File(context.getFilesDir(), DATA_FILE);
        if (dataFile.exists()) {
            try (ObjectInputStream stream = new ObjectInputStream(context.openFileInput(DATA_FILE))) {
                this.dataCollection = (DataCollection) stream.readObject();
            } catch (ClassNotFoundException | IOException e) {
                dataCollection = null;
            }
        }
    }
}
