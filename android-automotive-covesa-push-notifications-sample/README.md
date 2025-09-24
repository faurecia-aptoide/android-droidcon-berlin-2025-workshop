# Android Droidcon Berlin 2025 Workshop - Push Notifications

On the first app opening you will be prompted to accept notifications permissions. After accepting,
the app distributor `Sunup` will automatically open and some seconds later you will receive a
notification saying that you can come back to the app. You can click on the notification and you
will be redirected back to the app.
This app allows you to send 2 type of notifications:

1. Push notification with a proper notification to be show to the user
   <img width="1408" height="792" alt="Screenshot_1758639770" src="https://github.com/user-attachments/assets/e38b2d57-af40-4631-8b8c-74af91ced5b2" />

2. Push notification that contains only a payload
   <img width="1408" height="792" alt="Screenshot_1758639773" src="https://github.com/user-attachments/assets/5c6c3afe-0207-4d28-a222-b2d0eb16c4fd" />


You can switch between them using the `Switch` on the UI.
There are 2 buttons: one to send the desired notification and one to generate the `cURL` for the
type of notification that you are testing. Search for `MessagePayloadUtils` on the logcat.
If the notification is successfully sent, you will notice a counter increment on the type of
notification you just sent. By clicking on it, a list of received notifications will be show on
the right side, using Canonical Layouts.

> [!NOTE]
> Notifications sent using the generated curl will go to an actual notification server (https://updates.push.services.mozilla.com/wpush) via Sunup, while notifications sent by pressing the send button on the app will use mock server implemented in the app code.
