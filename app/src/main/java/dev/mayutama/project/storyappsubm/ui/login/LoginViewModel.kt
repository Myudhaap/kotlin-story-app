package dev.mayutama.project.storyappsubm.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mayutama.project.storyappsubm.config.DataStoreConfig
import dev.mayutama.project.storyappsubm.data.remote.dto.req.LoginReq
import dev.mayutama.project.storyappsubm.data.remote.dto.res.LoginRes
import dev.mayutama.project.storyappsubm.data.repository.AuthRepository
import dev.mayutama.project.storyappsubm.util.ResultState
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val dataStoreConfig: DataStoreConfig
)
: ViewModel(){
    fun login(email: String, password: String): LiveData<ResultState<LoginRes>> {
        val loginReq = LoginReq(email, password)
        return authRepository.login(loginReq)
    }

    fun saveAuthInfo(loginRes: LoginRes) {
        viewModelScope.launch {
            dataStoreConfig.saveAuth(
                loginRes.loginResult.token,
                loginRes.loginResult.name,
                loginRes.loginResult.userId
            )
        }
    }
}