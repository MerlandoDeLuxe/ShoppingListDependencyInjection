package com.example.shoppinglistdependencyinjection.di

import androidx.lifecycle.ViewModel
import com.example.shoppinglisttest.presentation.MainViewModel
import com.example.shoppinglisttest.presentation.ShopItemViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(impl: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShopItemViewModel::class)
    fun bindShopItemViewModel(impl: ShopItemViewModel): ViewModel
}