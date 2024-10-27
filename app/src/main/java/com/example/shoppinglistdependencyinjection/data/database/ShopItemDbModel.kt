package com.example.shoppinglisttest.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.inject.Inject

@Entity("shop_item")
data class ShopItemDbModel @Inject constructor(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val quantity: Int,
    val enabled: Boolean
) {
    companion object {
        const val UNKNOWN_ID = -1
    }
}
