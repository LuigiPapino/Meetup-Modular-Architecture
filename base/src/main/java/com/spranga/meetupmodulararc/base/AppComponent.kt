package com.spranga.meetupmodulararc.base

import android.app.Application
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
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
    modules = [(NetworkModule::class), (RepositoryModule::class)])
interface AppComponent : AndroidInjector<Application> {
  val apiService: ApiService
  val userRepository: UserRepository
  val okHttp: OkHttp
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


@Scope
@kotlin.annotation.Retention(RUNTIME)
annotation class Browser


class BrowserService(okHttp: OkHttp)

@Browser
@Component(modules = [(BrowserModule::class)], dependencies = [(AppComponent::class)])
interface BrowserComponent : AndroidInjector<AppCompatActivity> {
  val browserService: BrowserService

  @Component.Builder
  abstract class Builder : AndroidInjector.Builder<AppCompatActivity>() {
    abstract fun plus(component: AppComponent): Builder
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

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    DaggerBrowserComponent
        .builder()
        .plus((application as MyApplication).component)
        .create(this)
        .inject(this)

  }
}