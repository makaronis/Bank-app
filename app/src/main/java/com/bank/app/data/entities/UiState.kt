package com.bank.app.data.entities

import java.lang.Exception

sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val msg: Int, val exception: Exception? = null) : UiState<Nothing>()
}
