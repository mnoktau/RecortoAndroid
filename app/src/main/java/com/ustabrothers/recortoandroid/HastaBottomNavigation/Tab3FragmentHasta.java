package com.ustabrothers.recortoandroid.HastaBottomNavigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ustabrothers.recortoandroid.R;

public class Tab3FragmentHasta extends Fragment {

    public Tab3FragmentHasta() {
        // Boş yapıcı metot gereklidir.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Fragment'ın görünümünü oluşturun veya şişirin ve döndürün.
        return inflater.inflate(R.layout.fragment_tab3, container, false);
    }
}
