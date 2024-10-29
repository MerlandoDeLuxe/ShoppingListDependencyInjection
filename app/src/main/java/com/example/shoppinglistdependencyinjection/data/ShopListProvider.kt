package com.example.shoppinglistdependencyinjection.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.example.shoppinglistdependencyinjection.data.mapper.ShopItemMapper
import com.example.shoppinglistdependencyinjection.presentation.ShopItemApp
import com.example.shoppinglisttest.data.database.ShopListItemDAO
import com.example.shoppinglisttest.domain.ShopItem
import javax.inject.Inject

class ShopListProvider : ContentProvider() {
    private val TAG = "ShopListProvider"

    @Inject
    lateinit var connectDB: ShopListItemDAO

    @Inject
    lateinit var mapper: ShopItemMapper

    private val component by lazy {
        (context as ShopItemApp).component
    }

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI("com.example.shoppinglisttest", "shop_item", GET_SHOP_ITEM_QUERY)
        addURI("com.example.shoppinglisttest", "shop_item/#", GET_SHOP_ITEM_BY_ID)
        addURI("com.example.shoppinglisttest", "shop_item/*", GET_SHOP_ITEM_BY_STRING)
    }

    override fun onCreate(): Boolean {
        component.inject(this)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val code = uriMatcher.match(uri)
        return when (code) {
            GET_SHOP_ITEM_QUERY -> {
                Log.d(TAG, "query: code = $code")
                connectDB.getListShopCursorFromDatabase()
            }

            GET_SHOP_ITEM_BY_ID -> {
                Log.d(TAG, "query: code = $code")
                connectDB.getListShopCursorFromDatabase()
            }

            GET_SHOP_ITEM_BY_STRING -> {
                Log.d(TAG, "query: code = $code")
                connectDB.getListShopCursorFromDatabase()
            }

            else -> {
                Log.d(TAG, "query: null")
                null
            }
        }
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val code = uriMatcher.match(uri)
        when (code) {
            GET_SHOP_ITEM_QUERY -> {
                Log.d(TAG, "query: code = $code")
                if (values != null) {
                    val id = values.getAsInteger("id")
                    val name = values.getAsString("name")
                    val quantity = values.getAsInteger("quantity")
                    val enabled = values.getAsBoolean("quantity")

                    val shopItem = ShopItem(
                        id = id,
                        name = name,
                        quantity = quantity,
                        enabled = enabled
                    )
                    connectDB.addShopItemSyncToDatabase(mapper.mapShopItemToShopItemDBModel(shopItem))
                }
            }

            else -> {
                Log.d(TAG, "query: null")
                return null
            }
        }
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val code = uriMatcher.match(uri)
        when (code) {
            GET_SHOP_ITEM_QUERY -> {
                val id = selectionArgs?.get(0)?.toInt() ?: -1
                val count = connectDB.removeShopItemFromDatabaseByIdSync(id)
                Log.d(TAG, "delete: удалено элементов: $count")
            }
        }
        return 0 //0 возвращаем если никакие данные удалены не были
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val code = uriMatcher.match(uri)
        when (code) {
            GET_SHOP_ITEM_QUERY -> {
                if (values != null) {
                    val id = values.getAsInteger("id")
                    val name = values.getAsString("name")
                    val quantity = values.getAsInteger("quantity")
                    val enabled = values.getAsBoolean("enabled")
                    Log.d(TAG, "update: values = $values")
                    val shopitem = ShopItem(
                        id = id,
                        name = name,
                        quantity = quantity,
                        enabled = enabled
                    )
                    Log.d(TAG, "update: shopitem = $shopitem")
                    val count = connectDB.editShopItemInDatabaseSync(mapper.mapShopItemToShopItemDBModel(shopitem))
                    Log.d(TAG, "update: изменено элементов: $count")
                }
            }
        }
        return 0 //0 возвращаем если никакие данные удалены не были
    }

    companion object {
        private const val GET_SHOP_ITEM_QUERY = 100
        private const val GET_SHOP_ITEM_BY_ID = 101
        private const val GET_SHOP_ITEM_BY_STRING = 102
    }
}