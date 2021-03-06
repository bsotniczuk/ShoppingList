package pl.bsotniczuk.shoppinglist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import java.util.Date;
import java.util.Objects;

import pl.bsotniczuk.shoppinglist.data.GroceryDatabase;
import pl.bsotniczuk.shoppinglist.data.model.ShoppingItem;
import pl.bsotniczuk.shoppinglist.data.viewmodel.ShoppingViewModel;
import pl.bsotniczuk.shoppinglist.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    public static int groceriesActivityOpened = 0; //not elegant but works perfectly, now user cannot open three GroceriesActivities at once
    private AlertDialog alertToShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        Objects.requireNonNull(tabs.getTabAt(0)).setIcon(R.drawable.ic_baseline_list_24);
        Objects.requireNonNull(tabs.getTabAt(1)).setIcon(R.drawable.ic_baseline_archive_24);

        GroceryDatabase db = Room.databaseBuilder(getApplicationContext(),
                GroceryDatabase.class, "app_database").build();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpTextBox(getString(R.string.new_shopping_list), getString(R.string.message_add_shopping_list), getString(R.string.save), getString(R.string.cancel));
            }
        });
    }

    private void insertDataToDatabaseShoppingItem(String name, boolean isArchived) {
        ShoppingViewModel shoppingViewModel = new ViewModelProvider(this).get(ShoppingViewModel.class);
        Date date = new Date();
        ShoppingItem shoppingItem = new ShoppingItem(0, name, getString(R.string.no_groceries_added), isArchived, date);
        shoppingViewModel.addShoppingItem(shoppingItem);
    }

    private void popUpTextBox(String title, String message, String positiveButtonText, String negativeButtonText) {
        if (alertToShow != null && alertToShow.isShowing()) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                if (name.compareToIgnoreCase("") == 0 || name.length() > 30)
                    insertDataToDatabaseShoppingItem(getString(R.string.default_shopping_list_name), false);
                else insertDataToDatabaseShoppingItem(name, false);
                Intent intent = new Intent(getApplicationContext(), GroceriesActivity.class);
                intent.putExtra("id_in_db", -1);
                startActivity(intent);
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