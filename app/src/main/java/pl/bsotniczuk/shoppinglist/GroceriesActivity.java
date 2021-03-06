package pl.bsotniczuk.shoppinglist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import pl.bsotniczuk.shoppinglist.data.model.GroceryItem;
import pl.bsotniczuk.shoppinglist.data.model.ShoppingItem;
import pl.bsotniczuk.shoppinglist.data.viewmodel.GroceryViewModel;
import pl.bsotniczuk.shoppinglist.data.viewmodel.ShoppingViewModel;
import pl.bsotniczuk.shoppinglist.fragment.GroceryListFragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

public class GroceriesActivity extends AppCompatActivity {

    private int idInDb;
    List<ShoppingItem> shoppingList;
    private AlertDialog alertToShow;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groceries);

        MainActivity.groceriesActivityOpened++;
        Log.i("ShoppingList", "activities opened count: " + MainActivity.groceriesActivityOpened);

        if (MainActivity.groceriesActivityOpened > 1)
            finish(); //ensure that only one GroceryActivity is opened

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_keyboard_backspace_24));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPressAction();
            }
        });
        idInDb = getIntent().getIntExtra("id_in_db", -1);

        readDataFromDatabaseNotArchivedSortByDate(); //read shopping lists, to get id for newly added shopping list

        FloatingActionButton fab = findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ensureNotNullShoppingItem();
                popUpTextBox(getString(R.string.add_new_grocery), getString(R.string.add), getString(R.string.cancel));
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new GroceryListFragment()).commit();
    }

    @Override
    protected void onDestroy() {
        MainActivity.groceriesActivityOpened = 0;
        super.onDestroy();
    }

    private void ensureNotNullShoppingItem() {
        if (idInDb < 0)
            if (shoppingList.size() > 0)
                idInDb = shoppingList.get(0).getId();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.groceries_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        ensureNotNullShoppingItem();

        if (id == R.id.action_archive) {
            updateCurrentShoppingItemArchived(idInDb, true);
            backPressAction();
            return true;
        } else if (id == R.id.action_remove) {
            deleteCurrentShoppingItem(idInDb); //delete shopping item
            deleteAllGroceriesWithShoppingId(idInDb); //delete all groceries related with that shopping item
            backPressAction();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        backPressAction();
        //super.onBackPressed();
    }

    private void backPressAction() {
        MainActivity.groceriesActivityOpened = 0;
        super.onBackPressed();
    }

    private void insertGroceryItem(int idOfShoppingItem, String nameOfGrocery, boolean isDone, int quantity) {
        GroceryViewModel groceryViewModel = new ViewModelProvider(this).get(GroceryViewModel.class);
        Date date = new Date();
        GroceryItem groceryItem = new GroceryItem(0, nameOfGrocery, quantity, isDone, date, idOfShoppingItem);
        groceryViewModel.addGrocery(groceryItem);
        readAllGroceryForShoppingIdAndUpdateDescription(idOfShoppingItem);
    }

    private void updateCurrentShoppingItemArchived(int idOfShoppingItem, boolean isArchived) {
        ShoppingViewModel shoppingViewModel = new ViewModelProvider(this).get(ShoppingViewModel.class);
        shoppingViewModel.updateShoppingArchivedById(idOfShoppingItem, isArchived);
    }

    private void deleteCurrentShoppingItem(int idOfShoppingItem) {
        ShoppingViewModel shoppingViewModel = new ViewModelProvider(this).get(ShoppingViewModel.class);
        shoppingViewModel.deleteShoppingListWithId(idOfShoppingItem);
    }

    private void deleteAllGroceriesWithShoppingId(int idOfShoppingItem) {
        GroceryViewModel groceryViewModel = new ViewModelProvider(this).get(GroceryViewModel.class);
        groceryViewModel.deleteGroceriesWithShoppingId(idOfShoppingItem);
    }

    private void readDataFromDatabaseNotArchivedSortByDate() {
        ShoppingViewModel shoppingViewModel = new ViewModelProvider(this).get(ShoppingViewModel.class);
        final Observer<List<ShoppingItem>> shoppingObserver3 = new Observer<List<ShoppingItem>>() {
            @Override
            public void onChanged(List<ShoppingItem> shoppingItems) {
                shoppingList = shoppingItems;
                ensureNotNullShoppingItem();
            }
        };
        shoppingViewModel.getReadAllNotArchivedSortByDate().observe(this, shoppingObserver3);
    }

    private void readAllGroceryForShoppingIdAndUpdateDescription(int id) {
        GroceryViewModel groceryViewModel = new ViewModelProvider(this).get(GroceryViewModel.class);
        final Observer<List<GroceryItem>> groceryObserver3 = new Observer<List<GroceryItem>>() {
            @Override
            public void onChanged(List<GroceryItem> groceryItems) {
                int count = 0;
                for (GroceryItem a : groceryItems) {
                    if (a.getDone())
                        count++;
                }
                String description = count + "/" + groceryItems.size();
                updateShoppingDescription(id, description);
            }
        };
        groceryViewModel.readAllGroceryForShoppingId(id).observe(this, groceryObserver3);
    }

    private void updateShoppingDescription(int id, String descr) {
        String description = "Groceries done " + descr;
        ShoppingViewModel shoppingViewModel = new ViewModelProvider(this).get(ShoppingViewModel.class);
        shoppingViewModel.updateShoppingDescriptionById(id, description);
    }

    private void popUpTextBox(String title, String positiveButtonText, String negativeButtonText) {
        if (alertToShow != null && alertToShow.isShowing()) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText input_name = new EditText(this);
        input_name.setHint("Grocery Name");
        input_name.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(input_name);

        final EditText input_quantity = new EditText(this);
        input_quantity.setHint("Quantity");
        input_quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(input_quantity);

        builder.setView(layout);

        builder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int quantity;
                String name = input_name.getText().toString();

                try {
                    quantity = Integer.parseInt(input_quantity.getText().toString());
                } catch (Exception e) {
                    quantity = 1;
                }
                if (quantity < 1) quantity = 1;

                if (name.compareToIgnoreCase("") == 0 || name.length() > 30)
                    insertGroceryItem(idInDb, getString(R.string.default_grocery_name), false, quantity);
                else insertGroceryItem(idInDb, name, false, quantity);
            }
        });
        builder.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertToShow = builder.create();
        alertToShow.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alertToShow.show();
    }
}