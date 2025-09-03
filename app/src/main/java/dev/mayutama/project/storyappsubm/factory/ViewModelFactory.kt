package dev.mayutama.project.storyappsubm.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.mayutama.project.storyappsubm.di.Injection
import dev.mayutama.project.storyappsubm.ui.login.LoginViewModel
import dev.mayutama.project.storyappsubm.ui.main.setting.SettingViewModel
import dev.mayutama.project.storyappsubm.ui.main.story.StoryViewModel
import dev.mayutama.project.storyappsubm.ui.register.RegisterViewModel
import dev.mayutama.project.storyappsubm.ui.splash.SplashViewModel
import dev.mayutama.project.storyappsubm.ui.storyAdd.StoryAddViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(
    private val application: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(Injection.provideDataStore(application)) as T
        }

        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(Injection.provideAuthRepository()) as T
        }

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                Injection.provideAuthRepository(),
                Injection.provideDataStore(application)
                ) as T
        }

        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            return StoryViewModel(
                Injection.provideStoryRepository()
            ) as T
        }

        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(Injection.provideDataStore(application)) as T
        }

        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(Injection.provideDataStore(application)) as T
        }

        if (modelClass.isAssignableFrom(StoryAddViewModel::class.java)) {
            return StoryAddViewModel(
                Injection.provideStoryRepository()
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(application: Application): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    application
                )
            }.also { instance = it }
        }
    }
}