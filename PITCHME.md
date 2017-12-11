@title[Introduction]

# <span class="gold">Instant App</span>

### Multi-Modular Architecture with Android Gradle Plugin 3.0
<br>
<br>
<span class="byline">[luigi.papino@gmail.com - I Hate Android]</span>

---

@title[Instant App - What? email flow]

#### Android Apps Without Installation
![Video](https://www.youtube.com/embed/fGdiOeYuORE)


---
@title[Instant App - What? install prompt]

#### Android Apps Without Installation
![Video](https://www.youtube.com/embed/b-dMmGOB0nA)


---
@title[Instant App - What? show case]

#### Android Apps Without Installation
![Video](https://www.youtube.com/embed/JUykpWHtgec)


---
@title[Instant App - Why?]
### Remove install friction
> Vimeo has seen sessions more than double in length (+130% increase) and native app [users increase 20%.][1] 

<br>

> NYTimes Crossword is used more frequently and those using the instant app have [more than doubled the number of sessions.][2] 

[1]: https://developer.android.com/stories/instant-apps/vimeo.html
[2]: https://developer.android.com/stories/instant-apps/nytimes-crossword.html

---
@title[Instant App - How?]
#### App split by module
![Structure](assets/images/aia-how-structure.png)

[^3]: https://developer.android.com/topic/instant-apps/getting-started/structure.html "AIA Structure" 


---
@title[Instant App - AS How?]
#### App split by module - Android Studio
![ASStructure](assets/images/aia-studio-multi.png)

---
@title[Instant App - Base/Gradle]
#### base/build.gradle
```gradle
apply plugin: 'com.android.feature'
android {
  baseFeature true
  ...
}
dependencies {
  application project(':app-installed')
  feature project(':feature-home')
  feature project(":feature-details")
  ...
}
```

@[1](very similar to `com.android.library`, Google says)
@[3](flag this module as base feature for this project)
@[7](the base feature must know the application module, needed for the `applicationId` and flavors)
@[8-9](feature modules that this base feature will serve)

---
@title[Instant App - Installed/Gradle]
#### app-installed/build.gradle
```gradle
apply plugin: 'com.android.application'
android { ... }
dependencies {
    implementation project(':feature-home')
    implementation project(":feature-details")
    implementation project(':base')
}

```

@[1](nothing changed)
@[4-6](features and base modules that are part of the installed app)

---
@title[Instant App - Instant/Gradle]
#### app-instant/build.gradle
```gradle
apply plugin: 'com.android.instantapp'
dependencies {
    implementation project(':feature-home')
    implementation project(":feature-details")
    implementation project(':base')
}
```

@[1](instantapp plugin that will generate the .zip file with all the features)
@[3-5](features and base modules that are part of the installed app)

---
@title[Instant App - Feature/Gradle]
#### feature-home/build.gradle
```gradle
apply plugin: 'com.android.feature'
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-android-extensions'
android { ... }
dependencies {
    implementation project(':base')
...   
}
```

@[1](very similar to `com.android.library`, Google says)
@[6](dependency on base module)

---
@title[Instant App - APK Installed]
#### Installed APK
![InstalledAPK](assets/images/aia-apk-installed.png)

---
@title[Instant App - APK Instant]
#### Installed APK
![InstantAPK](assets/images/aia-apk-instant.png)

---
@title[Instant App - Issues]
### AIA Issues
- Multi-features not working (fixed last week, Google says)
- Proguard not working
- Base module resources need to be referenced full-scoped
- Resources in the feature manifest file has to be in the base module
- ButterKnife/AndroidAnnotation needs workaround for view-injection
- Crashlytics has to be applied in the base.gradle
- 4MB base+feature size limit
 
---
@title[Multi-Module - Structure]
#### AIA not necessary to build a multi-module project 
![MM-AS-Structure](assets/images/mm-structure.png)

---
@title[Multi-Module - Google I/0 2017 Painful]

#### Modularity is painful
![Video](https://www.youtube.com/embed/7ll-rkLCtyk?rel=0&amp;start=1420)


---
@title[Multi-Module - Google I/0 2017 Dagger2]

#### Modularity and Dagger2
![Video](https://www.youtube.com/embed/7ll-rkLCtyk?rel=0&amp;start=1965)

---
@title[Dagger2 - Definition]

### Dagger2
> [Dagger 2 is a compile-time evolution approach to dependency injection.][1] Taking the approach started in Dagger 1.x to its ultimate conclusion, Dagger 2.x eliminates all reflection, and improves code clarity by removing the traditional ObjectGraph/Injector in favor of user-specified @Component interfaces.

[1]: https://github.com/google/dagger

---

@title[Dagger2 - Dependency Graph]
#### Dependency Graph
![InstalledAPK](assets/images/dagger2-main.png)

---

@title[Dagger2 - Application]
##### Dependency Graph - Application
![InstalledAPK](assets/images/dagger2-application.png)

---

@title[Dagger2 - Application, code]
##### Dependency Graph - Application
```kotlin
@Singleton
@Component(modules = arrayOf(NetworkModule::class, RepositoryModule::class))
interface ApplicationComponent : AndroidInjector<MyApplication> {
  val userRepository: UserRepository
  val apiService: ApiService
  
  fun browserSubComponentBuilder(): BrowserSubComponent.Builder
}

@Module
object NetworkModule {
  @Provides
  @Singleton
  @JvmStatic
  fun provideUserRepository(sharedPrefs: SharedPreference): UserRepository {
    return UserRepository(sharedPrefs)
  }
 }
```

@[2](component declaration with list of modules)
@[4](dependecy exposed)
@[7](builder for subcomponent)
@[10-11](module declaration)
@[12](this is a provide method for UserRepository)
@[15-17](build the dependency)
---

@title[Dagger2 - Main]
##### Dependency Graph - Browser
![InstalledAPK](assets/images/dagger2-browser.png)

---