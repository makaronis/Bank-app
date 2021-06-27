package com.bank.app.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bank.app.data.db.AppSharedPref
import com.bank.app.data.entities.*
import com.bank.app.domain.usecases.*
import com.bank.app.presentation.utils.AppConfig
import com.bank.bank_app.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getData: GetDataUseCase,
    private val changeCurrency: ChangeCurrencyUseCase,
    private val changeUser: ChangeUserUseCase,
    private val appSharedPref: AppSharedPref,
) : ViewModel() {

    private var currencies = emptyMap<String, Currency>()
    private var allUsersData = emptyMap<String, Map.Entry<CardholderData, List<TransactionData>>>()

    private val _selectedUserCard = MutableStateFlow<CardholderData?>(null)
    val selectedUserCard: StateFlow<CardholderData?>
        get() = _selectedUserCard

    private val _userTransactions = MutableStateFlow(listOf<TransactionData>())
    val userTransactions: StateFlow<List<TransactionData>>
        get() = _userTransactions

    private val _uiEvents = MutableSharedFlow<UiEvent>()
    val uiEvents: SharedFlow<UiEvent>
        get() = _uiEvents

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState>
        get() = _uiState

    private val _userCards = MutableStateFlow<List<CardholderData>>(emptyList())
    val userCards: StateFlow<List<CardholderData>>
        get() = _userCards

    private val _operatedCurrencies = MutableStateFlow(
        listOf(
            CurrencyItem(
                currencyCode = AppConfig.GBP_CURRENCY_CODE,
                currencySymbol = "£",
            ),
            CurrencyItem(
                currencyCode = AppConfig.EUR_CURRENCY_CODE,
                currencySymbol = "€",
            ),
            CurrencyItem(
                currencyCode = AppConfig.RUB_CURRENCY_CODE,
                currencySymbol = "₽"
            ),
        )
    )
    val operatedCurrencies: StateFlow<List<CurrencyItem>>
        get() = _operatedCurrencies

    var updateOnAvailable = false

    init {
        setLastSelectedCurrency()
        refreshData()
    }

    fun updateOnAvailable() {
        if (updateOnAvailable) {
            updateOnAvailable = false
            refreshData()
        }
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
        when (e) {
            is UnknownHostException -> _uiState.value = UiState.Error(R.string.error_no_internet)
            else -> _uiState.value = UiState.Error(R.string.error_unknown)
        }
        Log.d("HomeViewModel", "onFailure")
        Log.e("HomeViewModel", e.message, e)
    }


    fun setNewCurrency(newCurrencyCode: String) {
        viewModelScope.launch {
            val newUsersData = changeCurrency(newCurrencyCode, allUsersData, currencies)
            handleNewUsersData(newUsersData)
            updateOperatedCurrencies(newCurrencyCode)
        }
    }

    fun changeUserCard(cardNumber: String) {
        viewModelScope.launch {
            changeUser(cardNumber)
            updateSelectedUser(cardNumber)
            _uiEvents.emit(UiEvent.NavigateBack)
        }
    }

    private fun updateSelectedUser(cardNumber: String) {
        _selectedUserCard.value = allUsersData[cardNumber]?.key
        _userTransactions.value = allUsersData[cardNumber]?.value ?: emptyList()
    }

    private fun handleNewUsersData(usersData: Map<CardholderData, List<TransactionData>>) {
        allUsersData = usersData.mapWithCardNumber()
        _userCards.value = usersData.map { it.key }
        var selectedCardNumber = _selectedUserCard.value?.cardNumber
        if (selectedCardNumber == null) {
            selectedCardNumber = allUsersData.keys.firstOrNull()
        }
        val selectedUserData = allUsersData[selectedCardNumber]
        _selectedUserCard.value = selectedUserData?.key
        _userTransactions.value = selectedUserData?.value ?: emptyList()
    }

    private fun setLastSelectedCurrency() {
        updateOperatedCurrencies(appSharedPref.getLastCurrencyCode())
    }

    private fun updateOperatedCurrencies(newCurrencyCode: String) {
        val selectedCurrency = _operatedCurrencies.value.map {
            it.copy(
                isSelected = it.currencyCode == newCurrencyCode
            )
        }
        _operatedCurrencies.value = selectedCurrency
    }

    private fun Map<CardholderData, List<TransactionData>>.mapWithCardNumber() =
        this.entries.associateBy { data -> data.key.cardNumber }

    companion object {
        const val NULL_CARD = "0000 0000 0000 0000"
    }
}