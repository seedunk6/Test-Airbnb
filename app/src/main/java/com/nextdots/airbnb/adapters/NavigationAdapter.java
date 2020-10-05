package com.nextdots.airbnb.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nextdots.airbnb.R;
import com.nextdots.airbnb.models.Item;

import java.util.ArrayList;

/**
 * Created by Mariexi on 03/12/2016.
 */
public class NavigationAdapter extends BaseAdapter {
    private Activity activity;
    ArrayList<Item> arrayitms;

    public NavigationAdapter(Activity activity, ArrayList<Item> listarry) {
        this.activity = activity;
        this.arrayitms = listarry;
    }

    @Override
    public Object getItem(int position) {
        return arrayitms.get(position);
    }

    @Override
    public int getCount() {
        return arrayitms.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class Fila{
        ImageView icono;
        TextView titulo_itm;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Fila view;
        LayoutInflater inflator = activity.getLayoutInflater();

        if (convertView == null){
            view = new Fila();
            Item itm=arrayitms.get(position);
            convertView = inflator.inflate(R.layout.item, null);

            view.titulo_itm = (TextView) convertView.findViewById(R.id.title_item);
            view.titulo_itm.setText(itm.getTitulo());

            view.icono = (ImageView) convertView.findViewById(R.id.icon);
            view.icono.setImageResource(itm.getIcono());
        }
        else{
            view = (Fila) convertView.getTag();
        }
        return convertView;
    }
}

