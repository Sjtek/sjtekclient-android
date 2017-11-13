package io.habets.sjtek

import android.content.Context
import nl.sjtek.control.data.actions.Actions
import nl.sjtek.control.data.parsers.LampParser
import nl.sjtek.control.data.staticdata.Lamp
import timber.log.Timber
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

object Net {

    private const val BASE_URL = "https://sjtek.nl/api/"
    private const val TIMEOUT = 2000

    fun getLamps(context: Context): List<Lamp> {
        try {
            val connection = URL(BASE_URL + Actions.lamps()).openConnection() as HttpURLConnection
            connection.connectTimeout = TIMEOUT
            connection.readTimeout = TIMEOUT
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val json = connection.inputStream.use { stream ->
                    stream.reader().readText()
                }
                val lampHolder = LampParser.parse(json)
                return lampHolder.lamps.values.toList()
            } else {
                Timber.e("Could not fetch lamps, code: ${connection.responseCode}, message: ${connection.responseMessage}")
            }
        } catch (e: Exception) {
            Timber.e(e, "Could not fetch lamps")
        }
        return listOf()
    }

    fun send(url: String) {
        thread(start = true) {
            try {
                val connection = URL(BASE_URL + url).openConnection() as HttpURLConnection
                connection.connectTimeout = TIMEOUT
                connection.readTimeout = TIMEOUT
                connection.responseCode
                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    Timber.e("Could not send command, code: ${connection.responseCode}, message: ${connection.responseMessage}")
                }
            } catch (e: Exception) {
                Timber.e(e, "Could not send command")
            }
        }
    }
}