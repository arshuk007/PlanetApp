package com.test.planetapp.network

open class ResponseHandler {

    fun <T: Any> handleSuccess(data: T?): Resource<T> {
        return Resource.success(data)
    }

    fun <T: Any> handleFail(message: String): Resource<T> {
        return Resource.fail(message)
    }
}