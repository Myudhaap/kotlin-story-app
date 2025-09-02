package dev.mayutama.project.storyappsubm.data.remote.service

import dev.mayutama.project.storyappsubm.data.remote.dto.req.LoginReq
import dev.mayutama.project.storyappsubm.data.remote.dto.req.RegisterReq
import dev.mayutama.project.storyappsubm.data.remote.dto.res.LoginRes
import dev.mayutama.project.storyappsubm.data.remote.dto.res.RegisterRes
import dev.mayutama.project.storyappsubm.data.remote.dto.res.StoryRes
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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
}