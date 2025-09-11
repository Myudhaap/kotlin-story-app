package dev.mayutama.project.storyappsubm.ui.main.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dev.mayutama.project.storyappsubm.data.local.entity.StoryEntity
import dev.mayutama.project.storyappsubm.data.repository.StoryRepository

class StoryViewModel(
    private val storyRepository: StoryRepository
)
: ViewModel() {
    fun getStories(location: Int = 0): LiveData<PagingData<StoryEntity>> =
        storyRepository.getStoriesPaging(location).cachedIn(viewModelScope)
}