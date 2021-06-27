package com.bank.app.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bank.app.data.entities.CurrencyItem
import com.bank.bank_app.R
import com.bank.bank_app.databinding.ItemCurrencyBinding
import java.util.*

class CurrencyAdapter(private val onClick: (String) -> Unit) :
    ListAdapter<CurrencyItem, CurrencyViewHolder>(diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CurrencyViewHolder.newInstance(parent, onClick)

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    companion object {
        private val diff = object : DiffUtil.ItemCallback<CurrencyItem>() {
            override fun areItemsTheSame(oldItem: CurrencyItem, newItem: CurrencyItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: CurrencyItem, newItem: CurrencyItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class CurrencyViewHolder(
    private val binding: ItemCurrencyBinding,
    private val onClick: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: CurrencyItem) {
        val context = binding.root.context
        binding.apply {
            val backgroundColorId = if (item.isSelected) R.color.primary else R.color.white
            val elementsColorId = if (item.isSelected) R.color.white else R.color.secondaryText
            val elementsColor = ContextCompat.getColor(context, elementsColorId)
            card.setCardBackgroundColor(ContextCompat.getColor(context, backgroundColorId))
            tvSymbol.text = item.currencySymbol
            tvSymbol.setTextColor(elementsColor)
            tvText.text = item.currencyCode
            tvText.setTextColor(elementsColor)
            card.setOnClickListener { onClick.invoke(item.currencyCode) }
        }
    }

    companion object {
        fun newInstance(parent: ViewGroup, onClick: (String) -> Unit): CurrencyViewHolder {
            val binding =
                ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CurrencyViewHolder(binding, onClick)
        }
    }
}