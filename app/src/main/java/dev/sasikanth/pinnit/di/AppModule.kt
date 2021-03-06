package dev.sasikanth.pinnit.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dev.sasikanth.pinnit.data.AppDatabase
import dev.sasikanth.pinnit.notifications.NotificationModule
import dev.sasikanth.pinnit.utils.CoroutineDispatcherProvider
import dev.sasikanth.pinnit.utils.DispatcherProvider
import dev.sasikanth.pinnit.utils.UtcClock

@Module(
  includes = [
    NotificationModule::class,
    AssistedInjectModule::class,
    PreferencesModule::class
  ]
)
object AppModule {

  @AppScope
  @Provides
  fun providesAppDatabase(application: Application): AppDatabase {
    return Room.databaseBuilder(application, AppDatabase::class.java, "pinnit-db")
      .build()
  }

  @AppScope
  @Provides
  fun providesUtcClock(): UtcClock = UtcClock()

  @AppScope
  @Provides
  fun providesDispatcherProvider(): DispatcherProvider = CoroutineDispatcherProvider()
}
