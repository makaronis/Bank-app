package com.bank.app.domain.usecases

import com.bank.app.data.db.AppSharedPref
import javax.inject.Inject

class ChangeUserUseCase @Inject constructor(
    private val appSharedPref: AppSharedPref,
) {

    operator fun invoke(newUserCardNumber: String) {
        appSharedPref.setLastUserCard(newUserCardNumber)
    }
}