package com.example.shoppinglisttest.domain

import javax.inject.Inject

class GetListShopItemUseCase @Inject constructor(private val repository: ShopItemListRepository) {

    operator fun invoke() = repository.getListShopItemFromDatabase()

}