package com.example.shoppinglisttest.domain

import javax.inject.Inject

class RemoveShopItemUseCase @Inject constructor(private val repository: ShopItemListRepository) {

    operator suspend fun invoke(shopItem: ShopItem) =
        repository.removeShopItemFromDatabase(shopItem)
}