package com.bank.app.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bank.app.data.entities.CurrencyItem
import com.bank.app.data.entities.UiState
import com.bank.app.presentation.adapters.CurrencyAdapter
import com.bank.app.presentation.adapters.TransactionAdapter
import com.bank.app.presentation.utils.AppConfig
import com.bank.app.presentation.utils.ImageUtils
import com.bank.app.presentation.utils.Utils
import com.bank.app.presentation.utils.observeOnLifecycle
import com.bank.bank_app.R
import com.bank.bank_app.databinding.FragmentHomeBinding
import com.google.android.material.card.MaterialCardView

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val fragmentBinding by viewBinding(FragmentHomeBinding::bind)

    private val viewModel: HomeViewModel by activityViewModels()

    private val transAdapter = TransactionAdapter()

    private var currencyAdapter: CurrencyAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUi()
        subscribeObservers()
    }

    private fun subscribeUi() {
        fragmentBinding.apply {
            rvTransactions.adapter = transAdapter
            rvTransactions.itemAnimator = null

            currencyAdapter = CurrencyAdapter(viewModel::setNewCurrency)
            rvCurrency.adapter = currencyAdapter
            rvCurrency.itemAnimator = null

            swipeToRefresh.setOnRefreshListener {
                viewModel.refreshData()
            }

            userCard.setOnClickListener {
                navigateToCards()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun subscribeObservers() {
        viewModel.apply {
            uiState.observeOnLifecycle(viewLifecycleOwner) {
                when (it) {
                    UiState.Loading -> startShimmer()
                    UiState.Idle -> {
                        fragmentBinding.swipeToRefresh.isRefreshing = false
                        stopShimmer()
                    }
                    is UiState.Error -> handleError(it.msgId)
                }
            }

            selectedUserCard.observeOnLifecycle(viewLifecycleOwner) {
                if (it == null) return@observeOnLifecycle
                fragmentBinding.apply {
                    tvCardNumber.text = it.cardNumber
                    tvConvertedBalance.text =
                        "${Utils.getSymbolByCode(it.convertedCode)}${it.convertedBalance}"
                    tvBalance.text =
                        "${Utils.getSymbolByCode(AppConfig.BASE_CURRENCY)}${it.balance}"
                    tvUserName.text = it.cardholderName
                    tvValidDate.text = it.valid
                    ivCardType.setImageDrawable(
                        ImageUtils.getImageByCardType(
                            it.type,
                            requireContext()
                        )
                    )

                }
            }

            userTransactions.observeOnLifecycle(viewLifecycleOwner) {
                transAdapter.submitList(it)
            }

            operatedCurrencies.observeOnLifecycle(viewLifecycleOwner) {
                currencyAdapter?.submitList(it)
            }
        }
    }

    private fun handleError(msgId: Int) {
        fragmentBinding.apply {
            ltContent.visibility = View.GONE
            tvError.visibility = View.VISIBLE
            tvError.text = getString(msgId)
        }
    }

    private fun hideError() {
        fragmentBinding.apply {
            ltContent.visibility = View.VISIBLE
            tvError.visibility = View.GONE
        }
    }

    private fun startShimmer() = fragmentBinding.apply {
        hideError()
        cardPlaceholder.visibility = View.VISIBLE
        transactionsPlaceholder.visibility = View.VISIBLE
        userCard.visibility = View.INVISIBLE
        cvTransactions.visibility = View.INVISIBLE
        rvCurrency.visibility = View.INVISIBLE
        cardPlaceholder.startShimmer()
        transactionsPlaceholder.startShimmer()
        currenciesPlaceholder.startShimmer()
    }

    private fun stopShimmer() = fragmentBinding.apply {
        cardPlaceholder.stopShimmer()
        transactionsPlaceholder.stopShimmer()
        currenciesPlaceholder.stopShimmer()
        cardPlaceholder.visibility = View.GONE
        transactionsPlaceholder.visibility = View.GONE
        currenciesPlaceholder.visibility = View.GONE
        userCard.visibility = View.VISIBLE
        cvTransactions.visibility = View.VISIBLE
        rvCurrency.visibility = View.VISIBLE
    }

    private fun navigateToCards() {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCardsFragment())
    }
}