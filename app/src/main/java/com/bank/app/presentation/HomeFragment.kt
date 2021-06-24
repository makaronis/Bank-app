package com.bank.app.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bank.bank_app.R
import com.bank.bank_app.databinding.FragmentHomeBinding
import com.facebook.shimmer.Shimmer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val fragmentBinding by viewBinding(FragmentHomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            fragmentBinding.apply {
                userCard.visibility = View.INVISIBLE
                cvTransactions.visibility = View.INVISIBLE
                cardPLaceholder.startShimmer()
                transactionsPlaceholder.startShimmer()
                delay(5_000)
                cardPLaceholder.stopShimmer()
                transactionsPlaceholder.stopShimmer()
                cardPLaceholder.visibility = View.GONE
                transactionsPlaceholder.visibility = View.GONE
                userCard.visibility = View.VISIBLE
                cvTransactions.visibility = View.VISIBLE
            }

        }
    }
}