# DAZN Code Challenge - Android TV

This is a code test project I have previously submitted as a part of the interview process. The
project covers common RESTApi, SQLite, RecyclerView, Constraint Layout, MVVm, plus dependency
injection and testings.

Although the title carries "Android TV", it has nothing to do with that - this is an Android mobile
App as required by the specifications. Unless indicated above the code snippets, this project was
completed by myself alone.

Under the commit history, you will see how I have built everything from scratch - with rounds of
refactoring and bug fixes. The interview process was concluded in October, 2021, but I am still
keep on improving the codes for demonstration purpose.

As for my technical background:
I have casually used Kotlin by converting Java codes on Android Studio. I have completed Udacity
Kotlin courses in August and September 2021. This project demonstrates what I am able to code
immediately by applying the best up-to-date practices I have just learnt.

Please note that the APIs are supplied by DAZN for recruitment purpose. They may not work at any
time, as we have already concluded the interview process.


## Screenshots

![Screenshot1](screenshots/screen0.png) ![Screenshot1](screenshots/screen1.png) ![Screenshot1](screenshots/screen2.png)

## Requirements

* Android Studio Arctic Fox 2021.3.1 Patch 2
* Android device or simulator running Android 5.0+ (API 21)

## Setting up the keystore

* Android keystore is not being stored in this repository. You need your own keystore to generate
  the apk / App Bundle

* To ensure sensitive data are not being pushed to Git by accident, the keystore and its passwords
  are kept one level up of the project folder, so they are not managed by Git.

* If your project folder is at `/app/dazn-code-challenge/`, the keystore file
  and `keystore.properties` should be placed at `/app/`

* The format of `keystore.properties` is:
  ```
     store=/app/release-key.keystore
     alias=<alias>
     pass=<alias password>
     storePass=<keystore password>
  ```

## Unit tests

* Tests can be executed on Android Studio, by choosing the tests to run
* Command line options are: ` ./gradlew testDebugUnitTest` and `./gradlew testReleaseUnitTest`

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
* Other usages can be listed using `./gradelew tasks`

## (incomplete list of) Dependencies

* [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines) - [Kotlin](https://kotlinlang.org/) - [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
* [Dagger Hilt](https://dagger.dev/hilt/) - [The Dagger Authors](https://dagger.dev/hilt/) - [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
* [Glide](https://github.com/bumptech/glide) - [Bump Technologies](https://github.com/bumptech) - [BSD, part MIT and Apache 2.0](https://github.com/bumptech/glide/blob/master/LICENSE)
* [Timber](https://github.com/JakeWharton/timber) - [Jake Wharton](https://github.com/JakeWharton) - [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
* [Retrofit](https://square.github.io/retrofit/) - [Square, Inc.](https://squareup.com/) - [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
* [Moshi](https://github.com/square/moshi) - [Square, Inc.](https://squareup.com/) - [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
* [Hamcrest](http://hamcrest.org/JavaHamcrest/) - [hamcrest.org](http://hamcrest.org/) - [BSD 3-Clause Licence](https://opensource.org/licenses/BSD-3-Clause)
* [ExoPlayer](https://github.com/google/ExoPlayer) - [Google Open Source](https://opensource.google/) - [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)