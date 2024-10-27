package com.example.shoppinglisttest.domain

import javax.inject.Inject

class GetShopItemUseCase @Inject constructor(private val repository: ShopItemListRepository) {

    operator suspend fun invoke(shopItemId: Int) =
        repository.getShopItemFromDatabase(shopItemId)

}