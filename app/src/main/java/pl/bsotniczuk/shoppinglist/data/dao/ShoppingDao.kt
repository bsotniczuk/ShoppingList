package pl.bsotniczuk.shoppinglist.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pl.bsotniczuk.shoppinglist.data.model.GroceryItem
import pl.bsotniczuk.shoppinglist.data.model.ShoppingItem

@Dao
interface ShoppingDao {

    @Insert
    fun addShoppingItem(shoppingItem: ShoppingItem)

    @Update
    suspend fun updateShoppingItem(shoppingItem: ShoppingItem)

    @Query ("UPDATE shopping_table SET description = :descr WHERE id = :id")
    suspend fun updateShoppingDescriptionById(id: Int, descr: String)

    @Delete
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    @Query("DELETE FROM shopping_table WHERE id = :id")
    suspend fun deleteShoppingItemWithId(id: Int)

    //to delete
    @Query("SELECT * FROM shopping_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<ShoppingItem>>

    //to delete
    @Query("SELECT * FROM shopping_table ORDER BY date DESC")
    fun readAllSortByDate(): LiveData<List<ShoppingItem>>

    @Query("SELECT * FROM shopping_table ORDER BY date DESC LIMIT 1")
    fun readLastAdded(): LiveData<List<ShoppingItem>>

    @Query("SELECT * FROM shopping_table WHERE is_archived = 0 ORDER BY date DESC")
    fun readAllNotArchivedSortByDate(): LiveData<List<ShoppingItem>>

    @Query("SELECT * FROM shopping_table WHERE is_archived = 1 ORDER BY date DESC")
    fun readAllArchivedSortByDate(): LiveData<List<ShoppingItem>>

    @Query("DELETE FROM shopping_table")
    suspend fun deleteAll()

}