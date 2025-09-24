package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.utils

import android.util.Base64
import android.util.Log
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.model.MessagePayload
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.model.PreparedWebPush
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.pushmanager.FakeApplicationServer
import com.google.crypto.tink.apps.webpush.WebPushHybridEncrypt
import com.google.gson.Gson


private const val MESSAGE_PAYLOAD_UTILS_TAG = "MessagePayloadUtils"

/**
 * Prepares a WebPush notification.
 * @param content The push notification payload.
 * @return The prepared WebPush notification.
 */
fun FakeApplicationServer.prepareWebPush(content: MessagePayload?): PreparedWebPush {
    val endpoint = this.MockApi().getEndpoint()
    val authSecret = this.MockApi().getAuthSecret()
    val pubKey = this.MockApi().getPubKey()
    val vapidHeader = this.MockApi().getVapidHeader()

    val url = endpoint ?: error("No endpoint")
    val auth = authSecret?.b64decode()
    val hybridEncrypt = WebPushHybridEncrypt.Builder()
        .withAuthSecret(auth)
        .withRecipientPublicKey(pubKey.decodePubKey())
        .build()
    val json = Gson().toJson(content)
    val body = content?.let {
        hybridEncrypt.encrypt(json.toByteArray(), null)
    }
    val headers = mapOf(
        "Content-Encoding" to "aes128gcm",
        "TTL" to "60",
        "Urgency" to "high",
        "Authorization" to vapidHeader
    )
    return PreparedWebPush(url, headers, body)
}

/**
 * Builds a cURL command string for sending a WebPush notification.
 * @param content The push notification payload.
 * @return The cURL command string.
 */
fun FakeApplicationServer.buildCurlCommand(content: MessagePayload?): String {
    val prep = prepareWebPush(content)

    val bodyB64 = content?.let {
        Base64.encodeToString(prep.body, Base64.NO_WRAP)
    } ?: run {
        "$(printf '%s' \"${'$'}JSON\" | base64 | tr -d '\\n')"
    }

    fun escSingleQuotes(s: String) = s.replace("'", "'\"'\"'")

    val hEnc = escSingleQuotes(prep.headers["Content-Encoding"] ?: "aes128gcm")
    val hTtl = escSingleQuotes(prep.headers["TTL"] ?: "60")
    val hUrg = escSingleQuotes(prep.headers["Urgency"] ?: "high")
    val hAuth = escSingleQuotes(prep.headers["Authorization"] ?: "")

    val title = content?.notification?.title ?: "New message from Appning"
    val body = content?.notification?.body ?: "Hey! Are you enjoying the workshop?"
    val payload = content?.data?.payload
        ?: "Hey! Are you enjoying the workshop? This is a payload from Appning."

    val notification = content?.notification?.let {
        """
                {
                    "title": "$title",
                    "body": "$body"
                }
            """.trimIndent()
    } ?: run { null }

    val script = CurlTemplate.format(
        notification = notification,
        payload = payload,
        url = prep.url,
        bodyB64 = bodyB64,
        hEnc = hEnc,
        hTtl = hTtl,
        hUrg = hUrg,
        hAuth = hAuth
    )

    return script
}

/**
 * Logs and returns a cURL command string for sending a WebPush notification.
 * @param content The push notification payload.
 * @return The cURL command string.
 */
fun FakeApplicationServer.logAndShowCurlWithContent(content: MessagePayload?): String {
    val curl = buildCurlCommand(
        content = content,
    ).trim()
    Log.i(
        MESSAGE_PAYLOAD_UTILS_TAG,
        "----------------------------------------------------------------"
    )
    Log.i(MESSAGE_PAYLOAD_UTILS_TAG, "\n$curl")
    Log.i(
        MESSAGE_PAYLOAD_UTILS_TAG,
        "----------------------------------------------------------------"
    )
    return curl
}
