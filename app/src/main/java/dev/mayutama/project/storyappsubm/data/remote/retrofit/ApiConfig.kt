package dev.mayutama.project.storyappsubm.data.remote.retrofit

import dev.mayutama.project.storyappsubm.BuildConfig
import dev.mayutama.project.storyappsubm.data.remote.service.StoryService
import dev.mayutama.project.storyappsubm.util.TokenCache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    var BASE_URL = BuildConfig.BASE_URL
    fun getApiStoryService(isAuth: Boolean = false): StoryService {
         val loggingInterceptor =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            else HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)

        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val requestHeaders = req.newBuilder()
                .addHeader("Authorization", "Bearer ${TokenCache.token}")
                .build()
            chain.proceed(requestHeaders)
        }

        val okHttpClient = OkHttpClient.Builder()
        if (isAuth) {
            okHttpClient.addInterceptor(authInterceptor)
        }
        okHttpClient.addInterceptor(loggingInterceptor)


        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient.build())
            .build()

        return retrofit.create(StoryService::class.java)
    }
}