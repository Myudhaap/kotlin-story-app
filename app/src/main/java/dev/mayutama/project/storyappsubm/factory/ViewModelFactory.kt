package dev.mayutama.project.storyappsubm.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.mayutama.project.storyappsubm.config.DataStoreConfig
import dev.mayutama.project.storyappsubm.di.Injection
import dev.mayutama.project.storyappsubm.ui.splash.SplashViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(
    private val dataStoreConfig: DataStoreConfig
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(dataStoreConfig) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(application: Application): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideDataStore(application)
                )
            }.also { instance = it }
        }
    }
}