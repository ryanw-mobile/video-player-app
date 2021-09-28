# DAZN Code Challenge - Android TV

## Requirements

* Android Studio Arctic Fox 2021.3.1 Patch 2
* Android device or simulator running Android 5.0+ (API 21)

## Setting up the keystore
* To ensure sensitive data are not being pushed to Git by accident, the keystore and its passwords are kept one level up of the project folder, so they are not managed by Git.

* If your project folder is at `/app/dazn-code-challenge/`, the keystore file and `keystore.properties` should be placed at `/app/`

* The format of `keystore.properties` is:
  ```
     store=/app/release-key.keystore
     alias=<alias>
     pass=<alias password>
     storePass=<keystore password>
  ```
## Building the App
### Build and install on the connected device

   ```
   ./gradlew installDebug
   // or
   // ./gradlew installRelease
   ```

* Options are: `Debug`, `Release`
* Debug builds will have an App package name suffix `.debug` 

### Build and sign an apk for distribution

   ```
   ./gradlew clean && ./gradlew assembleRelease
   ```

* The generated apk(s) will be stored under `app/build/outputs/apk/`

## Dependencies
* [Android X](https://developer.android.com/jetpack/androidx/) - [Android Open Source Project](https://source.android.com/) — [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
* [Glide](https://github.com/bumptech/glide) - [Bump Technologies](https://github.com/bumptech) - [BSD, part MIT and Apache 2.0](https://github.com/bumptech/glide/blob/master/LICENSE)
* [Timber](https://github.com/JakeWharton/timber) - [Jake Wharton](https://github.com/JakeWharton) - [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)