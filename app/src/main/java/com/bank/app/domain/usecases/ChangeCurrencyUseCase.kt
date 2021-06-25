package com.bank.app.domain.usecases

import com.bank.app.data.db.AppSharedPref
import com.bank.app.data.entities.CardholderData
import com.bank.app.data.entities.Currency
import com.bank.app.data.entities.Transaction
import com.bank.app.data.entities.TransactionData
import com.bank.app.domain.data.CurrencyProcessor
import javax.inject.Inject

class ChangeCurrencyUseCase @Inject constructor(
    private val sharedPref: AppSharedPref,
    private val currencyProcessor: CurrencyProcessor,
) {
    operator fun invoke(
        newCurrency: Currency,
        cardholderData: CardholderData,
        transactions: List<TransactionData>,
        currencies: Map<String, Currency>,
    ): Pair<CardholderData, List<TransactionData>> {
        sharedPref.setLastCurrencyCode(newCurrency.charCode)
        val cardHolder = cardholderData.copy(
            convertedSum = currencyProcessor.recountCurrency(
                recountKey = newCurrency.charCode,
                nominal = cardholderData.balance,
                currencies = currencies
            )
        )
        val transactions = transactions.map {
            it.copy(
                con
            )
        }
    }
}