package dev.mayutama.project.storyappsubm

import android.app.Application
import timber.log.Timber

class StoryApp: Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}