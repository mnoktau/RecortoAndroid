package com.ustabrothers.recortoandroid.HastaBottomNavigation;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ustabrothers.recortoandroid.R;

import java.util.ArrayList;
import java.util.List;

public class Tab2FragmentHasta extends Fragment {

    private AppCompatEditText searchEditText;
    private ListView searchResultsListView;
    private ArrayAdapter<String> adapter;

    private DatabaseReference databaseReference;

    public Tab2FragmentHasta() {
        // Boş yapıcı metot gereklidir.
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Doctors");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab7, container, false);

        searchEditText = view.findViewById(R.id.searchDoctors);
        searchResultsListView = view.findViewById(R.id.doctorsList);
        // Fragment'ın görünümünü oluşturun veya şişirin ve döndürün.
        adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1);
        searchResultsListView.setAdapter(adapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = s.toString();

                // Realtime Database'den doktorları sorgula ve sonuçları ListView'e yükle
                databaseReference.child("Names")
                        .orderByKey()
                        .startAt(searchText)
                        .endAt(searchText + "\uf8ff")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                List<String> doctors = new ArrayList<>();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String doctorName = snapshot.getValue(String.class);
                                    doctors.add(doctorName);
                                }

                                adapter.clear();
                                adapter.addAll(doctors);
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Sorgu iptal edildi
                            }
                        });
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }
}
