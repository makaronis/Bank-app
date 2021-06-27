package com.bank.app.presentation.adapters

import android.annotation.SuppressLint
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bank.app.data.entities.TransactionData
import com.bank.app.presentation.utils.AppConfig
import com.bank.app.presentation.utils.Utils
import com.bank.bank_app.R
import com.bank.bank_app.databinding.ItemTransactionBinding
import kotlin.math.absoluteValue

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

    @SuppressLint("SetTextI18n")
    fun onBind(item: TransactionData) {
        binding.apply {
            tvName.text = item.title
            tvDate.text = item.date
            tvUsdPrice.text = getBalanceString(item.amount, AppConfig.BASE_CURRENCY)
            tvConvertedPrice.setText(
                getSpannableString(item.convertedAmount, item.convertedCode),
                TextView.BufferType.SPANNABLE
            )
            ivIcon.load(item.iconUrl)
        }
    }

    private fun getSpannableString(value: Double, code: String): SpannableString {
        val context = binding.root.context
        val isNegative = value < 0.0

        val currencySymbol = Utils.getSymbolByCode(code)
        val prefix = if (isNegative) "- " else ""
        val coloredString = "$prefix$currencySymbol "
        val finalString = "$coloredString${value.absoluteValue}"
        val spannable = SpannableString(finalString)
        val color = ContextCompat.getColor(context, R.color.secondaryText)
        spannable.setSpan(
            ForegroundColorSpan(color),
            0,
            coloredString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
    }

    private fun getBalanceString(value: Double, code: String): String {
        val isNegative = value < 0.0
        val prefixOne = if (isNegative) "- " else ""
        val prefixTwo = Utils.getSymbolByCode(code)
        return "$prefixOne$prefixTwo${value.absoluteValue}"
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