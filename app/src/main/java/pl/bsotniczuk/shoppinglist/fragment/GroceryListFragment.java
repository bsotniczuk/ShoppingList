package pl.bsotniczuk.shoppinglist.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.bsotniczuk.shoppinglist.R;
import pl.bsotniczuk.shoppinglist.adapter.AdapterRecyclerGrocery;
import pl.bsotniczuk.shoppinglist.adapter.AdapterRecyclerShopping;
import pl.bsotniczuk.shoppinglist.data.model.GroceryItem;
import pl.bsotniczuk.shoppinglist.data.model.ShoppingItem;
import pl.bsotniczuk.shoppinglist.data.viewmodel.GroceryViewModel;
import pl.bsotniczuk.shoppinglist.data.viewmodel.ShoppingViewModel;

public class GroceryListFragment extends Fragment implements AdapterRecyclerGrocery.OnMessageClickListener {

    RecyclerView recyclerView;
    AdapterRecyclerGrocery adapter;
    List<GroceryItem> groceryList;
    //List<ShoppingItem> shoppingItems;

    private int idInDb;

    private GroceryViewModel groceryViewModel;
    AdapterRecyclerGrocery.OnMessageClickListener onMessageClickListener;

    static String tag = "ShoppingApp"; //to delete

    public GroceryListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        groceryList = new ArrayList<>();
        this.onMessageClickListener = this;
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView1);
        adapter = new AdapterRecyclerGrocery(view.getContext(), groceryList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);

        idInDb = getActivity().getIntent().getIntExtra("id_in_db", -1);

        if (idInDb > 0)
            readGroceriesByShoppingIdSortByDate(idInDb);
        else { //where element is created
            readShoppingItemNotArchivedSortByDate();
        }
        return view;
    }

    @Override
    public void onMessageClick(int position) {
        popUpTextBox(position, "Edit grocery", "", "Edit", "Cancel");
    }

    private void readShoppingItemNotArchivedSortByDate() {
        ShoppingViewModel shoppingViewModel = new ViewModelProvider(this).get(ShoppingViewModel.class);
        final Observer<List<ShoppingItem>> groceryObserver4 = new Observer<List<ShoppingItem>>() {
            @Override
            public void onChanged(List<ShoppingItem> shoppingItems) {
                Log.i("ShoppingList", "???Observer: item changed, got id: " + shoppingItems.get(0).getId());
                idInDb = shoppingItems.get(0).getId();
                readGroceriesByShoppingIdSortByDate(idInDb);
            }
        };
        shoppingViewModel.getReadAllNotArchivedSortByDate().observe(getViewLifecycleOwner(), groceryObserver4);
    }

    private void readGroceriesByShoppingIdSortByDate(int id) {
        groceryViewModel = new ViewModelProvider(this).get(GroceryViewModel.class);

        //observer can be easily mounted on the beginning of the app, because it will log all the changes
        final Observer<List<GroceryItem>> groceryObserver5 = new Observer<List<GroceryItem>>() {
            @Override
            public void onChanged(List<GroceryItem> groceryItems) {
                groceryList = groceryItems; //assign to another list
                populateRecyclerView(getContext(), groceryItems, onMessageClickListener);
            }
        };
        groceryViewModel.readAllGroceryForShoppingId(id).observe(getViewLifecycleOwner(), groceryObserver5);
    }

    private void populateRecyclerView(Context context, List<GroceryItem> groceryItems, AdapterRecyclerGrocery.OnMessageClickListener onMessageClickListener) {
        adapter = new AdapterRecyclerGrocery(context, groceryItems, onMessageClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    private void updateGroceryItem(int id, String nameOfGrocery, int quantity, boolean isDone, int idOfShoppingItem) {
        GroceryViewModel groceryViewModel = new ViewModelProvider(this).get(GroceryViewModel.class);
        Date date = new Date();
        GroceryItem groceryItem = new GroceryItem(id, nameOfGrocery, quantity, isDone, date, idOfShoppingItem);
        groceryViewModel.updateGrocery(groceryItem);
    }

    public void popUpTextBox(int position, String title, String message, String positiveButtonText, String negativeButtonText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText input_name = new EditText(getContext());
        input_name.setHint("Grocery Name");
        input_name.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(input_name); // Notice this is an add method

        final EditText input_quantity = new EditText(getContext());
        input_quantity.setHint("Quantity");
        input_quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(input_quantity); // Another add method

        builder.setView(layout);

        builder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int quantity;
                String name;

                if (input_quantity.getText().toString().compareToIgnoreCase("") == 0)
                    quantity = 0;
                else
                    quantity = Integer.parseInt(input_quantity.getText().toString());
                if (input_name.getText().toString().compareToIgnoreCase("") == 0)
                    name = "Some grocery";
                else
                    name = input_name.getText().toString();

                updateGroceryItem(groceryList.get(position).getId(), name, quantity, false, groceryList.get(position).getId_of_shopping_list_item());
            }
        });
        builder.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        input_name.setText(groceryList.get(position).getProduct_name());
        input_quantity.setText("" + groceryList.get(position).getQuantity());

        //automatically popping up keyboard
        AlertDialog alertToShow = builder.create();
        alertToShow.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alertToShow.show();
    }
}