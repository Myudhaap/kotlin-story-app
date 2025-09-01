package dev.mayutama.project.storyappsubm.ui.splash

import androidx.lifecycle.ViewModel
import dev.mayutama.project.storyappsubm.config.DataStoreConfig

class SplashViewModel(
    private val dataStoreConfig: DataStoreConfig
): ViewModel() {
    fun getAuthInfo() = dataStoreConfig.getAuth()
}