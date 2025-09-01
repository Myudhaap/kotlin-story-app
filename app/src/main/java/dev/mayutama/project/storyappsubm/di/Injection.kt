package dev.mayutama.project.storyappsubm.di

import android.app.Application
import dev.mayutama.project.storyappsubm.config.DataStoreConfig
import dev.mayutama.project.storyappsubm.config.dataStore

object Injection {
    fun provideDataStore(application: Application): DataStoreConfig {
        return DataStoreConfig.getInstance(application.dataStore)
    }
}