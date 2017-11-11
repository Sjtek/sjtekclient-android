package nl.sjtek.client.android.api

import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Response
import nl.sjtek.control.data.actions.Actions
import nl.sjtek.control.data.parsers.LampHolder
import nl.sjtek.control.data.parsers.LampParser

internal class LampsRequest(credentials: String) : SelfPostingRequest<LampHolder>(Actions.lamps(), credentials) {

    override fun parseNetworkResponse(response: NetworkResponse): Response<LampHolder> {
        val data = String(response.data)
        val holder = LampParser.parse(data)
        return if (holder.exception != null) {
            Response.error(ParseError(holder.exception))
        } else {
            Response.success(holder, null)
        }
    }
}