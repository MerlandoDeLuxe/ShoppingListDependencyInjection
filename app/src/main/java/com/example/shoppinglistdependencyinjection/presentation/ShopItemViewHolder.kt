package com.example.shoppinglisttest.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.shoppinglistdependencyinjection.R
import javax.inject.Inject

class ShopItemViewHolder @Inject constructor(itemView: View) : ViewHolder(itemView) {
    val textViewShopItemName = itemView.findViewById<TextView>(R.id.textViewShopItemName)
    val textViewShopItemQuantity = itemView.findViewById<TextView>(R.id.textViewShopItemQuantity)
}