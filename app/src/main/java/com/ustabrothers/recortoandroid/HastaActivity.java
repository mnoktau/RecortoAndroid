package com.ustabrothers.recortoandroid;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import com.ustabrothers.recortoandroid.DoktorBottomNavigation.Tab1Fragment;
import com.ustabrothers.recortoandroid.DoktorBottomNavigation.Tab2Fragment;
import com.ustabrothers.recortoandroid.DoktorBottomNavigation.Tab3Fragment;
import com.ustabrothers.recortoandroid.DoktorBottomNavigation.Tab4Fragment;
import com.ustabrothers.recortoandroid.DoktorBottomNavigation.Tab5Fragment;
import com.ustabrothers.recortoandroid.HastaBottomNavigation.Tab1FragmentHasta;
import com.ustabrothers.recortoandroid.HastaBottomNavigation.Tab2FragmentHasta;
import com.ustabrothers.recortoandroid.HastaBottomNavigation.Tab3FragmentHasta;
import com.ustabrothers.recortoandroid.HastaBottomNavigation.Tab4FragmentHasta;
import com.ustabrothers.recortoandroid.HastaBottomNavigation.Tab5FragmentHasta;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;

public class HastaActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasta);

        bottomNavigation = findViewById(R.id.bottom_navigation2);

        bottomNavigation.setOnNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.navigation_tab6:
                        selectedFragment = new Tab1FragmentHasta();
                        break;
                    case R.id.navigation_tab7:
                        selectedFragment = new Tab2FragmentHasta();
                        break;
                    case R.id.navigation_tab8:
                        selectedFragment = new Tab3FragmentHasta();
                        break;
                    case R.id.navigation_tab9:
                        selectedFragment = new Tab4FragmentHasta();
                        break;
                    case R.id.navigation_tab10:
                        selectedFragment = new Tab5FragmentHasta();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            }
        });

        // Başlangıç olarak ilk sekme seçili olsun
        bottomNavigation.setSelectedItemId(R.id.navigation_tab1);
    }
}
