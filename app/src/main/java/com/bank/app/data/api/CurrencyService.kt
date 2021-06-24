package com.bank.app.data.api

import com.bank.app.data.entities.CurrencyResponse
import retrofit2.http.GET

interface CurrencyService {

    @GET("daily_json.js")
    suspend fun getCurrency(): CurrencyResponse

}