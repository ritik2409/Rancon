package com.rancon;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MapActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        String cardId = getIntent().getStringExtra("cardId");
        Fragment mapFragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString("cardID", cardId);
        mapFragment.setArguments(args);
        FragmentManager fM = getSupportFragmentManager();
        FragmentTransaction fT = fM.beginTransaction().add(R.id.container,mapFragment);
        fT.commit();

    }
}
