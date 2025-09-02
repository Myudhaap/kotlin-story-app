package dev.mayutama.project.storyappsubm.data.repository

import androidx.lifecycle.liveData
import com.google.gson.Gson
import dev.mayutama.project.storyappsubm.data.remote.dto.res.ErrorRes
import dev.mayutama.project.storyappsubm.data.remote.retrofit.ApiConfig
import dev.mayutama.project.storyappsubm.util.ResultState
import retrofit2.HttpException

class StoryRepository private constructor() {

    fun getStories(page: Int? = null, size: Int? = null, location: Int = 0) = liveData {
        emit(ResultState.Loading)

        try {
            val response = ApiConfig.getApiStoryService(true)
                .stories()
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorRes::class.java)
            emit(ResultState.Error(errorResponse))
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(): StoryRepository {
            return instance ?: synchronized(this) {
                instance ?: StoryRepository()
            }.also { instance = it }
        }
    }
}