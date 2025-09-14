package dev.mayutama.project.storyappsubm.data.repository

import androidx.lifecycle.liveData
import com.google.gson.Gson
import dev.mayutama.project.storyappsubm.data.remote.dto.req.LoginReq
import dev.mayutama.project.storyappsubm.data.remote.dto.req.RegisterReq
import dev.mayutama.project.storyappsubm.data.remote.dto.res.ErrorRes
import dev.mayutama.project.storyappsubm.data.remote.retrofit.ApiConfig
import dev.mayutama.project.storyappsubm.util.ResultState
import dev.mayutama.project.storyappsubm.util.wrapEspressoIdlingResource
import retrofit2.HttpException

class AuthRepository private constructor() {
    fun register(registerReq: RegisterReq) = liveData {
        emit(ResultState.Loading)

        try {
            val response = ApiConfig.getApiStoryService().register(registerReq)
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorRes::class.java)
            emit(ResultState.Error(errorResponse))
        }
    }

    fun login(loginReq: LoginReq) = liveData {
        emit(ResultState.Loading)

        wrapEspressoIdlingResource {
            try {
                val response = ApiConfig.getApiStoryService().login(loginReq)
                emit(ResultState.Success(response))
            }catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorRes::class.java)
                emit(ResultState.Error(errorResponse))
            }
        }
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null

        fun getInstance(): AuthRepository {
            return instance ?: synchronized(this) {
                instance ?: AuthRepository()
            }.also { instance = it }
        }
    }
}