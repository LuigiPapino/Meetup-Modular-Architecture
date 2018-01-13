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

Note:
needed to understand tha packageId
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

