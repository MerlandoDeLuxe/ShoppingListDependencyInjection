package com.example.shoppinglistdependencyinjection.di

import com.example.shoppinglisttest.presentation.MainActivity
import com.example.shoppinglisttest.presentation.ShopItemFragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(
    modules = [
        ViewModelModule::class
    ]
)
interface ActivityComponent {

    fun inject(activity: MainActivity)
    fun inject(fragment: ShopItemFragment)

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance @ShopItemIdQualifier shopItemId: Int,
            @BindsInstance @ScreenModeQualifier screenMode: String
        ): ActivityComponent
    }
}