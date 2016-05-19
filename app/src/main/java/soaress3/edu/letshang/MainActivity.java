package soaress3.edu.letshang;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.multidex.MultiDex;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private Firebase fbRef;
    private String uid;
    private SupportMapFragment sMapFragment;
    private CreateEventFragment createEventFragment;
    private ChangeProfileFragment changeProfileFragment;
    private FragmentManager sFm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);

        fbRef = new Firebase(Constants.FIREBASE_URL);

        fbRef.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData == null) {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                } else {
                    uid = authData.getUid();
                }
            }
        });

        if (sMapFragment == null){
            sMapFragment = sMapFragment.newInstance();
            sMapFragment.setRetainInstance(true);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_map);

        android.support.v4.app.FragmentManager sFm = getSupportFragmentManager();

        if (savedInstanceState == null){
            sFm.beginTransaction().replace(R.id.map, sMapFragment).commit();
        }

        sMapFragment.getMapAsync(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        sFm = getSupportFragmentManager();


        if (sMapFragment.isAdded() && id != R.id.nav_map){
            sFm.beginTransaction().hide(sMapFragment).commit();
        }

        if (id == R.id.nav_map) {
            if (!sMapFragment.isAdded()) {
                sFm.beginTransaction().add(R.id.map, sMapFragment).commit();
            } else {
                sFm.beginTransaction().show(sMapFragment).commit();
            }

        } else if (id == R.id.nav_create_event) {
            if (createEventFragment == null){
                createEventFragment = new CreateEventFragment();
            }

            sFm.beginTransaction().replace(R.id.content_frame, createEventFragment).commit();

        } else if (id == R.id.nav_logout) {
            fbRef.unauth();
        } else if(id == R.id.nav_profile) {
            if (changeProfileFragment == null){
                changeProfileFragment = new ChangeProfileFragment();
            }

            sFm.beginTransaction().replace(R.id.content_frame, changeProfileFragment, "Profile").commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng scranton = new LatLng(41.4090, -75.6624);
        googleMap.addMarker(new MarkerOptions().position(scranton).title("Marker in Scranton"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(scranton, 13));
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        ((DatePickerFragment) newFragment).setContext("profile");
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public FragmentManager getsFm() {
        return sFm;
    }
}
