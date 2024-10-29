package com.example.shoppinglisttest.data.database

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.shoppinglisttest.domain.ShopItem
import com.example.shoppinglisttest.domain.ShopItemDbModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface ShopListItemDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addShopItemToDatabase(shopItemDbModel: ShopItemDbModel)

    @Update
    suspend fun editShopItemInDatabase(shopItemDbModel: ShopItemDbModel)

    @Query("select * from shop_item")
    fun getListShopItemFromDatabase(): LiveData<List<ShopItemDbModel>>

    @Query("select * from shop_item where id = :shopItemId")
    suspend fun getShopItemFromDatabase(shopItemId: Int): ShopItemDbModel

    @Delete
    suspend fun removeShopItemFromDatabase(shopItemDbModel: ShopItemDbModel)

    @Query("select * from shop_item where id =:shopItemId")
    fun monitoringShopItemExist(shopItemId: Int): LiveData<ShopItemDbModel>

    @Query("select * from shop_item")
    fun getListShopCursorFromDatabase(): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addShopItemSyncToDatabase(shopItemDbModel: ShopItemDbModel)

    @Query("delete from shop_item where id = :shopItemId")
    fun removeShopItemFromDatabaseByIdSync(shopItemId: Int): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun editShopItemInDatabaseSync(shopItemDbModel: ShopItemDbModel): Int
}