package com.nextdots.airbnb.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nextdots.airbnb.R;
import com.nextdots.airbnb.adapters.FavoritesAdapter;
import com.nextdots.airbnb.interfaces.MainActivityInterface;
import com.nextdots.airbnb.models.Favorite;
import com.nextdots.airbnb.utils.LocationManager;
import com.nextdots.airbnb.utils.RecyclerItemClickListener;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Mariexi on 04/12/2016.
 */
public class FavoritesFragment extends Fragment {
    public  static final  String TAG = FavoritesFragment.class.getSimpleName();
    MainActivityInterface mainActivityInterface;
    FragmentActivity myContext;
    RecyclerView list;
    private LocationManager locationManager;
    private FavoritesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View  view = inflater.inflate(R.layout.fragment_favorites, container, false);
        ButterKnife.bind(this, view);

        mainActivityInterface.setTitulo(getResources().getString(R.string.favorite));

        List<Favorite> favorites = Favorite.listAll(Favorite.class);

        list = (RecyclerView) view.findViewById(R.id.list);

        mLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        list.setLayoutManager(mLayoutManager);

        list.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Favorite item = mAdapter.getItemPosition(position);

                FragmentManager fragmentManager = myContext.getSupportFragmentManager();
                DetailsFragment fragment = DetailsFragment.newInstence(item);
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment, fragment.TAG)
                        .addToBackStack(fragment.TAG)
                        .commit();

            }
        }));



        mAdapter = new FavoritesAdapter(favorites);
        list.setAdapter(mAdapter);


        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
        try {
            mainActivityInterface = (MainActivityInterface) activity;
        } catch (ClassCastException e) {
            Log.e(TAG, activity.toString() + "must implement onCropListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivityInterface = null;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}

