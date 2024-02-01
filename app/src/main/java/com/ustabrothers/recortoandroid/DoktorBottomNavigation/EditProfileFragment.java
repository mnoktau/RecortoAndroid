package com.ustabrothers.recortoandroid.DoktorBottomNavigation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ustabrothers.recortoandroid.R;

public class EditProfileFragment extends DialogFragment {

    private EditText nameEditText, ageEditText, locationEditText, phoneEditText;

    private DatabaseReference databaseReference;
    private String currentUserId;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Layout dosyasını yükleyin
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_edit_profile, null);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Profile").child(currentUserId);

        nameEditText = dialogView.findViewById(R.id.editNameEditText);
        ageEditText = dialogView.findViewById(R.id.editAgeEditText);
        locationEditText = dialogView.findViewById(R.id.editLocationEditText);
        phoneEditText = dialogView.findViewById(R.id.editPhoneEditText);

        builder.setView(dialogView)
                .setTitle("Profil Düzenle")
                .setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String name = nameEditText.getText().toString();
                        int age = Integer.parseInt(ageEditText.getText().toString());
                        String location = locationEditText.getText().toString();
                        String phone = phoneEditText.getText().toString();

                        // Profil bilgilerini Firebase Realtime Database'e kaydet
                        databaseReference.child("name").setValue(name);
                        databaseReference.child("age").setValue(age);
                        databaseReference.child("location").setValue(location);
                        databaseReference.child("phone").setValue(phone);
                    }
                })
                .setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }
}
