 package com.ustabrothers.recortoandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


import org.json.JSONException;

import java.util.Arrays;

 public class MainActivity extends AppCompatActivity {

     private Context context = this;

    public AppCompatEditText email, sifre;
    public AppCompatTextView signup, giris;
    public AppCompatImageView facebook, google;

    private FirebaseAuth mAuth;

     CallbackManager callbackManager = CallbackManager.Factory.create();

    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);




        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();







        // mAuth nesnesini oluşturun


        email = findViewById(R.id.emailSignIn);
        sifre = findViewById(R.id.sifreSignIn);

        signup = findViewById(R.id.signUp);
        giris = findViewById(R.id.signIn);
        facebook = findViewById(R.id.facebookSignIn);
        google = findViewById(R.id.googleSignIn);


        mAuth = FirebaseAuth.getInstance();






        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();


                // Create an Intent to start the SignUp Activity:
                Intent intent = new Intent(context, SignUpActivity.class);

                // Start the new Activity:
                context.startActivity(intent);

            }
        });



        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailGiris = email.getText().toString();
                String sifreGiris = sifre.getText().toString();

                mAuth.signInWithEmailAndPassword(emailGiris, sifreGiris)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Giriş başarılı ise
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    if (user != null && user.isEmailVerified()) {
                                        showBiometricDialog();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Email onayı yapılmamış", Toast.LENGTH_SHORT).show();
                                    }


                                    // Kullanıcı bilgileri user değişkeninde bulunur
                                    // İstediğiniz işlemleri burada yapabilirsiniz
                                } else {
                                    // Giriş başarısız ise, hata mesajını işleyebilirsiniz
                                    FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                    Toast.makeText(MainActivity.this, "Giriş başarısız: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });



            }
        });

        /*
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // Facebook üzerinde başarılı bir şekilde oturum açıldı
                        AccessToken accessToken = loginResult.getAccessToken();

                        // Facebook Access Token'ı kullanarak Firebase'e giriş yapma işlemi
                        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
                        mAuth.signInWithCredential(credential)
                                .addOnCompleteListener(MainActivity.this, task -> {
                                    if (task.isSuccessful()) {
                                        // Firebase'e başarılı bir şekilde giriş yapıldı
                                        // Kullanıcı bilgilerini almak için Facebook Graph API'sını kullanabilirsiniz


                                        // Create an Intent to start the SignUp Activity:
                                        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);

                                        // Start the new Activity:
                                        startActivity(intent);
                                    } else {
                                        // Giriş başarısız oldu
                                        Exception e = task.getException();
                                        Toast.makeText(MainActivity.this, "Giriş Başarısız" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    @Override
                    public void onCancel() {
                        // Facebook Login işlemi iptal edildi
                    }

                    @Override
                    public void onError(FacebookException error) {
                        // Facebook Login işlemi sırasında bir hata oluştu
                        Toast.makeText(MainActivity.this, "Facebook Login failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

        }); */

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent signInIntent = GoogleSignIn.getClient(MainActivity.this, gso).getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                    finish();
            }
        });

    }




     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         callbackManager.onActivityResult(requestCode, resultCode, data);
         if (requestCode == RC_SIGN_IN) {
             Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
             try {
                 GoogleSignInAccount account = task.getResult(ApiException.class);
                 firebaseAuthWithGoogle(account.getIdToken());


             } catch (ApiException e) {
                 // Google Login işlemi başarısız oldu
             }
         }
     }



     private void firebaseAuthWithGoogle(String idToken) {
         AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
         mAuth.signInWithCredential(credential)
                 .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful()) {
                             // Create an Intent to start the SignUp Activity:


                             // Google ile başarılı bir şekilde giriş yapıldı
                             FirebaseUser user = mAuth.getCurrentUser();

                             Intent intent = new Intent(MainActivity.this, HastaActivity.class);

                             // Start the new Activity:
                             startActivity(intent);
                             finish();
                         } else {
                             // Giriş başarısız oldu
                         }
                     }
                 });
     }

     private void showBiometricDialog() {
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setTitle("Parmak İzi Doğrulama");
         builder.setMessage("Parmak izinizi kullanarak giriş yapın");

         builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 startBiometricAuthentication();
             }
         });

         builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 // İptal edildiğinde yapılacak işlemler
                 showToast("İşlem iptal edildi.");
             }
         });

         builder.show();
     }

     private void startBiometricAuthentication() {
         CancellationSignal cancellationSignal = new CancellationSignal();

         FingerprintManagerCompat.AuthenticationCallback authenticationCallback =
                 new FingerprintManagerCompat.AuthenticationCallback() {
                     @Override
                     public void onAuthenticationError(int errMsgId, CharSequence errString) {
                         showToast("Authentication Error: " + errString);
                     }

                     @Override
                     public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                         showToast("Authentication Help: " + helpString);
                     }

                     @Override
                     public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                         showToast("Authentication Succeeded.");

                         // Başarılı giriş durumu, yeni aktiviteye geçmek için Intent oluştur
                         Intent intent = new Intent(context, HastaActivity.class);
                         startActivity(intent);
                         finish(); // İsteğe bağlı: mevcut aktivitenin kapatılması
                     }

                     @Override
                     public void onAuthenticationFailed() {
                         showToast("Authentication Failed.");
                     }
                 };

         FingerprintManagerCompat.from(this).authenticate(null, 0, null, authenticationCallback, null);
     }

     private void showToast(CharSequence message) {
         Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
     }




 }