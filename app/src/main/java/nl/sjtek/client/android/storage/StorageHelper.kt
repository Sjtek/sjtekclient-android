package nl.sjtek.client.android.storage

import android.content.Context
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object StorageHelper {

    fun <T> save(context: Context, fileName: String, data: T?) {
        if (data == null) return
        try {
            ObjectOutputStream(context.openFileOutput(fileName, Context.MODE_PRIVATE)).use { stream ->
                stream.writeObject(data)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun <T> load(context: Context, fileName: String): T? {
        try {
            ObjectInputStream(context.openFileInput(fileName)).use { stream ->
                return stream.readObject() as? T
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}