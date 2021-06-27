package com.bank.app.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bank.app.data.entities.*
import com.bank.app.domain.usecases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getData: GetDataUseCase,
    private val changeCurrency: ChangeCurrencyUseCase,
    private val getCardHolders: GetCardHolderInfoUseCase,
) : ViewModel() {


    private var currencies = emptyMap<String, Currency>()
    private var allUsersData = emptyMap<String, Map.Entry<CardholderData, List<TransactionData>>>()

    private val _selectedUserCard = MutableStateFlow<CardholderData?>(null)
    val selectedUserCard: StateFlow<CardholderData?>
        get() = _selectedUserCard

    private val _userTransactions = MutableStateFlow(listOf<TransactionData>())
    val userTransactions: StateFlow<List<TransactionData>>
        get() = _userTransactions

    private val eventChannel = Channel<UiEvent>()
    val events = eventChannel.consumeAsFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState>
        get() = _uiState

    init {
        refreshData()
    }

    fun refreshData() = viewModelScope.launch {
        _uiState.value = UiState.Loading
        val dataResult = getData()

        dataResult.fold(
            onSuccess = {
                handleSuccessData(it.first, it.second)
            },
            onFailure = {
                handleFailure(it)
            }
        )
    }


    private fun handleSuccessData(
        usersData: Map<CardholderData, List<TransactionData>>,
        newCurrencies: Map<String, Currency>
    ) {
        Log.d("HomeViewModel", "onSuccess")
        currencies = newCurrencies
        handleNewUsersData(usersData)
        _uiState.value = UiState.Idle
    }

    private fun handleFailure(
        e: Throwable
    ) {
        Log.d("HomeViewModel", "onFailure")
        Log.e("HomeViewModel", e.message, e)
        _uiState.value = UiState.Idle
    }


    fun setNewCurrency(newCurrencyCode: String) {
        viewModelScope.launch {
            val newUsersData = changeCurrency(newCurrencyCode, allUsersData, currencies)
            handleNewUsersData(newUsersData)
        }
    }

    private fun handleNewUsersData(usersData: Map<CardholderData, List<TransactionData>>) {
        allUsersData = usersData.mapWithCardNumber()
        var selectedCardNumber = _selectedUserCard.value?.cardNumber
        if (selectedCardNumber == null) {
            selectedCardNumber = allUsersData.keys.firstOrNull()
        }
        val selectedUserData = allUsersData[selectedCardNumber]
        _selectedUserCard.value = selectedUserData?.key
        _userTransactions.value = selectedUserData?.value ?: emptyList()
    }

    private fun Map<CardholderData, List<TransactionData>>.mapWithCardNumber() =
        this.entries.associateBy { data -> data.key.cardNumber }

    companion object {
        const val NULL_CARD = "0000 0000 0000 0000"
    }
}