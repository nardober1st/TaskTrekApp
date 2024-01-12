package com.oechslerbernardo.mongodbteste.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.oechslerbernardo.mongodbteste.data.PreferencesManager
import com.oechslerbernardo.mongodbteste.data.dataStore
import com.oechslerbernardo.mongodbteste.data.repository.AuthRepositoryImpl
import com.oechslerbernardo.mongodbteste.data.repository.MainRepositoryImpl
import com.oechslerbernardo.mongodbteste.domain.repository.AuthRepository
import com.oechslerbernardo.mongodbteste.domain.repository.MainRepository
import com.oechslerbernardo.mongodbteste.util.Constants.APP_ID
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.AppConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRealApp(): App {
        return App.create(APP_ID)
    }

    @Provides
    @Singleton
    fun providesRepositoryImpl(app: App): AuthRepository {
        return AuthRepositoryImpl(app)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun providePreferencesManager(dataStore: DataStore<Preferences>): PreferencesManager {
        return PreferencesManager(dataStore)
    }

    @Provides
    @Singleton
    fun provideThemeRepository(preferencesManager: PreferencesManager): MainRepository {
        return MainRepositoryImpl(preferencesManager)
    }
}