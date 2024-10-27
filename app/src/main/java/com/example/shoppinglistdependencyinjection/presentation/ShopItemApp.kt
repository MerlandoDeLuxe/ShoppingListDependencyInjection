package com.example.shoppinglistdependencyinjection.presentation

import android.app.Application
import com.example.shoppinglistdependencyinjection.di.DaggerApplicationComponent

class ShopItemApp : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory()
            .create(this)
    }
}