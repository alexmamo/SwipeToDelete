# SwipeToDelete - Clean Architecture with MVVM.

It's an aplication build using Kotlin as an example for how to remove a record from a [Cloud Firestore](https://firebase.google.com/docs/firestore) database, on RecyclerView left/right swipe.

To keep things simple, the app uses a very simple database schema that look like in the image below:

![alt text](https://i.ibb.co/rkmLvqY/Db.jpg)

For getting the data, the app uses [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) together with the MVVM Architecture Pattern. For dependency injection the app uses [Hilt for Android](https://developer.android.com/training/dependency-injection/hilt-android). Besides that, the app also uses data binding to bind UI components in the layouts.

To make this app work, follow the instructions given in the official documentation regarding [how to add Firebase to your project](https://firebase.google.com/docs/android/setup). Add the JSON file in your app folder, add some dummy products and see it working.

See it on youtube: Coming soon