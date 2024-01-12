package com.oechslerbernardo.mongodbteste.util

sealed class Resource<out T>(val message: String? = null) {
    class Success<T>(val data: T) : Resource<T>()
    class Error<T>(message: String, data: T? = null) : Resource<T>(message)
    class Loading<T>(val data: T? = null) : Resource<T>()
}