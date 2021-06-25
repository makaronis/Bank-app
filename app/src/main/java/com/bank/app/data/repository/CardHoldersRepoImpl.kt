package com.bank.app.data.repository

import com.bank.app.data.api.TransactionsService
import com.bank.app.data.entities.CardholderInfo
import javax.inject.Inject

class CardHoldersRepoImpl @Inject constructor(
    private val apiService: TransactionsService,
) {

    suspend fun getCardHoldersInfo(): List<CardholderInfo> {
        return apiService.getTransactions()
    }
}