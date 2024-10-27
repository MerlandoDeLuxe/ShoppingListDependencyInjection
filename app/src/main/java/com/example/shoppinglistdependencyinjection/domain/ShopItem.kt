package com.example.shoppinglisttest.domain

import javax.inject.Inject

data class ShopItem @Inject constructor(
    val id: Int,
    val name: String,
    val quantity: Int,
    val enabled: Boolean
) {
    companion object {
        const val UNKNOWN_ID = -1
    }
}
