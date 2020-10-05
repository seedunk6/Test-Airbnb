package com.nextdots.airbnb.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nextdots.airbnb.R;
import com.nextdots.airbnb.models.Favorite;
import java.util.List;

/**
 * Created by Mariexi on 06/12/16.
 */
public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ListViewHolder>{
    List<Favorite> list;
    Context context;

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView type;
        TextView price;

        ListViewHolder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.image);
            name = (TextView)itemView.findViewById(R.id.name);
            type = (TextView)itemView.findViewById(R.id.type);
            price = (TextView)itemView.findViewById(R.id.price);
        }
    }

    public FavoritesAdapter(List<Favorite> list){
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_lodgings, viewGroup, false);
        ListViewHolder lvh = new ListViewHolder(v);
        context = viewGroup.getContext();
        return lvh;
    }


    @Override
    public void onBindViewHolder(final ListViewHolder holder, int i) {
        Glide.with(context)
                .load(list.get(i).getPicture_url())
                .into(holder.image);

        holder.name.setText(list.get(i).getName());
        holder.type.setText(list.get(i).getProperty_type());
        holder.price.setText(list.get(i).getPrice()+" "+list.get(i).getNative_currency());


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public Favorite getItemPosition(int position){
        return list.get(position);
    }
}
