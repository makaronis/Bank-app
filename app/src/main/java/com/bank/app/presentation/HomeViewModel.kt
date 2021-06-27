package com.bank.app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bank.app.data.entities.*
import com.bank.app.domain.usecases.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    val getData: GetDataUseCase,
    val changeCurrency: ChangeCurrencyUseCase,
    val getCardHolders: GetCardHolderInfoUseCase,
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
    private val uiState: StateFlow<UiState>
        get() = _uiState

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
        currencies = newCurrencies
        handleNewUsersData(usersData)
    }

    private fun handleFailure(
        e: Throwable
    ) {
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
        val selectedCardNumber = _selectedUserCard.value?.cardNumber ?: return
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