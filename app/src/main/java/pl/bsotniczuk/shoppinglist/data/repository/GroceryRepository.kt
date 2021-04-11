package pl.bsotniczuk.shoppinglist.data.repository

import androidx.lifecycle.LiveData
import pl.bsotniczuk.shoppinglist.data.dao.GroceryDao
import pl.bsotniczuk.shoppinglist.data.model.GroceryItem

class GroceryRepository(private val groceryDao: GroceryDao) {

    val readAllData: LiveData<List<GroceryItem>> = groceryDao.readAllData()
    val readAllSortByDate: LiveData<List<GroceryItem>> = groceryDao.readAllSortByDate()

    suspend fun addGrocery(groceryItem: GroceryItem) {
        groceryDao.addGrocery(groceryItem)
    }

    suspend fun updateGrocery(groceryItem: GroceryItem) {
        groceryDao.updateGrocery(groceryItem)
    }

    suspend fun deleteGrocery(groceryItem: GroceryItem) {
        groceryDao.deleteGrocery(groceryItem)
    }

    suspend fun deleteAll() {
        groceryDao.deleteAll()
    }

    suspend fun deleteGroceriesWithShoppingId(id: Int) {
        groceryDao.deleteGroceriesWithShoppingId(id)
    }

    fun readAllGroceryForShoppingId(id: Int) : LiveData<List<GroceryItem>> {
        return groceryDao.readAllGroceryForShoppingId(id)
    }

    fun readAllGroceryForShoppingIdThatAreDone(id: Int) : LiveData<List<GroceryItem>> {
        return groceryDao.readAllGroceryForShoppingIdThatAreDone(id)
    }
}