# Project : Food Recipe Share Kotlin Multiplatform / Compose Multiplatform App

### [Dongguo Wu](https://github.com/dongguowu)

### [Garret](https://github.com/Aeternitas460/food_recipe_share_garett)

### [Xiaowei](https://github.com/xiaoqianniu/food_recipe_share)

## Log

- Minimum SDK: API:30: Android 11.0(R)
- Physical Samsung Mobile Phone, Android version 11
- com.dishdiscoverers.foodrecipe
- FoodRecipeShare
- create another module, core, who does not have access to any Android code
- New Module / java Library
- add library navigation

## Gradle plugins configure

Using the plugins DSL: plugins { id("org.jetbrains.kotlin.android") version "1.9.0" }

Using legacy plugin application: buildscript { repositories { maven { url = uri("https://plugins.gradle.org/m2/") } } dependencies { classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0") } }

apply(plugin = "org.jetbrains.kotlin.android")

## Functionality

### Recipe Search from large real food recipe data

<img src="readme_images/steak_search.png" alt="Image Description" width="200" height=""> <img src="readme_images/sushi_search.png" alt="Image Description" width="200" height="">

### Recipe user review ( favorite, comments)

<img src="readme_images/fish_comments.png" alt="Image Description" width="200" height=""> <img src="readme_images/sushi_comment.png" alt="Image Description" width="200" height="">

### User Registration and Authentication

<img src="readme_images/login.png" alt="Image Description" width="200" height=""> <img src="readme_images/signup.png" alt="Image Description" width="200" height="">

### User Profile

<img src="readme_images/profile.png" alt="Image Description" width="200" height=""> <img src="readme_images/update.png" alt="Image Description" width="200" height=""> Implement CRUD

## Database Design and Implementation

- User Auth: create new, login, update password
- Recipe: search by title
- User-favorite-Recipe: create, delete
- Recipe-comments: create, getAllByRecipeID
- Ingredient and Nutrient are not implemented

<img src="readme_images/database.png" alt="Image Description" width="400" height="">
<img src="readme_images/firebase.png" alt="Image Description" width="400" height="">

## Technology

- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
- Firebase Authentication
- Firebase Database
- [Ktor client](https://ktor.io/docs/create-client.html)
- [Voyager](https://github.com/adrielcafe/voyager) navigation
- JetPack Compose / Material3 Components
- Material3 Theme
- Git GitHub
- [Napier](https://github.com/AAkira/Napier) Logger
- [Kotlin Multiplatform Libraries](https://github.com/AAkira/Kotlin-Multiplatform-Libraries)
- [TheMealDB](https://www.themealdb.com/api.php)
- [CalorieNinjas](https://calorieninjas.com/api)
- [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver)
- [OkHttp](https://square.github.io/okhttp/)
- [Recommendations for Android architecture](https://developer.android.com/topic/architecture/recommendations)
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- [Hilt and Dagger](https://developer.android.com/training/dependency-injection/hilt-android#hilt-and-dagger)

## Next

- Registered user creates and shares her own recipe
- Add pictures by phone camera in the comments
- Food recipe nutrition calculated by ingredients
- Food recipe rating computing by user favorite recipes data, and user positive / negative comments
- Search or Generate recipe picture (call AI api)
