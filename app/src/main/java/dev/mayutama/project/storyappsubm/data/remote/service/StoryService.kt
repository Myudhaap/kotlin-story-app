package dev.mayutama.project.storyappsubm.data.remote.service

import dev.mayutama.project.storyappsubm.data.remote.dto.req.LoginReq
import dev.mayutama.project.storyappsubm.data.remote.dto.req.RegisterReq
import dev.mayutama.project.storyappsubm.data.remote.dto.res.LoginRes
import dev.mayutama.project.storyappsubm.data.remote.dto.res.RegisterRes
import retrofit2.http.Body
import retrofit2.http.POST

interface StoryService {
    @POST("/v1/register")
    suspend fun register(
        @Body registerReq: RegisterReq
    ): RegisterRes

    @POST("/v1/login")
    suspend fun login(
        @Body loginReq: LoginReq
    ): LoginRes
}