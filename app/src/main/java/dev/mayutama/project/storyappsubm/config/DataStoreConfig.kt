package dev.mayutama.project.storyappsubm.config

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.mayutama.project.storyappsubm.domain.AuthInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("preference")

class DataStoreConfig private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveAuth(token: String, username: String, userId: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[USER_NAME_KEY] = username
            preferences[USER_ID_KEY] = userId
        }
    }

    fun getAuth(): Flow<AuthInfo> {
        return dataStore.data.map { preferences ->
            AuthInfo(
                token = preferences[TOKEN_KEY],
                userId = preferences[USER_ID_KEY],
                username = preferences[USER_NAME_KEY]
            )
        }
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val USER_NAME_KEY = stringPreferencesKey("user_name_key")
        private val USER_ID_KEY = stringPreferencesKey("user_id_key")
        private val TOKEN_KEY = stringPreferencesKey("token_key")

        @Volatile
        private var instance: DataStoreConfig? = null

        fun getInstance(dataStore: DataStore<Preferences>): DataStoreConfig {
            return instance ?: synchronized(this) {
                instance ?: DataStoreConfig(dataStore)
            }.also { instance = it }
        }
    }
}