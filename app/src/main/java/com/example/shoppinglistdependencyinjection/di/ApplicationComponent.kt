package com.example.shoppinglistdependencyinjection.di

import android.app.Application
import com.example.shoppinglistdependencyinjection.data.ShopListProvider
import com.example.shoppinglisttest.presentation.MainActivity
import com.example.shoppinglisttest.presentation.ShopItemFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DomainModule::class,
        DataModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {

    fun inject(activity: MainActivity)
    fun inject(fragment: ShopItemFragment)
    fun inject(provider: ShopListProvider)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}