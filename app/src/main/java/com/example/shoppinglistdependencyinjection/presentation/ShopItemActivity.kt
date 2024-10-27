package com.example.shoppinglisttest.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.shoppinglistdependencyinjection.R
import com.example.shoppinglisttest.domain.ShopItem

class ShopItemActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishListener {
    private var screenMode = UNKNOWN_SCREEN_MODE
    private var shopItemId = ShopItem.UNKNOWN_ID

    companion object {
        private const val EXTRA_SHOP_ITEM_ID = "shop_item_id"
        private const val EXTRA_SCREEN_MODE = "screen_mode"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val UNKNOWN_SCREEN_MODE = "unknown_screen_mode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item_contrainer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.shop_item_container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (savedInstanceState == null) {
            parseParams()
        }
        screenSelector()
    }

    private fun screenSelector() {
        val fragment =
            when (screenMode) {
                MODE_ADD -> ShopItemFragment().addNewShopItemInstance()
                MODE_EDIT -> ShopItemFragment().editShopItemInstance(shopItemId)
                else -> throw RuntimeException("Неизвестный тип экрана $screenMode")
            }
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.shop_item_container,
                fragment
            ).commit()
    }

    private fun parseParams() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Отсутствует тип экрана")
        }

        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("Неизвестный тип экрана $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Отсутствует ID элемента")
            }
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, -1)
        }
    }

    fun getIntentAddNewItem(context: Context): Intent {
        val intent = Intent(context, ShopItemActivity::class.java).apply {
            putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
        }
        return intent
    }

    fun getIntentEditItem(context: Context, shopItemId: Int): Intent {
        val intent = Intent(context, ShopItemActivity::class.java).apply {
            putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
        }
        return intent
    }

    override fun onEditingFinish(message: String) {
        finish()
    }
}