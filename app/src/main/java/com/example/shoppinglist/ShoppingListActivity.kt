package com.example.shoppinglist

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.example.shoppinglist.R
import android.widget.Button



class ShoppingListActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)

        database = Firebase.database.reference.child("shoppingLists")

        val linearLayoutShoppingLists = findViewById<LinearLayout>(R.id.linearShoppingLists)

        // Read shopping lists from Firebase Realtime Database
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                linearLayoutShoppingLists.removeAllViews()

                for (childSnapshot in snapshot.children) {
                    val shoppingListName = childSnapshot.key.toString()

                    val buttonShoppingList = Button(this@ShoppingListActivity)
                    buttonShoppingList.text = shoppingListName
                    buttonShoppingList.setOnClickListener {
                        // Handle click on shopping list button (navigate to shopping list items)
                    }

                    linearLayoutShoppingLists.addView(buttonShoppingList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ShoppingListActivity", "Failed to read shopping lists", error.toException())
            }
        })

        val buttonAddShoppingList = findViewById<Button>(R.id.buttonAddShoppingList)
        buttonAddShoppingList.setOnClickListener {
            // Implement logic to add a new shopping list
            // Example: Prompt user for shopping list name and add to Firebase Realtime Database
        }
    }
}
