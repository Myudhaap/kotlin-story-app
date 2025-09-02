package dev.mayutama.project.storyappsubm.util

sealed class ResultState<out R> private constructor(){
    data class Success<out T>(val data: T): ResultState<T>()
    data class Error<T>(val error: T): ResultState<Nothing>()
    data object Loading: ResultState<Nothing>()
}