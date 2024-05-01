package com.test.planetapp.network

data class Resource<out T>(val status: Status, val data: T?, val message: String?){

    companion object{
        fun <T> success(data: T?): Resource<T>{
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> fail(message: String?): Resource<T>{
            return Resource(Status.FAIL, null, message)
        }
    }
}
