package pl.bsotniczuk.shoppinglist.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pl.bsotniczuk.shoppinglist.data.model.GroceryItem

@Dao
interface GroceryDao {

    @Insert/*(onConflict = OnConflictStrategy.IGNORE)*/ //use this in shoppinglist maybe
    fun addGrocery(groceryItem: GroceryItem)

    @Update
    suspend fun updateGrocery(groceryItem: GroceryItem)

    @Delete
    suspend fun deleteGrocery(groceryItem: GroceryItem)

    @Query("DELETE FROM grocery_table WHERE id_of_shopping_list_item = :id")
    suspend fun deleteGroceriesWithShoppingId(id: Int)

    @Query("SELECT * FROM grocery_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<GroceryItem>>

    @Query("SELECT * FROM grocery_table ORDER BY date DESC")
    fun readAllSortByDate(): LiveData<List<GroceryItem>>//List<GroceryItem>

    @Query("SELECT * FROM grocery_table WHERE id_of_shopping_list_item = :id ORDER BY date DESC")
    fun readAllGroceryForShoppingId(id: Int): LiveData<List<GroceryItem>>//List<GroceryItem>

    // you can use readAllGroceryForShoppingId and then programatically get all done to get count of all with exact shopping id and count all done with the exact shopping id
    @Query("SELECT * FROM grocery_table WHERE id_of_shopping_list_item = :id AND done = 1")
    fun readAllGroceryForShoppingIdThatAreDone(id: Int): LiveData<List<GroceryItem>>//List<GroceryItem>

    @Query("SELECT * FROM grocery_table ORDER BY product_name ASC")
    fun getAlphabetizedWords(): LiveData<List<GroceryItem>>//List<GroceryItem>

    @Query("DELETE FROM grocery_table")
    suspend fun deleteAll()
}