package com.example.shoppinglisttest.presentation

import androidx.recyclerview.widget.DiffUtil
import com.example.shoppinglisttest.domain.ShopItem
import javax.inject.Inject

class ShopItemDiffCallback @Inject constructor(): DiffUtil.ItemCallback<ShopItem>() {
    override fun areContentsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return oldItem.id == newItem.id
    }
}