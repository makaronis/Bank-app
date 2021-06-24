package com.bank.app.data.api

import com.bank.app.data.entities.CardholderInfo
import retrofit2.http.GET

interface TransactionsService {

    @GET("test/android/v1/users.json")
    suspend fun getTransactions(): List<CardholderInfo>
}