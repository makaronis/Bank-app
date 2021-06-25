package com.bank.app.data.entities

import java.lang.Exception

sealed class State<out T> {
//    object Idle : State<Nothing>()
//    object Loading : State<Nothing>()
    data class Success<T>(val data: T) : State<T>()
    data class Error(val msgId: Int, val exception: Exception? = null) : State<Nothing>()
}
