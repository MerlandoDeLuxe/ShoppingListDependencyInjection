package com.example.shoppinglisttest.data


import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.shoppinglistdependencyinjection.data.mapper.ShopItemMapper
import com.example.shoppinglisttest.data.database.ShopListItemDAO
import com.example.shoppinglisttest.domain.ShopItem
import com.example.shoppinglisttest.domain.ShopItemListRepository
import javax.inject.Inject

class ShopListRepositoryImpl @Inject constructor(
    val connectDb: ShopListItemDAO,
    val mapper: ShopItemMapper
) : ShopItemListRepository {

    private val TAG = "ShopListRepositoryImpl"

    override suspend fun addShopItemToDatabase(shopItem: ShopItem) {
        connectDb.addShopItemToDatabase(mapper.mapShopItemToShopItemDBModel(shopItem))
    }

    override suspend fun editShopItemInDatabase(shopItem: ShopItem) {
        connectDb.editShopItemInDatabase(mapper.mapShopItemToShopItemDBModel(shopItem))
    }

    override fun getListShopItemFromDatabase(): LiveData<List<ShopItem>> {
        return connectDb.getListShopItemFromDatabase()//Преобразование одной LiveData в другую
            .map { it -> it.map { mapper.mapShopItemDbModelToShopItem(it) } }
    }

    override suspend fun getShopItemFromDatabase(shopItemId: Int): ShopItem {
        val shopItemDbModel = connectDb.getShopItemFromDatabase(shopItemId)
        val shopItem = mapper.mapShopItemDbModelToShopItem(shopItemDbModel)
        return shopItem
    }

    override suspend fun removeShopItemFromDatabase(shopItem: ShopItem) {
        connectDb.removeShopItemFromDatabase(mapper.mapShopItemToShopItemDBModel(shopItem))
    }

}