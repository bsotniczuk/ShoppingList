package pl.bsotniczuk.shoppinglist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pl.bsotniczuk.shoppinglist.R;
import pl.bsotniczuk.shoppinglist.data.model.GroceryItem;

public class AdapterRecyclerGrocery extends RecyclerView.Adapter<AdapterRecyclerGrocery.MyViewHolder> {

    private Context mContext;
    private List<GroceryItem> messageModelList;
    private OnMessageClickListener onMessageClickListener;

    public AdapterRecyclerGrocery(Context mContext, List<GroceryItem> messageModelList, OnMessageClickListener onMessageClickListener) {
        this.mContext = mContext;
        this.messageModelList = messageModelList;
        this.onMessageClickListener = onMessageClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        v = layoutInflater.inflate(R.layout.grocery_list_item, parent, false);
        //v = layoutInflater.inflate(R.layout.grocery_list_item, parent, false);

        return new MyViewHolder(v, onMessageClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.textView1.setText(messageModelList.get(position).getProduct_name());
        holder.textView2.setText("X" + messageModelList.get(position).getQuantity());
        holder.imageView.setImageResource(R.drawable.ic_baseline_shopping_basket_24);

        if (messageModelList.get(position).getDone() == false) {
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
        return messageModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView1; //title
        TextView textView2;
        ImageView imageView; //image
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