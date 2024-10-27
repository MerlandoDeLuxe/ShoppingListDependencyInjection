package com.example.shoppinglisttest.domain

import androidx.lifecycle.LiveData

interface ShopItemListRepository {

    suspend fun addShopItemToDatabase(shopItem: ShopItem)

    suspend fun editShopItemInDatabase(shopItem: ShopItem)

    fun getListShopItemFromDatabase(): LiveData<List<ShopItem>>

    suspend fun getShopItemFromDatabase(shopItemId: Int): ShopItem

    suspend fun removeShopItemFromDatabase(shopItem: ShopItem)
}