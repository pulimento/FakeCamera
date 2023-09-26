Creating this kind of Android application is a bit complex, but below is the high-level guide on making it possible:

1. App Permissions: You need to define CAMERA and usage of other external storage permission in your AndroidManifest.xml.

```
<uses-permission android:name="android.permission.CAMERA" />
<uses-feature android:name="android.hardware.camera" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```

2. Define a Camera Intent: You need to define an intent-filter within your camera activity that can respond to the android.media.action.IMAGE_CAPTURE action. This will make your app available to be chosen when a photo needs to be taken.

```
<activity android:name=".YourCameraActivity">
  <intent-filter>
    <action android:name="android.media.action.IMAGE_CAPTURE" />
    <category android:name="android.intent.category.DEFAULT" />
  </intent-filter>
</activity>
```

3. Implementing the Camera Feature: Create a new activity to handle the intent. You will need to use `Camera API` or `Camera2 API` based on the device or you can use libraries like `CameraX` which abstracts the complexities for different devices.

4. Passing the taken Photo back to calling App: You will need to send the image data back to the calling app in the form of a Uri. To do so, create a content provider and add it to your manifest, and use the `FileProvider.getUriForFile()` method to get the Uri for the image file.

```
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.provider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/provider_paths"/>
</provider>
```

This is a high-level guide and does not cover everything. Also, remember to take user's consent to record and use device's camera before you do so due to privacy reasons. It's always recommended to have a deep understanding of the Android Manifest file, Intents, Camera API, Storage API and the general workings of Android before moving on to build this app.

Please note, starting from Android 11 (API level 30), if an app invokes the Intent that uses the ACTION_IMAGE_CAPTURE, ACTION_VIDEO_CAPTURE, or related intents, the system automatically directs the captured media only to the authorized storage location for the app that invoked the camera. (For an image file, the system invokes the system media store, and the user does not have to grant the app access to device storage.)