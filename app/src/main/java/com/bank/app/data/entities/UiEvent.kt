package com.bank.app.data.entities

import com.google.android.material.snackbar.Snackbar

sealed class UiEvent {
    data class ShowSnackbar(val msgId: Int, val duration: Int = Snackbar.LENGTH_SHORT) : UiEvent()
}
