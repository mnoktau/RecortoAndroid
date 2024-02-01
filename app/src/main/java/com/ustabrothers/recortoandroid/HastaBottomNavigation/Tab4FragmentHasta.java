package com.ustabrothers.recortoandroid.HastaBottomNavigation;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ustabrothers.recortoandroid.HastaBottomNavigation.Model.MyEvents;
import com.ustabrothers.recortoandroid.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Tab4FragmentHasta extends Fragment {

    private Button addButton;
    private ListView eventListView;

    private DatabaseReference databaseReference;
    private String currentUserId, selectedDateString;

    public Tab4FragmentHasta() {
        // Boş yapıcı metot gereklidir.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab9, container, false);

        addButton = rootView.findViewById(R.id.addBtnKayit);
        eventListView = rootView.findViewById(R.id.eventListView);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Events").child(currentUserId);
        listMyEvents();




        // Kaydedilen etkinlikleri listele


        addButton.setOnClickListener(view -> showAddEventPopup());

        // Firebase'den etkinlikleri dinle
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Hata durumunu işle
            }
        });

        return rootView;
    }


    // getMarkedDatesFromFirebase metodu
    private void getMarkedDatesFromFirebase(final DatePicker datePicker) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dateSnapshot : snapshot.getChildren()) {
                    String markedDate = dateSnapshot.getKey();
                    Calendar markedCalendar = Calendar.getInstance();
                    markedCalendar.setTime(parseStringToDate(markedDate));

                    // Firebase'den alınan işaretlenmiş tarihleri DatePicker üzerinde işaretle
                    markDatePicker(datePicker, markedCalendar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Hata durumunda yapılacak işlemler
            }
        });
    }

    // markDatePicker metodu
    private void markDatePicker(DatePicker datePicker, Calendar markedCalendar) {
        int year = markedCalendar.get(Calendar.YEAR);
        int month = markedCalendar.get(Calendar.MONTH);
        int dayOfMonth = markedCalendar.get(Calendar.DAY_OF_MONTH);

        try {
            // DatePicker'ın içindeki tarih görünümlerini bul
            ViewGroup ll = (ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0);
            for (int i = 0; i < ll.getChildCount(); i++) {
                LinearLayout ll2 = (LinearLayout) ll.getChildAt(i);
                for (int j = 0; j < ll2.getChildCount(); j++) {
                    LinearLayout ll3 = (LinearLayout) ll2.getChildAt(j);
                    for (int k = 0; k < ll3.getChildCount(); k++) {
                        View dpHeaderText = ll3.getChildAt(k);
                        if (dpHeaderText instanceof TextView) {
                            TextView tv = (TextView) dpHeaderText;
                            int day = Integer.parseInt(tv.getText().toString());
                            if (day == dayOfMonth && datePicker.getMonth() == month && datePicker.getYear() == year) {
                                // İşaretlenmiş tarihi burada işaretle
                                tv.setBackgroundColor(Color.parseColor("#ffcccb"));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Date parseStringToDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
            return new Date(); // Hata durumunda şu anki tarihi döndürür
        }
    }

    private void showAddEventPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Yeni Etkinlik Ekle");

        View view = getLayoutInflater().inflate(R.layout.popup_add_event, null);
        builder.setView(view);

        final EditText eventNameEditText = view.findViewById(R.id.editTextEventName);
        final Button btnPickDate = view.findViewById(R.id.btnPickDate);
        final TextView textViewSelectedDate = view.findViewById(R.id.textViewSelectedDate);

        final Calendar calendar = Calendar.getInstance();

        btnPickDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getActivity(),
                    (view1, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String selectedDate = dateFormat.format(calendar.getTime());
                        textViewSelectedDate.setText("Seçilen Tarih: " + selectedDate);
                        selectedDateString = dateFormat.format(calendar.getTime());
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            datePickerDialog.show();
        });

        builder.setPositiveButton("Ekle", (dialog, which) -> {
            String eventName = eventNameEditText.getText().toString().trim();

            if (!eventName.isEmpty()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String selectedDate = dateFormat.format(calendar.getTime());
                addEventToFirebase(eventName, selectedDate);
            } else {
                Toast.makeText(getActivity(), "Etkinlik adı boş olamaz", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("İptal", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void addEventToFirebase(String eventName, String eventDate) {
        String eventId = databaseReference.push().getKey(); // Benzersiz bir EventID oluştur
        MyEvents myEvent = new MyEvents(eventId, eventName, eventDate);

        if (eventId != null) {
            databaseReference.child(eventId).setValue(myEvent)
                    .addOnSuccessListener(aVoid -> Toast.makeText(getActivity(), "Etkinlik başarıyla eklendi", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(getActivity(), "Etkinlik eklenirken hata oluştu", Toast.LENGTH_SHORT).show());
        }
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

                EventAdapterHasta adapter = new EventAdapterHasta(getActivity(), myEventsList);
                eventListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Hata durumunu işle
            }
        });
    }

}
