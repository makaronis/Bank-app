package com.bank.app.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bank.app.data.entities.CardholderData
import com.bank.app.presentation.utils.ImageUtils
import com.bank.bank_app.databinding.ItemCardBinding

class CardAdapter(
    private val onClick: (String) -> Unit,
    private val selectedCardNumber: String?,
) : ListAdapter<CardholderData, CardViewHolder>(diff) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CardViewHolder.newInstance(parent, onClick)

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = getItem(position)
        val isSelected = selectedCardNumber == item.cardNumber
        holder.onBind(item, isSelected)
    }

    companion object {
        private val diff = object : DiffUtil.ItemCallback<CardholderData>() {
            override fun areItemsTheSame(
                oldItem: CardholderData,
                newItem: CardholderData
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: CardholderData,
                newItem: CardholderData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class CardViewHolder(
    private val binding: ItemCardBinding,
    private val onClick: (String) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: CardholderData, isSelected: Boolean) {
        val context = binding.root.context
        binding.apply {
            root.setOnClickListener { onClick.invoke(item.cardNumber) }
            ivType.setImageDrawable(ImageUtils.getImageByCardType(item.type, context))
            ivSelected.visibility = if (isSelected) View.VISIBLE else View.INVISIBLE
            tvCardNumber.text = item.cardNumber
        }
    }

    companion object {
        fun newInstance(
            parent: ViewGroup,
            onClick: (String) -> Unit,
        ): CardViewHolder {
            val binding =
                ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CardViewHolder(binding, onClick)
        }
    }
}