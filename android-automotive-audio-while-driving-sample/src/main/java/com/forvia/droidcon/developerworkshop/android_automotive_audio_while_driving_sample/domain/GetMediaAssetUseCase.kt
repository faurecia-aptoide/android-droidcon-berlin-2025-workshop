package com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.domain

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.forvia.droidcon.developerworkshop.android_automotive_audio_while_driving_sample.R

class GetMediaAssetUseCase(
    private val context: Context
) {
    operator fun invoke() =
        MediaItem.fromUri(
            "android.resource://${context.packageName}/${R.raw.sample_video}"
        )
            .buildUpon().setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(context.getString(R.string.video_title))
                    .build()
            ).build()
}
