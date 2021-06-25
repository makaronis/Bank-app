package com.bank.app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bank.app.data.entities.*
import com.bank.app.domain.usecases.ConvertUserCurrencyUseCase
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
    val convertUserCurrencyUseCase: ConvertUserCurrencyUseCase,
) : ViewModel() {

    private val _currencies = MutableStateFlow<Map<String, Currency>>(emptyMap())
    val currencies: StateFlow<Map<String, Currency>>
        get() = _currencies

    private var cardHolders = emptyList<CardholderInfo>()
    private val _selectedUserCard = MutableStateFlow<CardholderData?>(null)
    val selectedUserCard: StateFlow<CardholderData?>
        get() = _selectedUserCard

    private val _transactions = MutableStateFlow(listOf<TransactionData>())
    val transactions: StateFlow<List<TransactionData>>
        get() = _transactions

    private val eventChannel = Channel<UiEvent>()
    val events = eventChannel.consumeAsFlow()

    fun refreshData(){
        viewModelScope.launch {
            val currencies = getCurrencyRates()
        }
    }

    fun refreshCurrencies() {
        viewModelScope.launch {
            when (val result = getCurrencyRates()) {
                is State.Success -> _currencies.value = result.data
                is State.Error -> eventChannel.send(UiEvent.ShowSnackbar(msgId = result.msgId))
            }
        }
    }

    fun refreshCardHoldersInfo() {
        viewModelScope.launch {
            when (val result = getCardHolderInfo()) {
                is State.Success -> cardHolders = result.data
                is State.Error -> eventChannel.send(UiEvent.ShowSnackbar(msgId = result.msgId))
            }
        }
    }

    fun changeCurrency(newCurrency: Currency) {
        val currentCardHolder = _selectedUserCard.value ?: return
        val (newCardData,newTransactions) = convertUserCurrencyUseCase(newCurrency,currentCardHolder,_transactions.value)
    }


}