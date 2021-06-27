package com.bank.app.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bank.app.data.entities.TransactionData
import com.bank.bank_app.databinding.ItemTransactionBinding

class TransactionAdapter : ListAdapter<TransactionData, TransactionViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TransactionViewHolder.newInstance(parent)

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<TransactionData>() {
            override fun areItemsTheSame(
                oldItem: TransactionData,
                newItem: TransactionData
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: TransactionData,
                newItem: TransactionData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class TransactionViewHolder(private val binding: ItemTransactionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: TransactionData) {
        binding.apply {
            tvName.text = item.title
            tvDate.text = item.date
            tvUsdPrice.text = item.amount.toString()
            tvConvertedPrice.text = item.convertedAmount.toString()
            ivIcon.load(item.iconUrl)
        }
    }


    companion object {
        fun newInstance(container: ViewGroup): TransactionViewHolder {
            val binding = ItemTransactionBinding.inflate(
                LayoutInflater.from(container.context),
                container,
                false
            )
            return TransactionViewHolder(binding)
        }
    }
}