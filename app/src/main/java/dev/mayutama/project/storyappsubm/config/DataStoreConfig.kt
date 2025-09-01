package dev.mayutama.project.storyappsubm.config

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("setting")

class DataStoreConfig private constructor(private val dataStore: DataStore<Preferences>) {

    companion object {
        @Volatile
        private var instance: DataStoreConfig? = null

        fun getInstance(dataStore: DataStore<Preferences>): DataStoreConfig {
            return instance ?: synchronized(this) {
                instance ?: DataStoreConfig(dataStore)
            }.also { instance = it }
        }
    }
}