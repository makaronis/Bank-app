package com.bank.app.presentation

import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bank.app.data.entities.UiState
import com.bank.app.presentation.adapters.TransactionAdapter
import com.bank.app.presentation.utils.ImageUtils
import com.bank.app.presentation.utils.observeOnLifecycle
import com.bank.bank_app.R
import com.bank.bank_app.databinding.FragmentHomeBinding
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val fragmentBinding by viewBinding(FragmentHomeBinding::bind)

    private val viewModel: HomeViewModel by activityViewModels()

    private val adapter = TransactionAdapter()

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
            rvTransactions.adapter = adapter

            cardGbp.setOnClickListener {
                onCurrencyCardClick(it as MaterialCardView, GBP_CODE)
            }
            cardEur.setOnClickListener {
                onCurrencyCardClick(it as MaterialCardView, EUR_CODE)
            }
            cardRub.setOnClickListener {
                onCurrencyCardClick(it as MaterialCardView, RUB_CODE)
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
                adapter.submitList(it)
            }
        }
    }

    private fun onCurrencyCardClick(card: MaterialCardView, currencyCode: String) {

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

    companion object {
        private const val GBP_CODE = "GBP"
        private const val EUR_CODE = "EUR"
        private const val RUB_CODE = "RUB"
    }
}