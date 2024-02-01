package com.ustabrothers.recortoandroid.DoktorBottomNavigation;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import com.ustabrothers.recortoandroid.R;

public class Tab1Fragment extends Fragment {

    private WebView webView;
    public Tab1Fragment() {
        // Boş yapıcı metot gereklidir.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Fragment'ın görünümünü oluşturun veya şişirin ve döndürün.
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);


        webView = view.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true); // JavaScript'i etkinleştirin (isteğe bağlı)

        // WebView içeriğini açın ve WebViewClient kullanarak WebView'i yönlendirin
        webView.loadUrl("https://www.artzzzz.com");
        webView.setWebViewClient(new WebViewClient());
        return view;
    }
}
