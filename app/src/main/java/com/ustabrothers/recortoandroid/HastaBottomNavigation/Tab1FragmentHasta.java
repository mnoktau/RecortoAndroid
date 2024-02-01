package com.ustabrothers.recortoandroid.HastaBottomNavigation;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ustabrothers.recortoandroid.R;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class Tab1FragmentHasta extends Fragment {

    private int clickCount = 0;
    private TextView clickCountTextView;
    private Button clickButton;
    private WebView webView;

    public Tab1FragmentHasta() {
        // Boş yapıcı metot gereklidir.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab6, container, false);


        webView = view.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true); // JavaScript'i etkinleştirin (isteğe bağlı)

        // WebView içeriğini açın ve WebViewClient kullanarak WebView'i yönlendirin
        webView.loadUrl("https://www.artzzzz.com");
        webView.setWebViewClient(new WebViewClient());

        clickCountTextView = view.findViewById(R.id.clickCountTextView);
        clickButton = view.findViewById(R.id.clickButton);

        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClick();
            }
        });

        scheduleResetTask();
        return view;
    }

    private void handleClick() {
        if (clickCount < 3) {
            // Tıklama işlemi
            clickCount++;
            clickCountTextView.setText("Tıklamalar: " + clickCount);
        } else {
            clickButton.setEnabled(false);
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    clickButton.setEnabled(true);
                }
            }, 1000); // 1 saniye sonra butonu tekrar etkinleştir
        }
    }

    private void resetCounter() {
        // Sayaç sıfırlama işlemi
        clickCount = 0;
        clickCountTextView.setText("Tıklamalar: " + clickCount);
    }

    private void scheduleResetTask() {
        // Sıfırlama görevini her gün saat 00:00'da planla
        Timer timer = new Timer();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                resetCounter();
            }
        }, calendar.getTime(), 24 * 60 * 60 * 1000); // Her 24 saatte bir çalıştır
    }
}
