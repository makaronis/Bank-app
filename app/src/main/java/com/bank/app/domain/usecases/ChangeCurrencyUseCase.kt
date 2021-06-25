package com.bank.app.domain.usecases

import com.bank.app.data.db.AppSharedPref
import com.bank.app.data.entities.CardholderData
import com.bank.app.data.entities.CardholderInfo
import com.bank.app.data.entities.Currency
import com.bank.app.data.entities.TransactionData
import javax.inject.Inject

class ChangeCurrencyUseCase @Inject constructor(
    private val convertUserCurrency: ConvertUserCurrencyUseCase,
    private val sharedPref: AppSharedPref,
) {

    operator fun invoke(
        newCurrencyCode: String,
        cardholderInfo: List<CardholderInfo>,
        currencies: Map<String, Currency>,
    ): Map<CardholderData, List<TransactionData>> {
        sharedPref.setLastCurrencyCode(newCurrencyCode)
        //TODO refactor convertUserCurrency to data objects
        return convertUserCurrency(cardholderInfo, currencies)
    }
}