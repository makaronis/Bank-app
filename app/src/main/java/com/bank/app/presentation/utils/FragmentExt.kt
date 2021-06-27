package com.bank.app.presentation.utils

import android.view.View
import androidx.fragment.app.Fragment
import com.bank.app.data.entities.UiEvent
import com.google.android.material.snackbar.Snackbar

fun Fragment.showSnackbar(view: View, msgId: Int, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(view, msgId, duration).show()
}