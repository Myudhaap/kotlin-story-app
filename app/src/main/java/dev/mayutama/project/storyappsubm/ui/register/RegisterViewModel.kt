package dev.mayutama.project.storyappsubm.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.mayutama.project.storyappsubm.data.remote.dto.req.RegisterReq
import dev.mayutama.project.storyappsubm.data.remote.dto.res.RegisterRes
import dev.mayutama.project.storyappsubm.data.repository.AuthRepository
import dev.mayutama.project.storyappsubm.util.ResultState

class RegisterViewModel(
    private val authRepository: AuthRepository
)
: ViewModel(){

    fun register(name: String, email: String, password: String): LiveData<ResultState<RegisterRes>> {
        val registerReq = RegisterReq(name, email, password)
        return authRepository.register(registerReq)
    }
}