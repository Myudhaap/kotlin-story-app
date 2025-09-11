package dev.mayutama.project.storyappsubm.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import dev.mayutama.project.storyappsubm.data.local.database.AppDatabase
import dev.mayutama.project.storyappsubm.data.local.entity.StoryEntity
import dev.mayutama.project.storyappsubm.data.mediator.StoryRemoteMediator
import dev.mayutama.project.storyappsubm.data.remote.dto.res.ErrorRes
import dev.mayutama.project.storyappsubm.data.remote.retrofit.ApiConfig
import dev.mayutama.project.storyappsubm.util.ResultState
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository constructor(
    private val database: AppDatabase
) {

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

    fun getStoriesPaging(location: Int = 0): LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(database, location),
            pagingSourceFactory = {
                database.storyDao().getAllQuote()
            }
        ).liveData
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

        fun getInstance(database: AppDatabase): StoryRepository {
            return instance ?: synchronized(this) {
                instance ?: StoryRepository(database)
            }.also { instance = it }
        }
    }
}