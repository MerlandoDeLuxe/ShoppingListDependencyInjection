package com.example.shoppinglisttest.domain

import javax.inject.Inject

class AddShopItemUseCase @Inject constructor(private val repository: ShopItemListRepository) {

    operator suspend fun invoke(shopItem: ShopItem) =
        repository.addShopItemToDatabase(shopItem)

}