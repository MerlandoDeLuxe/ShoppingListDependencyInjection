package com.example.shoppinglistdependencyinjection.data.mapper

import com.example.shoppinglisttest.domain.ShopItem
import com.example.shoppinglisttest.domain.ShopItemDbModel
import javax.inject.Inject

class ShopItemMapper @Inject constructor() {

    fun mapShopItemDbModelToShopItem(shopItemDbModel: ShopItemDbModel): ShopItem {
        return ShopItem(
            id = shopItemDbModel.id,
            name = shopItemDbModel.name,
            quantity = shopItemDbModel.quantity,
            enabled = shopItemDbModel.enabled
        )
    }

    fun mapShopItemToShopItemDBModel(shopItem: ShopItem): ShopItemDbModel {
        return ShopItemDbModel(
            id = shopItem.id,
            name = shopItem.name,
            quantity = shopItem.quantity,
            enabled = shopItem.enabled
        )
    }
}