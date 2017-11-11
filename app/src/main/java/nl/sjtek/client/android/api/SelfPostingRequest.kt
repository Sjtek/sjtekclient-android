package nl.sjtek.client.android.api

import org.greenrobot.eventbus.EventBus

internal abstract class SelfPostingRequest<T>(action: String, credentials: String) :
        AuthenticatedRequest<T>(Method.GET, API.BASE_URL + action, credentials, { response: T -> EventBus.getDefault().post(response) }, {})