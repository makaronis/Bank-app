package com.bank.app.data.repository

import com.bank.app.data.api.TransactionsService
import com.bank.app.data.entities.CardholderInfo
import com.bank.app.domain.repository.CardHoldersRepo
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import javax.inject.Inject

class CardHoldersRepoImpl @Inject constructor(
    private val apiService: TransactionsService,
) : CardHoldersRepo {

    override suspend fun getCardHoldersInfo(): List<CardholderInfo> {
        val json = apiService.getTransactions()
        return Json.decodeFromJsonElement(json["users"] ?: throw Exception("json file error"))
    }
}