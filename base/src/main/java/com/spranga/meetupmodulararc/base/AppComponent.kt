package com.spranga.meetupmodulararc.base

import android.app.Application
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import dagger.android.AndroidInjector
import javax.inject.Inject
import javax.inject.Scope
import javax.inject.Singleton
import kotlin.annotation.AnnotationRetention.RUNTIME

/**
 * Created by nietzsche on 11/12/17.
 */

class OkHttp

class ApiService(private val okHttp: OkHttp)
class UserRepository

@Singleton
@Component(
    modules = [(NetworkModule::class), (RepositoryModule::class), (SubcomponentModule::class)])
interface AppComponent : AndroidInjector<Application> {
  val apiService: ApiService
  val userRepository: UserRepository

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

  @Provides
  @Singleton
  @JvmStatic
  fun provideOkHttp(): OkHttp {
    return OkHttp()
  }
}

@Module
object RepositoryModule {

  @Provides
  @Singleton
  @JvmStatic
  fun provideUserRepository(): UserRepository {
    return UserRepository()
  }

}

@Module(subcomponents = [(BrowserSubComponent::class)])
object SubcomponentModule {

}


@Scope
@kotlin.annotation.Retention(RUNTIME)
annotation class Browser


class BrowserService(okHttp: OkHttp)

@Browser
@Subcomponent(modules = [(BrowserModule::class)])
interface BrowserSubComponent : AndroidInjector<AppCompatActivity> {
  val browserService: BrowserService

  @Subcomponent.Builder
  abstract class Builder : AndroidInjector.Builder<AppCompatActivity>() {

  }
}

@Module
object BrowserModule {
  @Provides
  @Browser
  @JvmStatic
  fun provideBrowserService(okHttp: OkHttp): BrowserService {
    return BrowserService(okHttp)
  }
}

class MyApplication : Application() {

  internal lateinit var component: AppComponent
  override fun onCreate() {
    super.onCreate()
    component = DaggerAppComponent.builder().build()
  }
}

class BrowserActivity : AppCompatActivity() {
  @Inject
  internal lateinit var okhttp: OkHttp

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    (application as MyApplication).component.browserBuilder().build()
        .inject(this)
  }
}