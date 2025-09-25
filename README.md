# Android Droidcon Berlin 2025 Workshop
This repository hosts the project for the Droidcon Berlin 2025 workshop, focusing on
Android for Cars development. The project demonstrates functionalities such as displaying Points of
Interest (POIs) on an Android Auto/Automotive head unit and a sample implementation of push
notifications based on COVESA standards.

## Core Components:

* **POI Display Sample (AAOS):**
    * Implements a `CarAppService` to display a list of Points of Interest.
    * Fetches and maps POI data (mocked, everything is local), including dynamic updates based on
      the current location.
    * Demonstratates how to setup and utilize `Screen Templates` for AAOS.
* **COVESA Push Notifications Sample:**
    * Downloads and installs the Sunup push distributor. Opens when app is first launched.
  * Includes a `BroadcastReceiver` (`PushTriggerBroadcastReceiver`) to listen for and trigger
      notifications.
    * Demonstrates sending messages to the automotive system via `adb` and `CLI` (cURL) for testing
      notification flows.
    * Interacts with a mock application server (`FakeApplicationServer`) for handling and displaying
      these notifications.
* **Audio while Driving Sample (AAOS):**
    * Demonstrates how to manage a media session using `media3` and how to control the media through
      out the lifecycle.
  * Demonstrates how to deal with the car driving restrictions and how to keep audio playing even
      when driving and there is visual restriction due to driving.
* **Vehicle Properties (AAOS):**
    * Downloads, parses and saves the whole content of `VehiclePropertyIds` from the car library in
      order to fill the missing information about the supported Vehicle Properties of the device.
    * Utilizes canonical layouts and a search bar for ease of look up and details visualization.

## Technology Stack:

* **Language:** Kotlin
* **Platform:** AAOS and AOSP
* **Architecture:** MVVM (Model-View-ViewModel) principles
* **Key Libraries & Frameworks:**
* Android for Cars App Library (for `CarAppService`, `Screen`, `Session`, etc.)
* Jetpack Components:
    * ViewModel
    * StateFlow (for reactive UI updates)
* Coroutines & Flow (for asynchronous operations)
* Koin (for Dependency Injection)
* Clean Architecture (for separation of concerns)
* Canonical layouts

## Prerequisites

* Android Studio Iguana | 2023.2.1 or later (ensure you have the latest Android SDK tools).
* Appning AAOS emulator with Android 14.
   * For additional information please have a look at our website: https://appning.com/developers/#test_using_the_appning_emulator.
* Git Large File Storage.
   * Be sure to review the [Setup & Installation section](#setup--installation).
   * This is required for one of the sample applications - Audio While Driving.
* Python3.
   * COVESA Push Notifications Sample will provide a cURL that can be used to dispatch notifications from anywhere - providing a "real-life" test scenario feeling.
   * MacOS: https://www.python.org/downloads/macos/
   * Windows: https://www.python.org/downloads/windows/
* platform-tools included in $PATH


## Setup & Installation

1. Clone this repository.
2. Sync with gradle.

3. Once everything is done, there are two ways of interacting with the project:
    1. Install all apps as a standalone:
    ```shell
    ./gradlew :app:installAllApps
    ```

    2. Install the main app that acts as a gateway to the other apps:
    ```shell
     ./gradlew :app:installPreviewApp
    ```
   This will install the Preview App, POI Sample App and Sunup.

And the setup is done!

There are 5 configurations on Android Studio:

* `app`
* `android-automotive-audio-while-driving-sample-app`
* `android-automotive-covesa-push-notifications-sample-app`
* `android-automotive-poi-sample`
* `android-automotive-vehicle-properties-sample-app`

You can either run each app separately via the Android Studio configuration selector or run the
global `app` and the use the UI to open each apps screens, exception for POI Sample that is a CarApp
and needs to be opened like a different app.

## Changing the package name of the app

If you decide to extend any of the sample apps and want to deploy it you'll need to change the package 
name of the app. To do this please update the defaultConfig.applicationId of the sample app before uploading
to the portal.

## About the apps

### POI Sample
[About](android-automotive-poi-sample/README.md)

### COVESA Push Notifications Sample
[About](android-automotive-covesa-push-notifications-sample/README.md)

### Audio While Driving Sample
[About](android-automotive-audio-while-driving-sample/README.md)

### Vehicle Properties
[About](android-automotive-vehicle-properties-sample/README.md)


### Additional resources
* Sample app build in a [COVESA](https://covesa.global/)'s developer session: https://github.com/Quartettmobile/RoadGallery


## Developer Portal Submission

### APK signing

Before you upload your app you need to build a signed release version, as the app store does not allow
the publication of apps signed with debug signature. To do this you can use 
Build > Generate signed App Bundle or APK, and create a signature if you do not already have one. 


### Submitting your application

1. The very first step is to... create a developer account! :D It's free, just head over to https://developers.appning.com
   1. You will receive a confirmation email, and once that's done - our team can approve your account.
2. Once you're logged in, head over to the "Upload" section under "Manage Apps" > "Apps".
3. Input the package name of your app. Remember, your APK must have a unique package name (and it must not be a debug build).
<img width="1242" height="273" alt="Screenshot 2025-09-25 at 15 21 50" src="https://github.com/user-attachments/assets/26c32852-04c0-40fa-adac-e0f81049bb7e" />

4. Now you will be redirected to a view where you can submit your app's metadata!
5. As an important first step, make sure to set the app as a "Test App"!
<img width="1242" height="232" alt="Screenshot 2025-09-25 at 15 23 19" src="https://github.com/user-attachments/assets/acbc11cd-8286-4512-a899-caac86f8bc22" />

6. Add the remaining required metadata.

<img width="1242" height="232" alt="Screenshot 2025-09-25 at 15 28 18" src="https://github.com/user-attachments/assets/b8c078e3-de6d-4344-a54c-708f1af3e409" />

7. Upload images

App icon ![screenshot02](https://github.com/user-attachments/assets/da71d704-30ac-4bb9-b6c1-35642eb9f5ab)


Screenshots ![screenshot01](https://github.com/user-attachments/assets/23e39d0d-ac1f-4113-8b99-b9758b2d5401)


8. Upload the app!

