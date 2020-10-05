package com.nextdots.airbnb.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.devs.acr.AutoErrorReporter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nextdots.airbnb.fragments.FavoritesFragment;
import com.nextdots.airbnb.fragments.HomeFragment;
import com.nextdots.airbnb.fragments.MapFragment;
import com.nextdots.airbnb.interfaces.MainActivityInterface;
import com.nextdots.airbnb.adapters.NavigationAdapter;
import com.nextdots.airbnb.R;
import com.nextdots.airbnb.utils.UserSessionManager;
import com.nextdots.airbnb.models.Item;
import com.orm.SugarContext;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Mariexi on 04/12/2016.
 */
public class MainActivity extends AppCompatActivity implements MainActivityInterface, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    UserSessionManager sesion;
    private View header;
    private TypedArray NavIcons;
    private String[] titulos;
    private ArrayList<Item> NavItms;
    private NavigationAdapter NavAdapter;
    private ActionBarDrawerToggle toggle;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.left_drawer) ListView drawerList;
    TextView user;
    CircleImageView perfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        sesion = new UserSessionManager(getApplicationContext());

        if(toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        header = getLayoutInflater().inflate(R.layout.header_menu, drawerList, false);

        user = (TextView) header.findViewById(R.id.user);
        perfil = (CircleImageView) header.findViewById(R.id.perfil);


        Log.d(TAG+" Mari","name ="+sesion.getNombre());
        Log.d(TAG+" Mari","photo ="+sesion.getPhotoPath());

        user.setText(sesion.getNombre());

        Glide.with(getApplicationContext())
                .load(sesion.getPhotoPath())
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .dontAnimate()
                .into(perfil);

        drawerList.addHeaderView(header, null, false);

        /**
         * Items del menú lateral
         */
        NavIcons = getResources().obtainTypedArray(R.array.navigation_iconos);
        titulos = getResources().getStringArray(R.array.nav_options);
        NavItms = new ArrayList<Item>();
        NavItms.add(new Item(titulos[0], NavIcons.getResourceId(0, -1)));
        NavItms.add(new Item(titulos[1], NavIcons.getResourceId(1, -1)));
        NavItms.add(new Item(titulos[2], NavIcons.getResourceId(2, -1)));
        NavItms.add(new Item(titulos[3], NavIcons.getResourceId(3, -1)));

        NavAdapter = new NavigationAdapter(this,NavItms);
        drawerList.setAdapter(NavAdapter);

        /**
         * Listener del menú lateral
         */
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Fragment fragment = null;
                String TAG = "";

                switch (position) {
                    case 1:
                        fragment = new HomeFragment();
                        TAG = HomeFragment.TAG;
                        break;
                    case 2:
                        fragment = new MapFragment();
                        TAG = MapFragment.TAG;
                        break;
                    case 3:
                        fragment = new FavoritesFragment();
                        TAG = FavoritesFragment.TAG;
                        break;
                    case 4:
                        sesion.logoutUser();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }

                if (fragment!=null){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, TAG).commit();

                    drawerList.setItemChecked(position, true);
                }

                drawerLayout.closeDrawer(drawerList);
            }
        });

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name) {

            public void onDrawerClosed(View view) {
                ActivityCompat.invalidateOptionsMenu(MainActivity.this);
            }

            public void onDrawerOpened(View drawerView) {
                ActivityCompat.invalidateOptionsMenu(MainActivity.this);
            }
        };

        drawerLayout.setDrawerListener(toggle);

        /**
         * Primera pantalla que muestra al iniciar sesión
         */
        if(savedInstanceState == null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            HomeFragment fragment = new HomeFragment();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment,fragment.TAG).commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void setTitulo(String titulo) {
        getSupportActionBar().setTitle(titulo);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



}
