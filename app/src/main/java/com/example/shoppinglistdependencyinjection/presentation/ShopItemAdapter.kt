package com.example.shoppinglisttest.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.shoppinglistdependencyinjection.R
import com.example.shoppinglisttest.domain.ShopItem
import javax.inject.Inject

class ShopItemAdapter @Inject constructor(): ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()) {

    private val TAG = "ShopItemAdapter"
    var onClickListener: ((ShopItem) -> Unit)? = null
    var onLongClickListener: ((ShopItem) -> Unit)? = null

    companion object {
        private const val SHOP_ITEM_ENABLED = 100
        private const val SHOP_ITEM_DISABLED = 200
    }

    override fun getItemViewType(position: Int): Int {
        val shopItem = getItem(position)
        return if (shopItem.enabled) {
            SHOP_ITEM_ENABLED
        } else {
            SHOP_ITEM_DISABLED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
//        Log.d(TAG, "onCreateViewHolder: ")
        val layoutID = when (viewType) {
            SHOP_ITEM_ENABLED -> R.layout.new_enabled_shopitem_layout_template
            SHOP_ITEM_DISABLED -> R.layout.new_disabled_shopitem_layout_template
            else -> throw RuntimeException("Неизвестный тип макета")
        }
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutID, parent, false)

        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: ")
        val shopItem = getItem(position)
        holder.textViewShopItemName.text = shopItem.name
        holder.textViewShopItemQuantity.text = shopItem.quantity.toString()

        holder.itemView.setOnClickListener {
            onClickListener?.invoke(shopItem)
        }

        holder.itemView.setOnLongClickListener {
            onLongClickListener?.invoke(shopItem)
            false
        }
    }


}