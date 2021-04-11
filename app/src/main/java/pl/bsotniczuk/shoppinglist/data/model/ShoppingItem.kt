package pl.bsotniczuk.shoppinglist.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "shopping_table")
data class ShoppingItem (

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "shopping_list_name")
    val shopping_list_name: String,
    //description should also be included, think about, how you want to manage data, on database side or on load to recyclerView or on update of grocerylist
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "is_archived")
    val is_archived: Boolean,
    @ColumnInfo(name = "date")
    val date: Date

)