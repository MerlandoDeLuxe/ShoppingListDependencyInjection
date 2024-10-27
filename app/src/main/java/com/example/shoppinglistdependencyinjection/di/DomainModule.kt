package com.example.shoppinglistdependencyinjection.di

import com.example.shoppinglisttest.data.ShopListRepositoryImpl
import com.example.shoppinglisttest.domain.ShopItemListRepository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {

    @Binds
    @ApplicationScope
    fun bindShopListRepository(impl: ShopListRepositoryImpl): ShopItemListRepository
}