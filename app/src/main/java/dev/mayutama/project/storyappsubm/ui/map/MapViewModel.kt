package dev.mayutama.project.storyappsubm.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dev.mayutama.project.storyappsubm.data.remote.dto.res.StoryRes
import dev.mayutama.project.storyappsubm.data.repository.StoryRepository
import dev.mayutama.project.storyappsubm.util.ResultState
import kotlinx.coroutines.launch

class MapViewModel(
    private val storyRepository: StoryRepository
)
:  ViewModel() {
    private val _stories = MutableLiveData<ResultState<StoryRes>>()
    val stories: LiveData<ResultState<StoryRes>> get() = _stories

    init {
        getStories()
    }

    fun getStories(){
        viewModelScope.launch {
            storyRepository.getStories(location = 1).asFlow()
                .collect {
                    _stories.postValue(it)
                }
        }
    }
}