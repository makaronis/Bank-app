package com.bank.app.data.entities

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class Error(val msgId: Int) : UiState()
}