package com.example.shoppinglisttest.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shoppinglisttest.domain.ShopItemDbModel

@Database(entities = [ShopItemDbModel::class], version = 2, exportSchema = false)
abstract class ShopListItemDatabase : RoomDatabase() {

    companion object {
        private val DB_NAME = "shop_item_db"
        private var db_instance: ShopListItemDatabase? = null

        fun getInstance(application: Application): ShopListItemDatabase {
            if (db_instance == null) {
                db_instance =
                    Room.databaseBuilder(application, ShopListItemDatabase::class.java, DB_NAME)
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return db_instance as ShopListItemDatabase
        }
    }

    abstract val shopListItemDAO: ShopListItemDAO
}