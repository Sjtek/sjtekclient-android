package nl.sjtek.client.android.storage

import android.content.Context
import android.util.Log
import nl.sjtek.control.data.parsers.LampHolder
import nl.sjtek.control.data.parsers.QuotesHolder
import nl.sjtek.control.data.parsers.ResponseHolder
import nl.sjtek.control.data.parsers.UserHolder
import nl.sjtek.control.data.staticdata.Lamp
import nl.sjtek.control.data.staticdata.User
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import kotlin.system.measureTimeMillis

/**
 * Utility for caching the API responses.
 */
object StateManager {
    private const val FILE_RESPONSE = "d_response.dat"
    private const val FILE_USERS = "d_users.dat"
    private const val FILE_QUOTES = "d_quotes.dat"
    private const val FILE_LAMPS = "d_lamps.dat"

    var responseHolder: ResponseHolder? = null
        private set
    var users: List<User>? = null
        private set
    var quotes: List<String>? = null
        private set
    var lamps: Map<Int, Lamp>? = null

    init {
        EventBus.getDefault().register(this)
    }

    @Subscribe
    fun onResponseUpdated(responseHolder: ResponseHolder) {
        this.responseHolder = responseHolder
    }

    @Subscribe
    fun onUsersUpdate(holder: UserHolder) {
        this.users = holder.users
    }

    @Subscribe
    fun onQuotesUpdate(holder: QuotesHolder) {
        this.quotes = holder.quotes
    }

    @Subscribe
    fun onLampsUpdate(holder: LampHolder) {
        this.lamps = holder.lamps
    }

    @Deprecated("Just do a null check")
    val isReady: Boolean
        get() = responseHolder != null && users != null && quotes != null && lamps != null

    @Deprecated("Check user")
    fun areExtraLightsEnabled(context: Context): Boolean {
        val user = Preferences.getInstance(context).username
        return users?.any { it.username == user } == true
    }

    /**
     * Get the default playlist of the user.
     *
     * @param context Context to get the username
     * @return Playlist
     */
    fun getDefaultPlaylist(context: Context): String {
        val user = Preferences.getInstance(context).username
        val selectedUser = users?.find { it.username == user } ?: return ""
        return if (isReady) {
            selectedUser.playlists.values.first()
        } else {
            ""
        }
    }

    /**
     * Save the cache to disk.
     *
     * @param context Context
     */
    fun save(context: Context) {
        val time = measureTimeMillis {
            StorageHelper.save(context, FILE_RESPONSE, responseHolder)
            StorageHelper.save(context, FILE_LAMPS, lamps)
            StorageHelper.save(context, FILE_QUOTES, lamps)
            StorageHelper.save(context, FILE_USERS, users)
        }
        Log.d("StateManager", "Saved in ${time}ms")
//        WearSyncThread(context, responseHolder, dataCollection).start()
    }

    /**
     * Load the cache from the disk.
     *
     * @param context Context
     */
    fun load(context: Context) {
        val time = measureTimeMillis {
            responseHolder = StorageHelper.load(context, FILE_RESPONSE)
            lamps = StorageHelper.load(context, FILE_LAMPS)
            quotes = StorageHelper.load(context, FILE_QUOTES)
            users = StorageHelper.load(context, FILE_USERS)
        }
        Log.d("StateManager", "Loaded in ${time}ms")
    }
}
