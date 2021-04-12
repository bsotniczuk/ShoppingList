package pl.bsotniczuk.shoppinglist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pl.bsotniczuk.shoppinglist.R;
import pl.bsotniczuk.shoppinglist.data.model.ShoppingItem;

public class AdapterRecyclerShopping extends RecyclerView.Adapter<AdapterRecyclerShopping.MyViewHolder> {

    private Context mContext;
    private List<ShoppingItem> shoppingItems;
    private OnMessageClickListener onMessageClickListener;

    public AdapterRecyclerShopping(Context mContext, List<ShoppingItem> shoppingItems, OnMessageClickListener onMessageClickListener) {
        this.mContext = mContext;
        this.shoppingItems = shoppingItems;
        this.onMessageClickListener = onMessageClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        v = layoutInflater.inflate(R.layout.shopping_list_item, parent, false);
        //v = layoutInflater.inflate(R.layout.grocery_list_item, parent, false);

        return new MyViewHolder(v, onMessageClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.textView1.setText(shoppingItems.get(position).getShopping_list_name());
        holder.textView2.setText(shoppingItems.get(position).getDescription());
        holder.imageView.setImageResource(R.drawable.ic_baseline_shopping_cart_24);
    }

    @Override
    public int getItemCount() {
        return shoppingItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView1; //title
        TextView textView2;
        ImageView imageView; //image
        OnMessageClickListener onMessageClickListener;

        public MyViewHolder(@NonNull View itemView, OnMessageClickListener onMessageClickListener) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textViewRecycler1);
            textView2 = itemView.findViewById(R.id.textViewRecycler2);
            imageView = itemView.findViewById(R.id.imageViewRecycler);
            this.onMessageClickListener = onMessageClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onMessageClickListener.onMessageClick(getAdapterPosition());
        }
    }

    public interface OnMessageClickListener {
        void onMessageClick(int position);
    }
}