package dev.mayutama.project.storyappsubm.ui.storyAdd

import androidx.lifecycle.ViewModel
import dev.mayutama.project.storyappsubm.data.repository.StoryRepository
import java.io.File

class StoryAddViewModel(
    private val storyRepository: StoryRepository
): ViewModel() {
    fun addStory(photo: File, description: String, lat: Float? = null, lon: Float? = null) =
        storyRepository.addStory(photo, description, lat, lon)
}