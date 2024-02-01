package com.ustabrothers.recortoandroid.DoktorBottomNavigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ustabrothers.recortoandroid.DoktorBottomNavigation.Model.MyEvent;
import com.ustabrothers.recortoandroid.HastaBottomNavigation.Model.MyEvents;
import com.ustabrothers.recortoandroid.R;


import java.util.ArrayList;
import java.util.List;

public class Tab4Fragment extends Fragment {

    private Button addButton;
    private ListView eventListView;

    private DatabaseReference databaseReference;
    private String currentUserId;

    public Tab4Fragment() {
        // Boş yapıcı metot gereklidir.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab4, container, false);

        addButton = rootView.findViewById(R.id.addBtn);
        eventListView = rootView.findViewById(R.id.eventListView);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("events").child(currentUserId);

        // Kaydedilen etkinlikleri listele
        listEvents();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddEventPopup();
            }
        });

        // Firebase'den etkinlikleri dinle
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listEvents();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Hata durumunu işle
            }
        });

        return rootView;
    }

    private void showAddEventPopup() {
        // Düzenleme popup'ını açmak için kullanılan kodu ekleyin
    }

    private void listEvents() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<MyEvent> events = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MyEvent myEvent = snapshot.getValue(MyEvent.class);

                    // Eğer myEvent null değilse (veri dönüşümü başarılıysa)
                    if (myEvent != null) {
                        events.add(myEvent);
                    }
                }

                EventAdapterDoctor adapter = new EventAdapterDoctor(getActivity(), events);
                eventListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Hata durumunu işle
            }
        });
    }

}
