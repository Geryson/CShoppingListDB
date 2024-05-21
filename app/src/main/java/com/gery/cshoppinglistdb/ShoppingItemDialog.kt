package com.gery.cshoppinglistdb

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.gery.cshoppinglistdb.data.ShoppingItem
import com.gery.cshoppinglistdb.databinding.ShoppingItemDialogBinding

class ShoppingItemDialog(item: ShoppingItem?, position: Int) : DialogFragment() {

    private var shoppingItem = item
    private var shoppingItemPosition = position

    interface ShoppingItemDialogHandler {
        fun shoppingItemCreated(item: ShoppingItem)
        fun shoppingItemModified(item: ShoppingItem, position: Int)
    }

    private lateinit var shoppingItemHandler: ShoppingItemDialogHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ShoppingItemDialogHandler) {
            shoppingItemHandler = context
        } else {
            throw RuntimeException("The Activity does not implement the ShoppingItemDialogHandler interface")
        }
    }

    private lateinit var shoppingDialogBinding: ShoppingItemDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        val editMode = shoppingItem != null

        val dialogTitle = if (editMode) "Edit Item" else "New Item"

        builder.setTitle(dialogTitle)

        shoppingDialogBinding = ShoppingItemDialogBinding.inflate(
            requireActivity().layoutInflater
        )

        val categoryAdapter = ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.category,
            android.R.layout.simple_spinner_item
        )
        categoryAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        shoppingDialogBinding.spItemDialogCategoryPicker.adapter = categoryAdapter

        builder.setView(shoppingDialogBinding.root)

        builder.setPositiveButton("OK") { dialog, which ->
        }

        if (editMode) {
            shoppingDialogBinding.etItemDialogName.setText(shoppingItem!!.name)
            shoppingDialogBinding.etItemDialogDescription.setText(shoppingItem!!.description)
            shoppingDialogBinding.etItemDialogPrice.setText(shoppingItem!!.price.toString())
            shoppingDialogBinding.spItemDialogCategoryPicker.setSelection(shoppingItem!!.category)
            shoppingDialogBinding.cbItemDialogStatus.isChecked = shoppingItem!!.status
        }
        return builder.create()
    }

    override fun onResume() {
        super.onResume()

        val dialog = dialog as AlertDialog
        val positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE)

        positiveButton.setOnClickListener {
            if (shoppingDialogBinding.etItemDialogName.text!!.isNotEmpty()) {
                if (shoppingDialogBinding.etItemDialogPrice.text!!.isNotEmpty()) {
                    if (shoppingItem != null) {
                        handleItemEdit()
                    } else {
                        handleItemCreate()
                    }

                    dialog.dismiss()
                } else {
                    shoppingDialogBinding.etItemDialogPrice.error = "This field can not be empty"
                }
            } else {
                shoppingDialogBinding.etItemDialogName.error = "This field can not be empty"
            }
        }
    }

    private fun handleItemCreate() {
        shoppingItemHandler.shoppingItemCreated(
            generateShoppingItem()
        )

    }

    private fun generateShoppingItem() = ShoppingItem(
        null,
        shoppingDialogBinding.etItemDialogName.text.toString(),
        shoppingDialogBinding.etItemDialogDescription.text.toString(),
        shoppingDialogBinding.etItemDialogPrice.text.toString().toInt(),
        shoppingDialogBinding.spItemDialogCategoryPicker.selectedItemPosition,
        shoppingDialogBinding.cbItemDialogStatus.isChecked
    )

    private fun handleItemEdit() {
        shoppingItemHandler.shoppingItemModified(
            generateShoppingItem(), shoppingItemPosition
        )
    }
}