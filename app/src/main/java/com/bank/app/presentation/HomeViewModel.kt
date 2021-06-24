package com.bank.app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bank.app.data.entities.CardholderInfo
import com.bank.app.data.entities.Currency
import com.bank.app.data.entities.CurrencyResponse
import com.bank.app.data.entities.UiState
import com.bank.app.domain.usecases.GetCurrencyRatesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    val getCurrencyRates: GetCurrencyRatesUseCase
) : ViewModel() {

    private val _currencies = MutableStateFlow<Map<String, Currency>>(emptyMap())
    val currencies: StateFlow<Map<String, Currency>>
        get() = _currencies

    private val selectedUserCard = MutableStateFlow(CardholderInfo)

    fun refreshCurrencies() {
        viewModelScope.launch {
            when (val result = getCurrencyRates()) {
                is UiState.Success -> _currencies.value = result.data
//                is UiState.Error ->
            }
        }
    }
}