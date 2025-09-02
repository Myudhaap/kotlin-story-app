package dev.mayutama.project.storyappsubm.ui.main.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dev.mayutama.project.storyappsubm.data.remote.dto.res.StoryRes
import dev.mayutama.project.storyappsubm.data.repository.StoryRepository
import dev.mayutama.project.storyappsubm.util.ResultState
import kotlinx.coroutines.launch

class StoryViewModel(
    private val storyRepository: StoryRepository
)
: ViewModel() {
    private val _stories = MutableLiveData<ResultState<StoryRes>>()
    val stories: LiveData<ResultState<StoryRes>> get() = _stories

    init {
        getStories()
    }

    fun getStories(page: Int? = null, size: Int? = null, location: Int = 0){
        viewModelScope.launch {
            storyRepository.getStories(page, size, location).asFlow()
                .collect {
                    _stories.postValue(it)
                }
        }
    }
}