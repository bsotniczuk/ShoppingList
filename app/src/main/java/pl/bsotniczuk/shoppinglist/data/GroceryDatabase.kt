package pl.bsotniczuk.shoppinglist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.bsotniczuk.shoppinglist.data.dao.GroceryDao
import pl.bsotniczuk.shoppinglist.data.dao.ShoppingDao
import pl.bsotniczuk.shoppinglist.data.model.GroceryItem
import pl.bsotniczuk.shoppinglist.data.model.ShoppingItem

//@Database(entities = arrayOf(GroceryItem::class)/*entities = [GroceryItem::class]*/, version = 1, exportSchema = false)
//@Database(entities = { arrayOf(GroceryItem::class), arrayOf(ShoppingItem::class) }, version = 1, exportSchema = false)
@Database(entities = [GroceryItem ::class, ShoppingItem::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class GroceryDatabase : RoomDatabase() {

    abstract fun groceryDao(): GroceryDao
    abstract fun shoppingDao(): ShoppingDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: GroceryDatabase? = null

        fun getDatabase(context: Context): GroceryDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        GroceryDatabase::class.java,
                        "app_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}