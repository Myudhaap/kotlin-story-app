package dev.mayutama.project.storyappsubm.ui.main.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mayutama.project.storyappsubm.config.DataStoreConfig
import dev.mayutama.project.storyappsubm.util.TokenCache
import kotlinx.coroutines.launch

class SettingViewModel(
    private val dataStoreConfig: DataStoreConfig
): ViewModel() {
    fun logout() {
        viewModelScope.launch {
            dataStoreConfig.clear()
            TokenCache.token = null
        }
    }
}