package com.nextdots.airbnb.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.nextdots.airbnb.R;
import com.nextdots.airbnb.models.ResHotel;
import com.nextdots.airbnb.models.SearchResults;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Mariexi on 04/12/2016.
 */
public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ListViewHolder>{
    ArrayList<SearchResults> list;
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

    public ListingAdapter(ArrayList<SearchResults> list){
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
                .load(list.get(i).getListing().getPicture_url())
                .into(holder.image);

        holder.name.setText(list.get(i).getListing().getName());
        holder.type.setText(list.get(i).getListing().getProperty_type());
        holder.price.setText(list.get(i).getPricing_quote().getNightly_price()+" "+list.get(i).getPricing_quote().getListing_currency());


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public SearchResults getItemPosition(int position){
        return list.get(position);
    }
}
