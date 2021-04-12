package pl.bsotniczuk.shoppinglist.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.bsotniczuk.shoppinglist.GroceriesActivity;
import pl.bsotniczuk.shoppinglist.R;
import pl.bsotniczuk.shoppinglist.adapter.AdapterRecyclerShopping;
import pl.bsotniczuk.shoppinglist.data.model.ShoppingItem;
import pl.bsotniczuk.shoppinglist.data.viewmodel.ShoppingViewModel;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListArchivedFragment extends Fragment implements AdapterRecyclerShopping.OnMessageClickListener {

    RecyclerView recyclerView;
    AdapterRecyclerShopping adapter;
    List<ShoppingItem> shoppingList;

    AdapterRecyclerShopping.OnMessageClickListener onMessageClickListener;
    private AlertDialog alertToShow;

    public ShoppingListArchivedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        shoppingList = new ArrayList<>();
        this.onMessageClickListener = this;

        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView1);
        adapter = new AdapterRecyclerShopping(view.getContext(), shoppingList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);

        readDataFromDatabaseArchivedSortByDate();

        return view;
    }

    @Override
    public void onMessageClick(int position) {
        popUpTextBox(position, getString(R.string.unarchive), getString(R.string.to_unarchive), getString(R.string.yes), getString(R.string.no));
    }

    private void readDataFromDatabaseArchivedSortByDate() {
        ShoppingViewModel shoppingViewModel = new ViewModelProvider(this).get(ShoppingViewModel.class);

        final Observer<List<ShoppingItem>> groceryObserver2 = new Observer<List<ShoppingItem>>() {
            @Override
            public void onChanged(List<ShoppingItem> shoppingItems) {
                shoppingList = shoppingItems;
                populateRecyclerView(getContext(), shoppingItems, onMessageClickListener);
            }
        };
        shoppingViewModel.getReadAllArchivedSortByDate().observe(getViewLifecycleOwner(), groceryObserver2);
    }

    private void populateRecyclerView(Context context, List<ShoppingItem> shoppingItems, AdapterRecyclerShopping.OnMessageClickListener onMessageClickListener) {
        adapter = new AdapterRecyclerShopping(context, shoppingItems, onMessageClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    private void updateCurrentShoppingItemArchived(int idOfShoppingItem, boolean isArchived) {
        ShoppingViewModel shoppingViewModel = new ViewModelProvider(this).get(ShoppingViewModel.class);
        shoppingViewModel.updateShoppingArchivedById(idOfShoppingItem, isArchived);
    }

    private void popUpTextBox(int position, String title, String message, String positiveButtonText, String negativeButtonText) {
        if (alertToShow != null && alertToShow.isShowing()) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateCurrentShoppingItemArchived(shoppingList.get(position).getId(), false);
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