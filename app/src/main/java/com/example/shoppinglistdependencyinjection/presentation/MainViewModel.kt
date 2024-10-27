package com.example.shoppinglisttest.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglisttest.domain.EditShopItemUseCase
import com.example.shoppinglisttest.domain.GetListShopItemUseCase
import com.example.shoppinglisttest.domain.RemoveShopItemUseCase
import com.example.shoppinglisttest.domain.ShopItem
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getListShopItemUseCase: GetListShopItemUseCase,
    private val editShopItemUseCase: EditShopItemUseCase,
    private val removeShopItemUseCase: RemoveShopItemUseCase
) : ViewModel() {

    private val TAG = "MainViewModel"

    val coinInfoList = getListShopItemUseCase()

    fun editShopItemInDatabase(shopItem: ShopItem) {
        val tempShopItem = shopItem.copy(enabled = !shopItem.enabled)
        viewModelScope.launch {
            editShopItemUseCase(tempShopItem)
        }
    }

    fun removeShopItemFromDatabase(shopItem: ShopItem) {
        viewModelScope.launch {
            removeShopItemUseCase(shopItem)
        }
    }
}