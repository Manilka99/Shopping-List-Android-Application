package com.example.shoppinglist
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ShoppingListAdapter :
    ListAdapter<ShoppingItem, ShoppingListAdapter.ShoppingItemViewHolder>(ShoppingItemDiffCallback()) {

    private lateinit var database: DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.shopping_list_item, parent, false)
        return ShoppingItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoppingItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ShoppingItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemNameTextView: TextView = itemView.findViewById(R.id.item_name_textview)
        private val itemQuantityTextView: TextView = itemView.findViewById(R.id.item_quantity_textview)
        private val itemCheckBox: CheckBox = itemView.findViewById(R.id.item_checkbox)

        init {
            database = FirebaseDatabase.getInstance().reference.child("shopping_list")

            // Set an OnClickListener for the delete button
            val deleteButton = itemView.findViewById<ImageView>(R.id.delete_item_button)
            deleteButton.setOnClickListener {
                val item = getItem(adapterPosition) // Use adapterPosition here
                // Assuming you have an 'id' field in your ShoppingItem model
                val itemIdToDelete = item.id

                // Remove the item from the database
                database.child(itemIdToDelete).removeValue()
            }

            itemCheckBox.setOnCheckedChangeListener { _, isChecked ->
                val item = getItem(adapterPosition) // Use adapterPosition here
                item.isChecked = isChecked
                database.child(item.id).setValue(item)
            }

        }

        fun bind(item: ShoppingItem) {
            itemNameTextView.text = item.name
            itemQuantityTextView.text = "Quantity: ${item.quantity}"
            itemCheckBox.isChecked = item.isChecked
        }
    }
}

class ShoppingItemDiffCallback : DiffUtil.ItemCallback<ShoppingItem>() {
    override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
        return oldItem == newItem
    }
}
