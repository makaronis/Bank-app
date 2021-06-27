package com.bank.app.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bank.app.data.entities.CurrencyItem
import com.bank.app.data.entities.UiState
import com.bank.app.presentation.adapters.CurrencyAdapter
import com.bank.app.presentation.adapters.TransactionAdapter
import com.bank.app.presentation.utils.ImageUtils
import com.bank.app.presentation.utils.observeOnLifecycle
import com.bank.bank_app.R
import com.bank.bank_app.databinding.FragmentHomeBinding
import com.google.android.material.card.MaterialCardView

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val fragmentBinding by viewBinding(FragmentHomeBinding::bind)

    private val viewModel: HomeViewModel by activityViewModels()

    private val transAdapter = TransactionAdapter()

    private var currencyAdapter: CurrencyAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

            userCard.setOnClickListener {
                navigateToCards()
            }
        }
    }

    private fun subscribeObservers() {
        viewModel.apply {
            uiState.observeOnLifecycle(viewLifecycleOwner) {
                when (it) {
                    UiState.Loading -> startShimmer()
                    UiState.Idle -> stopShimmer()
                    is UiState.Error -> handleError()
                }
            }

            selectedUserCard.observeOnLifecycle(viewLifecycleOwner) {
                if (it == null) return@observeOnLifecycle
                fragmentBinding.apply {
                    tvCardNumber.text = it.cardNumber
                    tvConvertedBalance.text = it.convertedBalance.toString()
                    tvBalance.text = it.balance.toString()
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

    private fun handleError() {

    }

    private fun startShimmer() = fragmentBinding.apply {
        cardPlaceholder.visibility = View.VISIBLE
        transactionsPlaceholder.visibility = View.VISIBLE
        userCard.visibility = View.INVISIBLE
        cvTransactions.visibility = View.INVISIBLE
        cardPlaceholder.startShimmer()
        transactionsPlaceholder.startShimmer()
    }

    private fun stopShimmer() = fragmentBinding.apply {
        cardPlaceholder.stopShimmer()
        transactionsPlaceholder.stopShimmer()
        cardPlaceholder.visibility = View.GONE
        transactionsPlaceholder.visibility = View.GONE
        userCard.visibility = View.VISIBLE
        cvTransactions.visibility = View.VISIBLE
    }

    private fun navigateToCards() {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCardsFragment())
    }

    companion object {
        private const val GBP_CODE = "GBP"
        private const val EUR_CODE = "EUR"
        private const val RUB_CODE = "RUB"
    }
}