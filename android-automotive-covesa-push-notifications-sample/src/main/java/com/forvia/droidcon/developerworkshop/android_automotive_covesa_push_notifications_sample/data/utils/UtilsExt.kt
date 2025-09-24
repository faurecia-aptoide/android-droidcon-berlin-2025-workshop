package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.utils

import android.content.Context
import android.content.Intent
import android.util.Base64
import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ReturnWorker
import com.google.crypto.tink.subtle.EllipticCurves
import java.security.KeyFactory
import java.security.interfaces.ECPublicKey
import java.security.spec.ECPublicKeySpec
import java.util.concurrent.TimeUnit


/**
 * Decode [ECPublicKey] from [String]
 */
fun String.decodePubKey(): ECPublicKey {
    val point = EllipticCurves.pointDecode(
        EllipticCurves.CurveType.NIST_P256,
        EllipticCurves.PointFormatType.UNCOMPRESSED, this.b64decode()
    )
    val spec = EllipticCurves.getCurveSpec(EllipticCurves.CurveType.NIST_P256)
    return KeyFactory.getInstance("EC").generatePublic(ECPublicKeySpec(point, spec)) as ECPublicKey
}

/**
 * Encode [ECPublicKey] to [String]
 */
fun ECPublicKey.encode(): String {
    val points = EllipticCurves.pointEncode(
        EllipticCurves.CurveType.NIST_P256,
        EllipticCurves.PointFormatType.UNCOMPRESSED,
        this.w
    )
    return points.b64encode()
}

/**
 * Base64 decode, url safe, no padding
 */
fun String.b64decode(): ByteArray {
    return Base64.decode(
        this,
        Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING,
    )
}

/**
 * Base64 encode, url safe, no padding
 */
fun ByteArray.b64encode(): String {
    return Base64.encode(
        this,
        Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING,
    ).toString(Charsets.UTF_8)
}


/**
 * Opens Sunup distributor app and schedules a worker to return to this app.
 */
fun Context.openOtherAppAndScheduleReturn() {
    val targetPkg = "org.unifiedpush.distributor.sunup"

    val work = OneTimeWorkRequestBuilder<ReturnWorker>()
        .setInitialDelay(3, TimeUnit.SECONDS)
        .addTag("return-to-app")
        .build()

    WorkManager.getInstance(this).enqueueUniqueWork(
        "return-to-app", ExistingWorkPolicy.REPLACE, work
    )

    val launch = packageManager.getLaunchIntentForPackage(targetPkg)?.apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    if (launch != null) startActivity(launch)
    else Log.e("openOtherAppAndScheduleReturn", "Could not launch $targetPkg")
}
