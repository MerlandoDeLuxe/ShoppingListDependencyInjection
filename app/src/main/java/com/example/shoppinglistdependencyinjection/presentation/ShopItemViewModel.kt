package com.example.shoppinglisttest.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglisttest.data.ShopListRepositoryImpl
import com.example.shoppinglisttest.data.database.ShopListItemDAO
import com.example.shoppinglisttest.domain.AddShopItemUseCase
import com.example.shoppinglisttest.domain.EditShopItemUseCase
import com.example.shoppinglisttest.domain.GetShopItemUseCase
import com.example.shoppinglisttest.domain.ShopItem
import com.example.shoppinglisttest.domain.ShopItemDbModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopItemViewModel @Inject constructor(
    private val addShopItemUseCase: AddShopItemUseCase,
    private val getShopItemUseCase: GetShopItemUseCase,
    private val editShopItemUseCase: EditShopItemUseCase,
    private val connectDB: ShopListItemDAO
) : ViewModel() {

    private val TAG = "ShopItemViewModel"

    private val _shouldCloseActivity = MutableLiveData<Unit>()
    val shouldCloseActivity: LiveData<Unit>
        get() = _shouldCloseActivity

    private val _getShopItemFromDB_LD = MutableLiveData<ShopItem>()
    val getShopItemFromDB_LD: LiveData<ShopItem>
        get() = _getShopItemFromDB_LD

    private val _errorInputName_LD = MutableLiveData<Boolean>()
    val errorInputName_LD: LiveData<Boolean>
        get() = _errorInputName_LD
    private val _errorInputQuantity_LD = MutableLiveData<Boolean>()
    val errorInputQuantity_LD: LiveData<Boolean>
        get() = _errorInputQuantity_LD

    fun addNewShopItem(inputName: String, inputQuantity: String) {
        val name = parseName(inputName)
        val quantity = parseQuantity(inputQuantity)
        val isFieldsValid = validateParam(name, quantity)
        if (isFieldsValid) {
            val shopItem = ShopItem(0, name, quantity, true)
            viewModelScope.launch {
                addShopItemUseCase(shopItem)
                finishWork()
            }
        }
    }

    fun monitoringShopItemExist(shopItemId: Int): LiveData<ShopItemDbModel> {
        return connectDB.monitoringShopItemExist(shopItemId)
    }

    fun getShopItemFromDatabase(shopItemId: Int) {
        viewModelScope.launch {
            val editingShopItem = getShopItemUseCase(shopItemId)
            _getShopItemFromDB_LD.value = editingShopItem
        }
    }

    fun editShopItemInDatabase(inputName: String, inputQuantity: String) {
        val name = parseName(inputName)
        val quantity = parseQuantity(inputQuantity)
        val isFieldsValid = validateParam(name, quantity)
        if (isFieldsValid) {
            val tempShopItem = _getShopItemFromDB_LD.value?.copy(name = name, quantity = quantity)

            if (tempShopItem != null) {
                viewModelScope.launch {
                    editShopItemUseCase(tempShopItem)
                    finishWork()
                }
            }
        }
    }

    private fun finishWork() {
        _shouldCloseActivity.value = Unit
    }

    private fun parseName(inputName: String) = inputName.trim()

    private fun parseQuantity(inputQuantity: String): Int {
        return try {
            inputQuantity.trim().toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun validateParam(name: String, quantity: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName_LD.value = true
            result = false
        }
        if (quantity <= 0) {
            _errorInputQuantity_LD.value = true
            result = false
        }
        return result
    }

    fun resetErrorInputName() {
        _errorInputName_LD.value = false
    }

    fun resetErrorInputQuantity() {
        _errorInputQuantity_LD.value = false
    }

}