package com.bank.app.domain.usecases

import com.bank.app.data.db.AppSharedPref
import javax.inject.Inject

class ChangeCurrencyUseCase @Inject constructor(
    val sharedPref: AppSharedPref,
) {
    operator fun invoke(newCurrency: String) {

    }
}