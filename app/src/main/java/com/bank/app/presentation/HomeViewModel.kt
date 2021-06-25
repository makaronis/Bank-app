package com.bank.app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bank.app.data.entities.*
import com.bank.app.domain.usecases.ChangeCurrencyUseCase
import com.bank.app.domain.usecases.GetCurrencyRatesUseCase
import com.bank.app.domain.usecases.GetCardHolderInfoUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    val getCurrencyRates: GetCurrencyRatesUseCase,
    val getCardHolderInfo: GetCardHolderInfoUseCase,
    val changeCurrencyUseCase: ChangeCurrencyUseCase,
) : ViewModel() {

    private val _currencies = MutableStateFlow<Map<String, Currency>>(emptyMap())
    val currencies: StateFlow<Map<String, Currency>>
        get() = _currencies

    private var cardHolders = emptyList<CardholderInfo>()
    private val _selectedUserCard = MutableStateFlow<CardholderData?>(null)
    val selectedUserCard: StateFlow<CardholderData?>
        get() = _selectedUserCard

    private val _transactions = MutableStateFlow(listOf<Transaction>())
    val transactions: StateFlow<List<Transaction>>
        get() = _transactions

    private val eventChannel = Channel<UiEvent>()
    val events = eventChannel.consumeAsFlow()

    fun refreshCurrencies() {
        viewModelScope.launch {
            when (val result = getCurrencyRates()) {
                is UiState.Success -> _currencies.value = result.data
                is UiState.Error -> eventChannel.send(UiEvent.ShowSnackbar(msgId = result.msgId))
            }
        }
    }

    fun refreshCardHoldersInfo() {
        viewModelScope.launch {
            when (val result = getCardHolderInfo()) {
                is UiState.Success -> cardHolders = result.data
                is UiState.Error -> eventChannel.send(UiEvent.ShowSnackbar(msgId = result.msgId))
            }
        }
    }

    fun changeCurrency(newCurrencyCode: String) {

    }


}