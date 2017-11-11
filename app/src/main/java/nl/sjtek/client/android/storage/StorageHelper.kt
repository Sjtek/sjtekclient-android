package nl.sjtek.client.android.storage

import android.content.Context
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object StorageHelper {

    fun save(context: Context, fileName: String, data: Any?) {
        if (data == null) return
        try {
            ObjectOutputStream(context.openFileOutput(fileName, Context.MODE_PRIVATE)).use { stream ->
                stream.writeObject(data)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun load(context: Context, fileName: String): Any? {
        try {
            ObjectInputStream(context.openFileInput(fileName)).use { stream ->
                return stream.readObject()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}