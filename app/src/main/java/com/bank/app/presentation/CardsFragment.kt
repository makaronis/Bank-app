package com.bank.app.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bank.app.data.entities.UiEvent
import com.bank.app.presentation.adapters.CardAdapter
import com.bank.app.presentation.utils.observeOnLifecycle
import com.bank.app.presentation.utils.showSnackbar
import com.bank.bank_app.R
import com.bank.bank_app.databinding.FragmentCardsBinding

class CardsFragment : Fragment(R.layout.fragment_cards) {

    private val binding by viewBinding(FragmentCardsBinding::bind)

    private val viewModel: HomeViewModel by activityViewModels()

    private var adapter: CardAdapter? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToolbar()
        subscribeUi()
        subscribeObservers()
    }

    private fun subscribeUi() {
        adapter =
            CardAdapter(viewModel::changeUserCard, viewModel.selectedUserCard.value?.cardNumber)
        binding.rvItems.adapter = adapter
    }

    private fun subscribeObservers() {
        viewModel.apply {
            uiEvents.observeOnLifecycle(viewLifecycleOwner) {
                when (it) {
                    UiEvent.NavigateBack -> navigateBack()
                    is UiEvent.ShowSnackbar -> showSnackbar(binding.root, it.msgId, it.duration)
                }
            }
            userCards.observeOnLifecycle(viewLifecycleOwner) {
                adapter?.submitList(it)
            }
        }
    }

    private fun subscribeToolbar() {
        binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener { requireActivity().onBackPressed() }
        }
    }

    private fun navigateBack() {
        requireActivity().onBackPressed()
    }

}