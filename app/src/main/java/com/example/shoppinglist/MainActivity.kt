package com.example.shoppinglist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var addButton: Button
    private lateinit var adapter: ShoppingListAdapter
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        addButton = findViewById(R.id.add_button)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ShoppingListAdapter()
        recyclerView.adapter = adapter

        // Initialize Firebase components
        val currentUser = FirebaseAuth.getInstance().currentUser
        database = FirebaseDatabase.getInstance().reference.child("shopping_list")

        // Listen for changes in shopping list data
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = mutableListOf<ShoppingItem>()
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(ShoppingItem::class.java)
                    item?.let { items.add(it) }
                }
                adapter.submitList(items)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainActivity", "Firebase database error: ${error.message}")
            }
        })

        // Set up add button click listener
        addButton.setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
        }
    }
}
