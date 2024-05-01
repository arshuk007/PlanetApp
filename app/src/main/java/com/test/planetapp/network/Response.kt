package com.test.planetapp.network

sealed class Response<out R> {

    data class Success<out T>(val data: T) : Response<T>()

    data class Failure<out T>(val message: String): Response<T>()

    override fun toString(): String {
        return when(this){
            is Success<*> -> "Success[data=$data]"
            is Failure<*> -> "Success[data=$message]"
        }
    }
}
val Response<*>.succeeded
    get() = this is Response.Success && data != null