package com.example.shoppinglisttest.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglistdependencyinjection.R
import com.example.shoppinglistdependencyinjection.presentation.ShopItemApp
import com.example.shoppinglistdependencyinjection.presentation.ShopItemViewModelFactory
import com.example.shoppinglisttest.domain.ShopItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishListener {
    private val TAG = "MainActivity"

    private lateinit var recycleView: RecyclerView
    private lateinit var imageViewNewShopItem: FloatingActionButton
    private lateinit var adapter: ShopItemAdapter

    @Inject
    lateinit var viewModelFactory: ShopItemViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }

    private var shopItemContainer: FragmentContainerView? = null

    private val component by lazy {
        (application as ShopItemApp).component
            .activityComponentFactory()
            .create(-1,"123")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initializeAllElements()
        observeViewModel()
        setOnClickListeners()
        setupSwipeClickListener()
        runFragmentOnMainScreen()

    }

    private fun runFragmentOnMainScreen(shopItemId: Int = ShopItem.UNKNOWN_ID) {
        if (isHorizontalScreen()) {
            supportFragmentManager.popBackStack()
            if (shopItemId == ShopItem.UNKNOWN_ID) {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.shop_item_container,
                        ShopItemFragment().addNewShopItemInstance()
                    )
                    .addToBackStack(null)
                    .commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.shop_item_container,
                        ShopItemFragment().editShopItemInstance(shopItemId)
                    )
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    private fun isHorizontalScreen(): Boolean {
        return shopItemContainer != null
    }

    private fun observeViewModel() {
        viewModel.coinInfoList.observe(this, {
            adapter.submitList(it)
        })
    }

    private fun setOnClickListeners() {
        adapter.onClickListener = {
            if (isHorizontalScreen()) {
                runFragmentOnMainScreen(it.id)
            } else {
                val intent = ShopItemActivity().getIntentEditItem(this, it.id)
                startActivity(intent)
            }
        }

        adapter.onLongClickListener = {
            viewModel.editShopItemInDatabase(it)
        }

        imageViewNewShopItem.setOnClickListener {
            if (isHorizontalScreen()) {
                runFragmentOnMainScreen()
            } else {
                val intent = ShopItemActivity().getIntentAddNewItem(this)
                startActivity(intent)
            }
        }
    }

    private fun setupSwipeClickListener() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val shopItem = adapter.currentList.get(viewHolder.adapterPosition)
                viewModel.removeShopItemFromDatabase(shopItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recycleView)
    }

    private fun initializeAllElements() {
        imageViewNewShopItem = findViewById(R.id.imageViewNewShopItem)

        adapter = ShopItemAdapter()
        recycleView = findViewById(R.id.recycleView)
        recycleView.adapter = adapter

        shopItemContainer = findViewById(R.id.shop_item_container)
    }

    override fun onEditingFinish(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack() //после вывода сообщения удаляем фрагмент из стека
    }
}