package com.ustabrothers.recortoandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ustabrothers.recortoandroid.HastaBottomNavigation.EventAdapterHasta;
import com.ustabrothers.recortoandroid.HastaBottomNavigation.Model.MyEvents;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private ListView eventListView;

    private DatabaseReference databaseReference;
    private String currentUserId, selectedDateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        eventListView = this.findViewById(R.id.eventListView);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Events").child(currentUserId);
        listMyEvents();
    }



    private void listMyEvents() {
        databaseReference.orderByChild("eventName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<MyEvents> myEventsList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MyEvents myEvent = snapshot.getValue(MyEvents.class);
                    if (myEvent != null) {
                        myEventsList.add(myEvent);
                    }
                }

                EventAdapterHasta adapter = new EventAdapterHasta(AdminActivity.this, myEventsList);
                eventListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Hata durumunu işle
            }
        });
    }
}