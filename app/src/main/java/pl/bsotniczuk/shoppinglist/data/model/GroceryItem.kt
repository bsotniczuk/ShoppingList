package pl.bsotniczuk.shoppinglist.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "grocery_table")
data class GroceryItem (

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "product_name")
    val product_name: String,
    @ColumnInfo(name = "quantity")
    val quantity: Int,
    @ColumnInfo(name = "done")
    val done: Boolean,
    @ColumnInfo(name = "date")
    val date: Date,
    @ColumnInfo(name = "id_of_shopping_list_item")
    val id_of_shopping_list_item: Int

)