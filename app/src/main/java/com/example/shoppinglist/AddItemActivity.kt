package com.example.shoppinglist

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddItemActivity : AppCompatActivity() {
    private lateinit var itemNameEditText: EditText
    private lateinit var itemQuantityEditText: EditText
    private lateinit var addItemButton: Button
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        itemNameEditText = findViewById(R.id.item_name_edittext)
        itemQuantityEditText = findViewById(R.id.item_quantity_edittext)
        addItemButton = findViewById(R.id.add_item_button)

        database = FirebaseDatabase.getInstance().reference.child("shopping_list")

        addItemButton.setOnClickListener {
            val itemName = itemNameEditText.text.toString().trim()
            val quantity = itemQuantityEditText.text.toString().trim().toIntOrNull() ?: 0

            if (itemName.isNotEmpty()) {
                val newItemId = database.push().key ?: ""
                val newItem = ShoppingItem(newItemId, itemName, quantity, false)
                database.child(newItemId).setValue(newItem)
                finish()
            }
        }
    }
}
