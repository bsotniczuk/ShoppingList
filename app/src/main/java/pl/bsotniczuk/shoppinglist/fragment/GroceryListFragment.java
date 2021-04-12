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
import pl.bsotniczuk.shoppinglist.data.model.GroceryItem;
import pl.bsotniczuk.shoppinglist.data.model.ShoppingItem;
import pl.bsotniczuk.shoppinglist.data.viewmodel.GroceryViewModel;
import pl.bsotniczuk.shoppinglist.data.viewmodel.ShoppingViewModel;

public class GroceryListFragment extends Fragment implements AdapterRecyclerGrocery.OnMessageClickListener {

    RecyclerView recyclerView;
    AdapterRecyclerGrocery adapter;
    List<GroceryItem> groceryList;

    private int idInDb;

    AdapterRecyclerGrocery.OnMessageClickListener onMessageClickListener;
    private AlertDialog alertToShow;

    public GroceryListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        groceryList = new ArrayList<>();
        this.onMessageClickListener = this;

        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView1);
        adapter = new AdapterRecyclerGrocery(view.getContext(), groceryList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);

        idInDb = getActivity().getIntent().getIntExtra("id_in_db", -1);

        if (idInDb > 0)
            readGroceriesByShoppingIdSortByDate(idInDb);
        else //adding new shopping list
            readShoppingItemNotArchivedSortByDate();
        return view;
    }

    @Override
    public void onMessageClick(int position, boolean isButton) {
        if (!isButton)
            popUpTextBox(position, getString(R.string.edit_grocery), getString(R.string.edit), getString(R.string.cancel));
        else {
            updateGroceryItem(groceryList.get(position).getId(), !groceryList.get(position).getDone());
            readAllGroceryForShoppingIdAndUpdateDescription(idInDb);
            Log.i("ShoppingList", "done in id: " + groceryList.get(position).getId() + " | done: " + groceryList.get(position).getDone());
        }
    }

    private void updateGroceryItem(int id, boolean isDone) {
        GroceryViewModel groceryViewModel = new ViewModelProvider(this).get(GroceryViewModel.class);
        groceryViewModel.updateGroceryDoneById(id, isDone);
    }

    private void updateGroceryItem(int id, String nameOfGrocery, int quantity, boolean isDone, int idOfShoppingItem) {
        GroceryViewModel groceryViewModel = new ViewModelProvider(this).get(GroceryViewModel.class);
        Date date = new Date();
        GroceryItem groceryItem = new GroceryItem(id, nameOfGrocery, quantity, isDone, date, idOfShoppingItem);
        groceryViewModel.updateGrocery(groceryItem);
    }

    private void readShoppingItemNotArchivedSortByDate() {
        ShoppingViewModel shoppingViewModel = new ViewModelProvider(this).get(ShoppingViewModel.class);
        final Observer<List<ShoppingItem>> groceryObserver4 = new Observer<List<ShoppingItem>>() {
            @Override
            public void onChanged(List<ShoppingItem> shoppingItems) {
                idInDb = shoppingItems.get(0).getId();
                readGroceriesByShoppingIdSortByDate(idInDb);
            }
        };
        shoppingViewModel.getReadAllNotArchivedSortByDate().observe(getViewLifecycleOwner(), groceryObserver4);
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

    private void readGroceriesByShoppingIdSortByDate(int id) {
        GroceryViewModel groceryViewModel = new ViewModelProvider(this).get(GroceryViewModel.class);
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

    private void popUpTextBox(int position, String title, String positiveButtonText, String negativeButtonText) {
        if (alertToShow != null && alertToShow.isShowing()) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText input_name = new EditText(getContext());
        input_name.setHint("Grocery Name");
        input_name.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(input_name);

        final EditText input_quantity = new EditText(getContext());
        input_quantity.setHint("Quantity");
        input_quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(input_quantity);

        builder.setView(layout);
        input_name.setText(groceryList.get(position).getProduct_name());
        input_quantity.setText("" + groceryList.get(position).getQuantity());

        builder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                positiveButtonAction(position, input_name.getText().toString(), input_quantity.getText().toString());
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

    private void positiveButtonAction(int position, String name_input, String quantity_input) {
        int quantity;
        String name;

        try {
            quantity = Integer.parseInt(quantity_input);
        } catch (Exception e) {
            quantity = groceryList.get(position).getQuantity();
        }
        if (quantity < 1) quantity = 1;
        if (name_input.compareToIgnoreCase("") == 0) name = getString(R.string.default_grocery_name);
        else if (name_input.length() > 30)  name = groceryList.get(position).getProduct_name();
        else name = name_input;

        updateGroceryItem(groceryList.get(position).getId(), name, quantity, groceryList.get(position).getDone(), groceryList.get(position).getId_of_shopping_list_item());
    }
}