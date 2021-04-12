package pl.bsotniczuk.shoppinglist.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListFragment extends Fragment implements AdapterRecyclerShopping.OnMessageClickListener {

    RecyclerView recyclerView;
    AdapterRecyclerShopping adapter;
    List<ShoppingItem> shoppingList;

    private ShoppingViewModel shoppingViewModel;
    AdapterRecyclerShopping.OnMessageClickListener onMessageClickListener;
    static String tag = "ShoppingApp";

    public ShoppingListFragment() {
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

        readDataFromDatabaseNotArchivedSortByDate();

        return view;
    }

    @Override
    public void onMessageClick(int position) {
        Log.i(tag, "position: " + position + " | clicked | id: " + shoppingList.get(position).getId() + " | time: " + shoppingList.get(position).getDate().getTime() + " | name: " + shoppingList.get(position).getShopping_list_name());
        Intent intent = new Intent(getActivity().getApplicationContext(), GroceriesActivity.class);
        intent.putExtra("id_in_db", shoppingList.get(position).getId());
        startActivity(intent);
    }

    private void readDataFromDatabaseNotArchivedSortByDate() {
        shoppingViewModel = new ViewModelProvider(this).get(ShoppingViewModel.class);

        final Observer<List<ShoppingItem>> groceryObserver2 = new Observer<List<ShoppingItem>>() {
            @Override
            public void onChanged(List<ShoppingItem> shoppingItems) {
                shoppingList = shoppingItems;
                populateRecyclerView(getContext(), shoppingItems, onMessageClickListener);
            }
        };
        shoppingViewModel.getReadAllNotArchivedSortByDate().observe(getViewLifecycleOwner(), groceryObserver2);
    }

    private void populateRecyclerView(Context context, List<ShoppingItem> shoppingItems, AdapterRecyclerShopping.OnMessageClickListener onMessageClickListener) {
        adapter = new AdapterRecyclerShopping(context, shoppingItems, onMessageClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }
}