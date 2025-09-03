package dev.mayutama.project.storyappsubm.data.remote.service

import dev.mayutama.project.storyappsubm.data.remote.dto.req.LoginReq
import dev.mayutama.project.storyappsubm.data.remote.dto.req.RegisterReq
import dev.mayutama.project.storyappsubm.data.remote.dto.res.LoginRes
import dev.mayutama.project.storyappsubm.data.remote.dto.res.RegisterRes
import dev.mayutama.project.storyappsubm.data.remote.dto.res.StoryAddRes
import dev.mayutama.project.storyappsubm.data.remote.dto.res.StoryRes
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface StoryService {
    @POST("/v1/register")
    suspend fun register(
        @Body registerReq: RegisterReq
    ): RegisterRes

    @POST("/v1/login")
    suspend fun login(
        @Body loginReq: LoginReq
    ): LoginRes

    @GET("/v1/stories")
    suspend fun stories(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int = 0
    ): StoryRes

    @Multipart
    @POST("/v1/stories")
    suspend fun addStories(
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null
    ): StoryAddRes
}