package com.ustabrothers.recortoandroid;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import com.ustabrothers.recortoandroid.DoktorBottomNavigation.Tab1Fragment;
import com.ustabrothers.recortoandroid.DoktorBottomNavigation.Tab2Fragment;
import com.ustabrothers.recortoandroid.DoktorBottomNavigation.Tab3Fragment;
import com.ustabrothers.recortoandroid.DoktorBottomNavigation.Tab4Fragment;
import com.ustabrothers.recortoandroid.DoktorBottomNavigation.Tab5Fragment;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;

public class DoktorActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doktor);

        bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Context context = getApplicationContext();
                for (int i = 0; i < bottomNavigation.getMenu().size(); i++) {
                    MenuItem menuItem = bottomNavigation.getMenu().getItem(i);
                    Drawable icon = menuItem.getIcon();
                    if (icon != null) {
                        icon.setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_IN);
                    }
                }

                Drawable selectedIcon = item.getIcon();
                if (selectedIcon != null) {
                    selectedIcon.setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_IN);
                }


                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.navigation_tab1:
                        selectedFragment = new Tab1Fragment();
                        break;
                    case R.id.navigation_tab2:
                        selectedFragment = new Tab2Fragment();
                        break;
                    case R.id.navigation_tab3:
                        selectedFragment = new Tab3Fragment();
                        break;
                    case R.id.navigation_tab4:
                        selectedFragment = new Tab4Fragment();
                        break;
                    case R.id.navigation_tab5:
                        selectedFragment = new Tab5Fragment();
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
