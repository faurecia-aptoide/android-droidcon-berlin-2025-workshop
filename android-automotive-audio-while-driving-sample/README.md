# Android Droidcon Berlin 2025 Workshop - Audio While Driving Sample


## Description

This app is straightforward: click on the play button of the thumbnail and watch the video. Simulate
driving by changing car speed and gear on the emulator and the video should be blocked while the
audio is playing.
You can pause/play the video and also seek forward and seek back.
Immersive mode is triggered once the player starts and exited when the user leaves the player.


## Changing the video that is shown in the app

1. Change the video under the res/raw directory.
2. Change the file name & metadata used in the [GetMediaAssetUseCase](https://github.com/faurecia-aptoide/android-droidcon-berlin-2025-workshop/blob/main/android-automotive-audio-while-driving-sample/src/main/java/com/forvia/droidcon/developerworkshop/android_automotive_audio_while_driving_sample/domain/GetMediaAssetUseCase.kt).

```kotlin
class GetMediaAssetUseCase(
    private val context: Context
) {
    operator fun invoke() =
        MediaItem.fromUri(
            "android.resource://${context.packageName}/${R.raw.sample_video}" // Match this with your file's name.
        )
            .buildUpon().setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle("Appning Reveal Carpool")
                    .build()
            ).build()
}
```


## Changing metadata

In the same class, you are also able to change some metadata fields such as the title and the artwork.

```kotlin
class GetMediaAssetUseCase(
    private val context: Context
) {
    operator fun invoke() =
        MediaItem.fromUri(
            "android.resource://${context.packageName}/${R.raw.appning_reveal_carpool_interview_long_version}"
        )
            .buildUpon().setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle("Your cool title goes here")
                    .setArtworkUri(Uri.parse("content://${context.packageName}/artwork/album_01.png"))
                    //.setArtworkUri("YOUR_COOL_IMAGE.jpeg".toUri())
                    .build()
            ).build()
}
```

<img width="1920" height="1080" alt="Screenshot_1758560880" src="https://github.com/user-attachments/assets/04a2c397-bb9c-46da-a94a-4fd1603f6188" />


