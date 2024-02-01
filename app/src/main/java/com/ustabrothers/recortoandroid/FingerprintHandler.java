package com.ustabrothers.recortoandroid;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.widget.Toast;

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;

    public FingerprintHandler(Context context) {
        this.context = context;
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        showToast("Authentication Error: " + errString);
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        showToast("Authentication Help: " + helpString);
    }

    @Override
    public void onAuthenticationFailed() {
        showToast("Authentication Failed.");
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        showToast("Authentication Succeeded.");
        // Başarılı giriş durumu, uygulama içinde yapılacak işlemler burada gerçekleştirilir.
        Intent intent = new Intent(context, HastaActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    private void showToast(CharSequence message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
