package com.example.shoppinglisttest.presentation

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglistdependencyinjection.R
import com.example.shoppinglistdependencyinjection.presentation.ShopItemApp
import com.example.shoppinglistdependencyinjection.presentation.ShopItemViewModelFactory
import com.example.shoppinglisttest.domain.ShopItem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import javax.inject.Inject
import kotlin.concurrent.thread

class ShopItemFragment : Fragment() {
    private val TAG = "ShopItemFragment"
    private var screenMode = UNKNOWN_SCREEN_MODE
    private var shopItemId = ShopItem.UNKNOWN_ID
    private lateinit var onEditingFinishListener: OnEditingFinishListener

    companion object {
        private const val EXTRA_SHOP_ITEM_ID = "shop_item_id"
        private const val EXTRA_SCREEN_MODE = "screen_mode"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val UNKNOWN_SCREEN_MODE = "unknown_screen_mode"
    }

    private lateinit var textInputLayoutName: TextInputLayout
    private lateinit var editTextName: TextInputEditText
    private lateinit var textInputLayoutQuantity: TextInputLayout
    private lateinit var editTextQuantity: TextInputEditText
    private lateinit var buttonSave: Button

    @Inject
    lateinit var viewModelFactory: ShopItemViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ShopItemViewModel::class.java)
    }

    private val component by lazy {
        (requireActivity().application as ShopItemApp).component
    }

    interface OnEditingFinishListener {
        fun onEditingFinish(message: String)
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
        if (context is OnEditingFinishListener) {
            onEditingFinishListener = context
        } else {
            throw RuntimeException("$context должен реализовывать интерфейс OnEditingFinishListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_item_template, container, false)
    }

    private fun initParams() {
        val args = requireArguments()
        if (!args.containsKey(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Отсутствует параметр экрана")
        }
        screenMode = args.getString(EXTRA_SCREEN_MODE).toString()

        if (screenMode != MODE_ADD && screenMode != MODE_EDIT) {
            throw RuntimeException("Неизвестный параметр экрана $screenMode, требуются параметры: $MODE_ADD или $MODE_EDIT")
        }
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Отсутствует ID элемента")
            }
            shopItemId = args.getInt(EXTRA_SHOP_ITEM_ID, -1)
            Log.d(TAG, "initParams: shopItemId = $shopItemId")
        }
        Log.d(TAG, "initParams: Экран в режиме: $screenMode")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeAllElements(view)
        observeViewModel()
        observeTextEditing()
        selectScreenMode()
    }

    private fun selectScreenMode() {
        when (screenMode) {
            MODE_ADD -> launchScreenAddMode()
            MODE_EDIT -> launchScreenEditMode()
        }
    }

    private fun launchScreenEditMode() {
        viewModel.getShopItemFromDatabase(shopItemId)
        viewModel.getShopItemFromDB_LD.observe(viewLifecycleOwner) {
            Log.d(TAG, "screenEditMode: it = $it")
            editTextName.setText(it.name)
            editTextQuantity.setText(it.quantity.toString())
        }
        buttonSave.setOnClickListener {
//            viewModel.editShopItemInDatabase(
//                editTextName.text.toString(),
//                editTextQuantity.text.toString()
//            )
            thread {
                context?.contentResolver?.update(
                    Uri.parse("content://com.example.shoppinglisttest/shop_item"),
                    ContentValues().apply {
                        put("id", shopItemId)
                        put("name", editTextName.text.toString())
                        put("quantity", editTextQuantity.text.toString().toInt())
                        put("enabled", true)
                    },
                    null
                )
            }
        }

        //Костыль!! Потому что исопльзуется ShopItemDbModel из БД, а не ShopItem
        viewModel.monitoringShopItemExist(shopItemId).observe(viewLifecycleOwner) {
            if (it == null) {
                onEditingFinishListener.onEditingFinish(getString(R.string.item_has_been_deleted))
            }
        }
    }

    private fun launchScreenAddMode() {
        buttonSave.setOnClickListener {
//            viewModel.addNewShopItem(editTextName.text.toString(), editTextQuantity.text.toString())
            thread {
                context?.contentResolver?.insert(
                    Uri.parse("content://com.example.shoppinglisttest/shop_item"),
                    ContentValues().apply {
                        put("id", 0)
                        put("name", editTextName.text.toString())
                        put("quantity", editTextQuantity.text.toString().toInt())
                        put("enabled", true)
                    }
                )
            }

        }
    }

    private fun observeViewModel() {
        viewModel.shouldCloseActivity.observe(viewLifecycleOwner) {
            onEditingFinishListener.onEditingFinish(getString(R.string.edit_complete_successfully))
        }
    }

    private fun observeTextEditing() {
        viewModel.errorInputName_LD.observe(viewLifecycleOwner) {
            if (it) {
                textInputLayoutName.error = getString(R.string.error_name)
            } else {
                textInputLayoutName.error = null
            }
        }
        viewModel.errorInputQuantity_LD.observe(viewLifecycleOwner) {
            if (it) {
                textInputLayoutQuantity.error = getString(R.string.error_quantity1)
            } else {
                textInputLayoutQuantity.error = null
            }
        }

        editTextName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        editTextQuantity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputQuantity()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    fun addNewShopItemInstance(): ShopItemFragment {
        return ShopItemFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_SCREEN_MODE, MODE_ADD)
            }
        }
    }

    fun editShopItemInstance(shopItemId: Int): ShopItemFragment {
        return ShopItemFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_SCREEN_MODE, MODE_EDIT)
                putInt(EXTRA_SHOP_ITEM_ID, shopItemId)
            }
        }
    }

    private fun initializeAllElements(view: View) {
        textInputLayoutName = view.findViewById(R.id.textInputLayoutName)
        editTextName = view.findViewById(R.id.editTextName)
        textInputLayoutQuantity = view.findViewById(R.id.textInputLayoutQuantity)
        editTextQuantity = view.findViewById(R.id.editTextQuantity)
        buttonSave = view.findViewById(R.id.buttonSave)

    }
}