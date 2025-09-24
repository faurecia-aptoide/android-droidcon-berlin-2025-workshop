package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.pushmanager

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.model.MessagePayload
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.utils.b64decode
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.utils.b64encode
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.utils.decodePubKey
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.utils.encode
import com.google.crypto.tink.apps.webpush.WebPushHybridEncrypt
import com.google.crypto.tink.subtle.EllipticCurves
import com.google.gson.Gson
import global.covesa.sdk.api.client.push.data.PushEndpoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.net.URL
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.Signature
import java.security.interfaces.ECPublicKey
import java.security.spec.ECGenParameterSpec

/**
 * Fake application server that simulates a real application server.
 *
 * It stores the push endpoint, auth secret, public key, and VAPID public key in DataStore.
 * It also provides methods to send push notifications and generate cURL commands for testing.
 * @param context The application context.
 * @param ds The DataStore instance.
 */
class FakeApplicationServer(
    private val context: Context,
    private val ds: DataStore<Preferences>
) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val _notificationPayloads: MutableStateFlow<List<MessagePayload>> =
        MutableStateFlow(listOf())
    val notificationPayloads = _notificationPayloads.asStateFlow()

    /**
     * Data class to hold the cache data.
     * The cache is loaded once from DataStore and then updated asynchronously.
     */
    private data class Cache(
        val endpoint: String? = null,
        val pubKey: String? = null,
        val auth: String? = null,
        val vapidPub: String? = null
    )

    @Volatile
    private var cache: Cache = runBlocking { loadOnce() }

    /**
     * Loads the cache from DataStore.
     * This function is called only once when the class is instantiated.
     * @return The cache data.
     */
    private suspend fun loadOnce(): Cache {
        val prefs = ds.data.first()
        return Cache(
            endpoint = prefs[KEY_ENDPOINT],
            pubKey = prefs[KEY_PUBKEY],
            auth = prefs[KEY_AUTHKEY],
            vapidPub = prefs[KEY_VAPID_PUBKEY]
        )
    }

    /**
     * Writes the cache to DataStore asynchronously.
     * @param update A function that takes the current cache and returns the updated cache.
     */
    private fun writeAsync(update: Cache.() -> Cache) {
        val next = update(cache)
        cache = next
        scope.launch {
            ds.edit { prefs ->
                prefs[KEY_ENDPOINT] =
                    next.endpoint ?: run { prefs.remove(KEY_ENDPOINT); return@edit }
                prefs[KEY_PUBKEY] = next.pubKey ?: run { prefs.remove(KEY_PUBKEY); return@edit }
                prefs[KEY_AUTHKEY] = next.auth ?: run { prefs.remove(KEY_AUTHKEY); return@edit }
                prefs[KEY_VAPID_PUBKEY] =
                    next.vapidPub ?: run { prefs.remove(KEY_VAPID_PUBKEY); return@edit }
            }
        }
    }

    /** Push endpoint. Should be saved on application server. */
    private var endpoint: String?
        get() = cache.endpoint
        set(value) = writeAsync { copy(endpoint = value) }

    /** WebPush auth secret. Should be saved on application server. */
    private var authSecret: String?
        get() = cache.auth
        set(value) = writeAsync { copy(auth = value) }

    /** WebPush public key. Should be saved on application server. */
    private var pubKey: String?
        get() = cache.pubKey
        set(value) = writeAsync { copy(pubKey = value) }

    /** VAPID public key. Should be saved on application server. */
    private var vapidPubKey: String?
        get() = cache.vapidPub
        set(value) = writeAsync { copy(vapidPub = value) }

    /**
     * Stores a push notification in the list of notification payloads.
     * @param data The push notification payload.
     */
    fun storePushNotification(data: MessagePayload) {
        _notificationPayloads.update { currentList ->
            currentList + data
        }
    }

    /**
     * Sends a WebPush notification.
     * @param content The push notification payload.
     * @param callback A callback function that is called when the notification is sent or an error occurs.
     */
    private fun sendWebPushNotification(
        content: MessagePayload,
        callback: (response: String?, error: VolleyError?) -> Unit
    ) {
        Log.w(TAG, "Sending WebPushNotification with content $content")
        val url = endpoint
        if (url.isNullOrEmpty()) {
            callback(null, VolleyError("No endpoint"))
            return
        }
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        val request = object : StringRequest(
            Method.POST, url,
            Response.Listener { r -> callback(r, null) },
            Response.ErrorListener { e -> callback(null, e) }
        ) {
            override fun getBody(): ByteArray {
                val auth = authSecret?.b64decode()
                val hybridEncrypt = WebPushHybridEncrypt.Builder()
                    .withAuthSecret(auth)
                    .withRecipientPublicKey(pubKey?.decodePubKey() as ECPublicKey)
                    .build()
                val encoded = Gson().toJson(content)
                return hybridEncrypt.encrypt(encoded.toByteArray(), null)
            }

            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Content-Encoding"] = "aes128gcm"
                params["TTL"] = "60"
                params["Urgency"] = "high"
                params["Authorization"] = getVapidHeader()
                return params
            }
        }
        requestQueue.add(request)
    }

    /**
     * Gets the VAPID header.
     * @param sub The subject claim.
     * @return The VAPID header.
     */
    private fun getVapidHeader(sub: String = "mailto"): String {
        val endpointStr = endpoint ?: return ""
        val header = JSONObject()
            .put("alg", "ES256")
            .put("typ", "JWT")
            .toString().toByteArray(Charsets.UTF_8).b64encode()
        val endpointUrl = URL(endpointStr)
        val exp = (System.currentTimeMillis() / 1000) + 43200 // +12h
        val body = JSONObject()
            .put("aud", "${endpointUrl.protocol}://${endpointUrl.authority}")
            .put("exp", exp)
            .put("sub", sub)
            .toString().toByteArray(Charsets.UTF_8).b64encode()
        val toSign = "$header.$body".toByteArray(Charsets.UTF_8)
        val signature = sign(toSign)?.b64encode()
        val jwt = "$header.$body.$signature"
        return "vapid t=$jwt,k=$vapidPubKey"
    }

    /**
     * Generates a VAPID key pair.
     * @return The VAPID key pair.
     */
    private fun genVapidKey(): KeyPair {
        Log.d(TAG, "Generating a new KP.")
        val generator =
            KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, KEYSTORE_PROVIDER)
        generator.initialize(
            KeyGenParameterSpec.Builder(ALIAS, KeyProperties.PURPOSE_SIGN)
                .setAlgorithmParameterSpec(ECGenParameterSpec("secp256r1"))
                .setDigests(KeyProperties.DIGEST_SHA256)
                .setUserAuthenticationRequired(false)
                .build()
        )
        return generator.generateKeyPair().also {
            val pubkey = (it.public as ECPublicKey).encode()
            Log.d(TAG, "Pubkey: $pubkey")
            vapidPubKey = pubkey // persists via DataStore
        }
    }

    /**
     * Signs data using the VAPID private key.
     * @param data The data to sign.
     * @return The signature.
     */
    private fun sign(data: ByteArray): ByteArray? {
        val ks = KeyStore.getInstance(KEYSTORE_PROVIDER).apply { load(null) }
        if (!ks.containsAlias(ALIAS) || !ks.entryInstanceOf(
                ALIAS,
                KeyStore.PrivateKeyEntry::class.java
            )
        ) {
            genVapidKey()
        }
        val entry: KeyStore.Entry = ks.getEntry(ALIAS, null)
        if (entry !is KeyStore.PrivateKeyEntry) {
            Log.w(TAG, "Not an instance of a PrivateKeyEntry")
            return null
        }
        val signature = Signature.getInstance("SHA256withECDSA").run {
            initSign(entry.privateKey)
            update(data)
            sign()
        }.let { EllipticCurves.ecdsaDer2Ieee(it, 64) }
        return signature
    }

    inner class MockApi {

        /**
         * Stores the push endpoint.
         * @param endpoint The push endpoint.
         */
        fun storePushEndpoint(endpoint: PushEndpoint?) {
            this@FakeApplicationServer.endpoint = endpoint?.url
            authSecret = endpoint?.pubKeySet?.auth
            pubKey = endpoint?.pubKeySet?.pubKey
        }

        /**
         * Gets the VAPID public key.
         * If the key doesn't exist, it generates a new one.
         * @return The VAPID public key.
         */
        fun getVapidPubKey(): String {
            val existing = vapidPubKey
            return existing ?: (genVapidKey().public as ECPublicKey).encode()
        }

        /**
         * Gets the public key.
         * @return The public key.
         */
        fun getPubKey(): String = this@FakeApplicationServer.pubKey ?: ""

        /**
         * Gets the VAPID header.
         * @return The VAPID header.
         */
        fun getVapidHeader(): String = this@FakeApplicationServer.getVapidHeader()

        /**
         * Gets the auth secret.
         * @return The auth secret.
         */
        fun getAuthSecret(): String? = this@FakeApplicationServer.authSecret

        /**
         * Gets the endpoint.
         * @return The endpoint.
         */
        fun getEndpoint(): String? = this@FakeApplicationServer.endpoint

        /**
         * Sends a push notification.
         * @param content The push notification payload.
         */
        fun sendPushNotification(content: MessagePayload) {
            sendWebPushNotification(content) { responseString, e ->
                val message = when {
                    e != null -> {
                        val data = try {
                            e.networkResponse?.data?.decodeToString()
                        } catch (_: Throwable) {
                            null
                        }
                        "An error occurred: $e with message ${e.cause?.localizedMessage}, data: $data and code: ${e.networkResponse?.statusCode}"
                    }

                    responseString != null -> "Notification sent with content: $content"
                    else -> "Unknown result"
                }
                context.mainExecutor.execute { Log.i(TAG, message) }
            }
        }
    }

    private companion object {
        const val TAG = "FakeApplicationServer"
        const val KEYSTORE_PROVIDER = "AndroidKeyStore"
        const val ALIAS = "ApplicationServer"
        private val KEY_ENDPOINT = stringPreferencesKey("endpoint")
        private val KEY_PUBKEY = stringPreferencesKey("pubkey")
        private val KEY_AUTHKEY = stringPreferencesKey("authkey")
        private val KEY_VAPID_PUBKEY = stringPreferencesKey("vapidPubkey")
    }
}
