package pl.bsotniczuk.shoppinglist.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.bsotniczuk.shoppinglist.data.GroceryDatabase
import pl.bsotniczuk.shoppinglist.data.model.GroceryItem
import pl.bsotniczuk.shoppinglist.data.repository.GroceryRepository

class GroceryViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<GroceryItem>>
    val readAllSortByDate: LiveData<List<GroceryItem>>
    private val repository: GroceryRepository

    init {
        val groceryDao = GroceryDatabase.getDatabase(application).groceryDao()
        repository = GroceryRepository(groceryDao)

        readAllData = repository.readAllData
        readAllSortByDate = repository.readAllSortByDate
    }

    fun addGrocery(groceryItem: GroceryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addGrocery(groceryItem)
        }
    }

    fun updateGrocery(groceryItem: GroceryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateGrocery(groceryItem)
        }
    }

    fun deleteGrocery(groceryItem: GroceryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteGrocery(groceryItem)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll();
        }
    }

    fun deleteGroceriesWithShoppingId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteGroceriesWithShoppingId(id)
        }
    }

    fun readAllGroceryForShoppingId(id: Int): LiveData<List<GroceryItem>> {
        return repository.readAllGroceryForShoppingId(id)
    }

    fun readAllGroceryForShoppingIdThatAreDone(id: Int): LiveData<List<GroceryItem>> {
        return repository.readAllGroceryForShoppingIdThatAreDone(id)
    }
}