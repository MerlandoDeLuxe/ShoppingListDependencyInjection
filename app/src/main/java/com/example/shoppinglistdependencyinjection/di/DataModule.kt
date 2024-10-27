package com.example.shoppinglistdependencyinjection.di

import android.app.Application
import com.example.shoppinglisttest.data.database.ShopListItemDAO
import com.example.shoppinglisttest.data.database.ShopListItemDatabase
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    companion object {

        @Provides
        @ApplicationScope
        fun shopListItemDAO(
            application: Application
        ): ShopListItemDAO {
            return ShopListItemDatabase.getInstance(application).shopListItemDAO
        }
    }
}