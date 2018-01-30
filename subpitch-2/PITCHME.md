@title[Dagger2 - Application Component]
##### Dagger2 - Application Component 
```kotlin
@Singleton
@Component(modules = arrayOf(NetworkModule::class, RepositoryModule::class, SubcomponentModule::class))
interface ApplicationComponent : AndroidInjector<MyApplication> {
  val userRepository: UserRepository
  val apiService: ApiService
  fun browserBuilder(): BrowserSubComponent.Builder
}

@Module
object NetworkModule {
  @Provides
  @Singleton
  @JvmStatic
  fun provideApiService(okHttp: OkHttp): ApiService {
    return ApiService(okHttp)
  }
 }
```

@[2](component declaration with list of modules)
@[4-5](dependecy exposed)
@[6](subcomponent builder)
@[9-10](module declaration)
@[11](this is a provide method for ApiService)
@[14-16](build the dependency)


---
@title[Dagger2 - Application Injection]
##### Dagger2 - Application Injection 
```kotlin
class MyApplication : BaseApplication() {
  lateinit var appComponent: ApplicationComponent 
  override fun onCreate() {
    appComponent = DaggerApplicationComponent
      .builder()
      .create(this)
  }
}
```
@[4-6](component creation)
---
