package cm.avisingh.legalease.utils

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val message: String? = null, val exception: Exception? = null) : Result<Nothing>()
}