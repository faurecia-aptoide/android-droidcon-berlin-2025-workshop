package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.pushmanager

import global.covesa.sdk.api.client.push.PushManager

sealed class RegistrationAction {
    object Success : RegistrationAction()
    class FailedWithVapid(val exception: PushManager.VapidNotValidException) : RegistrationAction()
    object Failed : RegistrationAction()
}
