package nl.sjtek.client.android.storage;

import android.content.Context;
import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import nl.sjtek.control.data.responses.ResponseCollection;
import nl.sjtek.control.data.settings.DataCollection;

/**
 * Utility for caching the API responses.
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

    /**
     * Save the cache to the disk and clear the static instance.
     *
     * @param context
     */
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

    /**
     * Check if the caches are loaded from the disk or received from the API.
     *
     * @return Is ready
     */
    public boolean isReady() {
        return (responseCollection != null) && (dataCollection != null);
    }

    /**
     * Get the cached API state. Is null if the state manager is not ready yet.
     *
     * @return API state
     */
    public ResponseCollection getResponseCollection() {
        return responseCollection;
    }

    /**
     * Get the cached API data/preferences. Is null if the state manager is not ready yet.
     *
     * @return API data/preferences
     */
    public DataCollection getDataCollection() {
        return dataCollection;
    }

    /**
     * Check if the user is allowed to control the extra lights.
     *
     * @param context Context to get the username
     * @return Light permission
     */
    public boolean areExtraLightsEnabled(Context context) {
        String user = Preferences.getInstance(context).getUsername();
        return !TextUtils.isEmpty(user) && isReady() && dataCollection.getUsers().get(user).isCheckExtraLight();
    }

    /**
     * Get the default playlist of the user.
     *
     * @param context Context to get the username
     * @return Playlist
     */
    public String getDefaultPlaylist(Context context) {
        String user = Preferences.getInstance(context).getUsername();
        if (isReady()) {
            if (TextUtils.isEmpty(user)) {
                user = "default";
            }
            return dataCollection.getUsers().get(user).getDefaultPlaylist();
        } else {
            return "";
        }
    }

    /**
     * Save the cache to disk.
     *
     * @param context Context
     */
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
        new WearSyncThread(context, responseCollection, dataCollection).start();
    }

    /**
     * Load the cache from the disk.
     *
     * @param context Context
     */
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
