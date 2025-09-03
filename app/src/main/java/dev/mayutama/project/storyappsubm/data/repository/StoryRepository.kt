package dev.mayutama.project.storyappsubm.data.repository

import androidx.lifecycle.liveData
import com.google.gson.Gson
import dev.mayutama.project.storyappsubm.data.remote.dto.res.ErrorRes
import dev.mayutama.project.storyappsubm.data.remote.retrofit.ApiConfig
import dev.mayutama.project.storyappsubm.util.ResultState
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor() {

    fun getStories(page: Int? = null, size: Int? = null, location: Int = 0) = liveData {
        emit(ResultState.Loading)

        try {
            val response = ApiConfig.getApiStoryService(true)
                .stories(page, size, location)
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorRes::class.java)
            emit(ResultState.Error(errorResponse))
        }
    }

    fun addStory(photo: File, description: String, lat: Float? = null, lon: Float? = null) = liveData {
        emit(ResultState.Loading)

        val descriptionBody = description.toRequestBody("text/plain".toMediaType())
        val latBody = lat?.toString()?.toRequestBody("text/plain".toMediaType())
        val lonBody = lon?.toString()?.toRequestBody("text/plain".toMediaType())
        val requestImageFile = photo.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            photo.name,
            requestImageFile
        )

        try {
            val successResponse = ApiConfig.getApiStoryService(true)
                .addStories(multipartBody, descriptionBody, latBody, lonBody)
            emit(ResultState.Success(successResponse))
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