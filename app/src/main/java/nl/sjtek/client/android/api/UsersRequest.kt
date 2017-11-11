package nl.sjtek.client.android.api

import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Response
import nl.sjtek.control.data.actions.Actions
import nl.sjtek.control.data.parsers.UserHolder
import nl.sjtek.control.data.parsers.UserParser

internal class UsersRequest(credentials: String) : SelfPostingRequest<UserHolder>(Actions.users(), credentials) {

    override fun parseNetworkResponse(response: NetworkResponse): Response<UserHolder> {
        val data = String(response.data)
        val holder = UserParser.parse(data)
        return if (holder.exception != null) {
            Response.error(ParseError(holder.exception))
        } else {
            Response.success(holder, null)
        }
    }
}