package com.ustabrothers.recortoandroid.HastaBottomNavigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ustabrothers.recortoandroid.DoktorBottomNavigation.EditProfileFragment;
import com.ustabrothers.recortoandroid.R;

public class Tab5FragmentHasta extends Fragment {

    private Button editProfileButton;
    private TextView nameTextView, ageTextView, locationTextView, phoneTextView;

    private DatabaseReference databaseReference;
    private String currentUserId;

    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab10, container, false);

        editProfileButton = rootView.findViewById(R.id.editButton);
        nameTextView = rootView.findViewById(R.id.nameTextView);
        ageTextView = rootView.findViewById(R.id.ageTextView);
        locationTextView = rootView.findViewById(R.id.locationTextView);
        phoneTextView = rootView.findViewById(R.id.phoneTextView);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Profile").child(currentUserId);

        webView = rootView.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true); // JavaScript'i etkinleştirin (isteğe bağlı)

        // WebView içeriğini açın ve WebViewClient kullanarak WebView'i yönlendirin
        webView.loadUrl("https://www.artzzzz.com");
        webView.setWebViewClient(new WebViewClient());

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfilePopup();
            }
        });

        // Firebase'den profil bilgilerini kontrol et
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    int age = dataSnapshot.child("age").getValue(Integer.class);
                    String location = dataSnapshot.child("location").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);

                    nameTextView.setText("İsim: " + name);
                    ageTextView.setText("Yaş: " + age);
                    locationTextView.setText("Konum: " + location);
                    phoneTextView.setText("Telefon Numarası: " + phone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Hata durumunu işle
            }
        });

        return rootView;
    }

    private void showEditProfilePopup() {
        FragmentManager fragmentManager = getParentFragmentManager();
        EditProfileFragment editProfileFragment = new EditProfileFragment();
        editProfileFragment.show(fragmentManager, "edit_profile");
    }

}
