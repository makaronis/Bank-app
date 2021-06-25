package com.bank.app.data.db

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.bank.app.utils.AppConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSharedPref @Inject constructor(@ApplicationContext context: Context) {

    private val sf = context.applicationContext.getSharedPreferences(prefsTag, Context.MODE_PRIVATE)

    fun setLastCurrencyCode(currencyCode: String) {
        sf.edit {
            putString(SELECTED_CURRENCY_KEY, currencyCode)
        }
    }

    fun getLastCurrencyCode(): String {
        return sf.getString(SELECTED_CURRENCY_KEY, AppConfig.DEFAULT_CURRENCY)
            ?: AppConfig.DEFAULT_CURRENCY
    }

    fun setLastUserCard(card: String) {
        sf.edit {
            putString(SELECTED_USER_CARD, card)
        }
    }

    fun getLastUserCard(): String? {
        return sf.getString(SELECTED_USER_CARD, null)
    }

    companion object {
        const val prefsTag = "prefs"
        const val SELECTED_USER_CARD = "userCard"
        const val SELECTED_CURRENCY_KEY = "currency"
    }
}


