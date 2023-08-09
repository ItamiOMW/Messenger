package com.example.itami_chat.core.domain.model

import java.lang.Exception


sealed class AppResponse<out T> {

    //Success state
    data class Success<T>(val data: T) : AppResponse<T>()

    //Failed state
    data class Failed<T>(val exception: Exception, val message: String?) : AppResponse<T>()


    companion object {

        //GET SUCCESS STATE
        fun <T> success(data: T) = Success(data)

        //GET FAILED STATE
        fun <T> failed(exception: Exception, message: String? = null) = Failed<T>(exception, message)

    }

}
