package pl.bsotniczuk.shoppinglist.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pl.bsotniczuk.shoppinglist.data.model.GroceryItem

@Dao
interface GroceryDao {

    @Insert
    fun addGrocery(groceryItem: GroceryItem)

    @Update
    suspend fun updateGrocery(groceryItem: GroceryItem)

    @Query ("UPDATE grocery_table SET done = :isDone WHERE id = :id")
    suspend fun updateGroceryDoneById(id: Int, isDone: Boolean)

    @Delete
    suspend fun deleteGrocery(groceryItem: GroceryItem)

    @Query("DELETE FROM grocery_table WHERE id_of_shopping_list_item = :id")
    suspend fun deleteGroceriesWithShoppingId(id: Int)

    @Query("SELECT * FROM grocery_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<GroceryItem>>

    @Query("SELECT * FROM grocery_table ORDER BY date DESC")
    fun readAllSortByDate(): LiveData<List<GroceryItem>>//List<GroceryItem>

    @Query("SELECT * FROM grocery_table WHERE id_of_shopping_list_item = :id ORDER BY date DESC")
    fun readAllGroceryForShoppingId(id: Int): LiveData<List<GroceryItem>>

    @Query("DELETE FROM grocery_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM grocery_table WHERE product_name = :name ORDER BY id ASC LIMIT 1")
    fun findGroceryByName(name: String): GroceryItem

    /*@Query("SELECT * FROM grocery_table WHERE product_name = :name ORDER BY id ASC LIMIT 1")
    fun findGroceryByName(name: String): LiveData<List<GroceryItem>>*/
}