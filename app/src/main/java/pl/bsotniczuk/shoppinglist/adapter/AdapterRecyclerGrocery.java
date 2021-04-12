package pl.bsotniczuk.shoppinglist.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;
import pl.bsotniczuk.shoppinglist.R;
import pl.bsotniczuk.shoppinglist.data.model.GroceryItem;
import pl.bsotniczuk.shoppinglist.data.viewmodel.GroceryViewModel;

public class AdapterRecyclerGrocery extends RecyclerView.Adapter<AdapterRecyclerGrocery.MyViewHolder> {

    private Context mContext;
    private List<GroceryItem> groceryItems;
    private OnMessageClickListener onMessageClickListener;

    public AdapterRecyclerGrocery(Context mContext, List<GroceryItem> groceryItems, OnMessageClickListener onMessageClickListener) {
        this.mContext = mContext;
        this.groceryItems = groceryItems;
        this.onMessageClickListener = onMessageClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        v = layoutInflater.inflate(R.layout.grocery_list_item, parent, false);

        return new MyViewHolder(v, onMessageClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.textView1.setText(groceryItems.get(position).getProduct_name());
        holder.textView2.setText("X" + groceryItems.get(position).getQuantity());
        holder.imageView.setImageResource(R.drawable.ic_baseline_shopping_basket_24);

        final GroceryItem groceryItem = groceryItems.get(position);

        //set button look
        if (groceryItems.get(position).getDone() == false) {
            holder.imageButton.setBackgroundResource(R.drawable.round_button);
            holder.imageButton.setImageResource(R.drawable.ic_baseline_done_24);
        }
        else {
            holder.imageButton.setBackgroundResource(R.drawable.round_button_green);
            holder.imageButton.setImageResource(R.drawable.ic_baseline_done_24_white);
        }
    }

    @Override
    public int getItemCount() {
        return groceryItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView1;
        TextView textView2;
        ImageView imageView;
        ImageButton imageButton;
        OnMessageClickListener onMessageClickListener;

        public MyViewHolder(@NonNull View itemView, OnMessageClickListener onMessageClickListener) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textViewRecycler11);
            textView2 = itemView.findViewById(R.id.textViewRecycler22);
            imageView = itemView.findViewById(R.id.imageViewRecycler);
            imageButton = itemView.findViewById(R.id.groceryImageButton);

            this.onMessageClickListener = onMessageClickListener;
            itemView.setOnClickListener(this);
            imageButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (view.getId() == imageButton.getId()) {
                onMessageClickListener.onMessageClick(getAdapterPosition(), true);
            }
            else onMessageClickListener.onMessageClick(getAdapterPosition(), false);
        }
    }

    public interface OnMessageClickListener {
        void onMessageClick(int position, boolean isButton);
    }
}