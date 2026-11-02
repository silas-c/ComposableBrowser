# Composable Browser

Compose version of the Basic Browser class activity. Enter an address and tap
Go to load it in a WebView wrapped with `AndroidView`. Addresses without an
HTTP/HTTPS prefix get `https://` added. Blank input is ignored.

The URL field and Go button use Material 3. The page fills the remaining space.
Typing in the field does not reload the page. HTTP addresses are supported;
JavaScript is enabled for websites that need it.

## Build

Open in Android Studio, or use JDK 21 and an Android SDK:

```sh
./gradlew assembleDebug lintDebug testDebugUnitTest
./gradlew connectedDebugAndroidTest
```

The device test requires internet access and checks that `wikipedia.com` loads
in the embedded WebView. Verified on an Android 15 emulator; the bundled
Compose test library is incompatible with the Android 17 emulator. The emulator reported graphics-surface errors, so its blank screenshots could
not verify appearance; review the app visually in Android Studio before submission.
