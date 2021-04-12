package pl.bsotniczuk.shoppinglist;

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import pl.bsotniczuk.shoppinglist.data.GroceryDatabase
import pl.bsotniczuk.shoppinglist.data.dao.GroceryDao
import pl.bsotniczuk.shoppinglist.data.model.GroceryItem
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest {
    private lateinit var groceryDao: GroceryDao
    private lateinit var db: GroceryDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
                context, GroceryDatabase::class.java).build()
        groceryDao = db.groceryDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {

        val grocery: GroceryItem = TestUtil.createGrocery("Some grocery x1 .,")
        groceryDao.addGrocery(grocery)
        val byName = groceryDao.findGroceryByName("Some grocery x1 .,")
        assertThat(byName.product_name, equalTo(grocery.product_name))
    }
}

object TestUtil {

    private fun getDate(): Date {
        return Date()
    }

    fun createGrocery(name: String) = GroceryItem(
            id = 1,
            product_name = name,
            quantity = 2,
            done = true,
            id_of_shopping_list_item = 1,
            date = getDate()
    )

}
