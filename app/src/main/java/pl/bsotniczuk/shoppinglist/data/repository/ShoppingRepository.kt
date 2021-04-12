package pl.bsotniczuk.shoppinglist.data.repository

import androidx.lifecycle.LiveData
import pl.bsotniczuk.shoppinglist.data.dao.ShoppingDao
import pl.bsotniczuk.shoppinglist.data.model.ShoppingItem

class ShoppingRepository(private val shoppingDao: ShoppingDao) {

    val readLastAdded: LiveData<List<ShoppingItem>> = shoppingDao.readLastAdded()
    val readAllNotArchivedSortByDate: LiveData<List<ShoppingItem>> = shoppingDao.readAllNotArchivedSortByDate()
    val readAllArchivedSortByDate: LiveData<List<ShoppingItem>> = shoppingDao.readAllArchivedSortByDate()

    suspend fun addShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.addShoppingItem(shoppingItem)
    }

    suspend fun updateShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.updateShoppingItem(shoppingItem)
    }

    suspend fun updateShoppingDescriptionById(id: Int, descr: String) {
        shoppingDao.updateShoppingDescriptionById(id, descr)
    }

    suspend fun updateShoppingArchivedById(id: Int, isArchived: Boolean) {
        shoppingDao.updateShoppingArchivedById(id, isArchived)
    }

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    suspend fun deleteAll() {
        shoppingDao.deleteAll()
    }

    suspend fun deleteShoppingListWithId(id: Int) {
        shoppingDao.deleteShoppingItemWithId(id)
    }

}