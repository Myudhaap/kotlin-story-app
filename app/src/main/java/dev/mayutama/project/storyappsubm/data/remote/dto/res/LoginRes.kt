package dev.mayutama.project.storyappsubm.data.remote.dto.res

data class LoginRes(
    val error: Boolean,
    val message: String,
    val loginResult: Login
) {
    data class Login(
        val userId: String,
        val name: String,
        val token: String
    )
}