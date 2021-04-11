package pl.bsotniczuk.shoppinglist.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.bsotniczuk.shoppinglist.data.GroceryDatabase
import pl.bsotniczuk.shoppinglist.data.model.ShoppingItem
import pl.bsotniczuk.shoppinglist.data.repository.ShoppingRepository

class ShoppingViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<ShoppingItem>> //to delete
    val readAllSortByDate: LiveData<List<ShoppingItem>> //to delete
    val readLastAdded: LiveData<List<ShoppingItem>>
    val readAllNotArchivedSortByDate: LiveData<List<ShoppingItem>>
    val readAllArchivedSortByDate: LiveData<List<ShoppingItem>>
    private val repository: ShoppingRepository

    init {
        val shoppingDao = GroceryDatabase.getDatabase(application).shoppingDao()
        repository = ShoppingRepository(shoppingDao)

        readAllData = repository.readAllData //to delete
        readAllSortByDate = repository.readAllSortByDate //to delete
        readLastAdded = repository.readLastAdded
        readAllNotArchivedSortByDate = repository.readAllNotArchivedSortByDate
        readAllArchivedSortByDate = repository.readAllArchivedSortByDate
    }

    fun addShoppingItem(shoppingItem: ShoppingItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addShoppingItem(shoppingItem)
        }
    }

    fun updateShoppingItem(shoppingItem: ShoppingItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateShoppingItem(shoppingItem)
        }
    }

    fun updateShoppingDescriptionById(id: Int, descr: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateShoppingDescriptionById(id, descr)
        }
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteShoppingItem(shoppingItem)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun deleteShoppingListWithId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteShoppingListWithId(id)
        }
    }

}