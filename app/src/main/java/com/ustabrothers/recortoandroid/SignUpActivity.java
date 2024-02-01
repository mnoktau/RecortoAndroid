package com.ustabrothers.recortoandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private AppCompatEditText email;
    private AppCompatEditText sifre;
    private AppCompatTextView girisBtn, girisDoktor;

    private FirebaseAuth mAuth;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private Dialog popupDialog;

    private Button btnUpload;

    private ImageView imageViewPopup;

    private static final int PICK_IMAGE_REQUEST = 1;

    private Bitmap selectedImageBitmap;

    private Dialog termsDialog;
    private CheckBox checkBoxAgree;


// FirebaseAuth örneği oluşturun


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.signUpEmail);
        sifre = findViewById(R.id.signUpSifre);
        girisBtn = findViewById(R.id.signUpButton);
        girisDoktor = findViewById(R.id.doktorSignUp);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("images");


        mAuth = FirebaseAuth.getInstance();

        girisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailGiris = email.getText().toString();
                String sifreGiris = sifre.getText().toString();

                mAuth.createUserWithEmailAndPassword(emailGiris, sifreGiris)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    sendEmailVerification();
                                    showTermsDialog();
                                } else {
                                    // Kayıt başarısız ise, hata mesajını işleyebilirsiniz
                                    Exception e = task.getException();
                                    Toast.makeText(SignUpActivity.this, "Kayıt Başarısız: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


        girisDoktor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openPopup();
            }
        });
    }

    private void openPopup() {
        popupDialog = new Dialog(this);
        popupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupDialog.setContentView(R.layout.pop_layout);
        popupDialog.show();



        imageViewPopup = popupDialog.findViewById(R.id.imageViewPopup);
        btnUpload = popupDialog.findViewById(R.id.btnUpload);

        imageViewPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                openGallery();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImageBitmap != null) {
                    uploadImageToFirebase();
                } else {
                    Toast.makeText(SignUpActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Galeriden seçilen resmin URI'sini al
            // ve ImageView'e set et
            selectedImageBitmap = getBitmapFromGallery(data.getData());
            imageViewPopup.setImageBitmap(selectedImageBitmap);
        }
    }

    private Bitmap getBitmapFromGallery(Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    private void saveImageUrlToDatabase(String imageUrl) {
        String userId = mAuth.getCurrentUser().getUid();

        // Resim URL'sini Firebase Realtime Database'e kaydetme
        Map<String, Object> imageMap = new HashMap<>();
        imageMap.put("imageUrl", imageUrl);

        databaseReference.child(userId).push().setValue(imageMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Image uploaded and saved to database", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void uploadImageToFirebase() {
        // Bitmap'i byte dizisine dönüştürme
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();

        // Resmi Firebase Storage'a yükleme
        String userId = mAuth.getCurrentUser().getUid();
        String imageName = "image_" + System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = storageReference.child("images/" + userId + "/" + imageName);
        imageRef.putBytes(imageData)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            // Resim başarıyla yüklendi, Firebase Realtime Database'e kaydetme
                            String imageUrl = task.getResult().getMetadata().getReference().getDownloadUrl().toString();
                            saveImageUrlToDatabase(imageUrl);
                            String emailGiris = email.getText().toString();
                            String sifreGiris = sifre.getText().toString();

                            mAuth.createUserWithEmailAndPassword(emailGiris, sifreGiris)
                                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {

                                                Toast.makeText(SignUpActivity.this, "Email onayını bekleyiniz.", Toast.LENGTH_SHORT).show();

                                            } else {

                                            }
                                        }
                                    });

                            Toast.makeText(SignUpActivity.this, "Kimlik Başarı ile Eklendi.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Kimlik eklenemedi.", Toast.LENGTH_SHORT).show();
                        }
                        popupDialog.dismiss();
                    }
                });

    }


    private void showTermsDialog() {
        termsDialog = new Dialog(this);
        termsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        termsDialog.setContentView(R.layout.terms_dialog_layout);

        Button btnAgree = termsDialog.findViewById(R.id.btnAgree);
        Button btnCancel = termsDialog.findViewById(R.id.btnCancel);
        checkBoxAgree = termsDialog.findViewById(R.id.checkBoxAgree);

        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxAgree.isChecked()) {
                    // Kullanıcı sözleşmeyi kabul ettiği zaman yeni aktiviteye geçiş yapabilirsiniz.
                    Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                    termsDialog.dismiss();
                } else {
                    // Kullanıcı sözleşmeyi kabul etmediği durumu işleyebilirsiniz.
                    // Örneğin, bir uyarı mesajı gösterebilirsiniz.
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                termsDialog.dismiss();
            }
        });

        termsDialog.show();
    }

    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // E-posta onayı başarıyla gönderildi.
                            Toast.makeText(getApplicationContext(), "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        } else {
                            // Hata durumunda kullanıcıya bilgi verin.
                            Toast.makeText(getApplicationContext(), "Failed to send verification email. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
