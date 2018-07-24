package br.com.douglimar.surpresinhadiadesorte;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by dmoraes on 08/12/2017.
 * Get Results of lottery on the internet
 */

public class WebviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        WebView myWebView;

        AdView adViewWeb = findViewById(R.id.adViewWebview);
        AdRequest adRequestWeb = new AdRequest.Builder().build();
        adViewWeb.loadAd(adRequestWeb);


        Intent intent = getIntent();

        String url = intent.getStringExtra("URL");

        WebViewClient myWebViewClient = new WebViewClient() {

            final ProgressDialog progressDialog = new ProgressDialog(WebviewActivity.this);

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                super.onPageStarted(view, url, favicon);
                progressDialog.setMessage("Aguarde... Carregando Resultados");
                progressDialog.setCancelable(false);

                progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener(){

                    //@Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        return keyCode == KeyEvent.KEYCODE_SEARCH;
                    }
                });

                progressDialog.show();
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                progressDialog.setMessage("Aguarde... Carregando Resultados na internet");
                progressDialog.setCancelable(true);

                progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener(){
                    //@Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                        return keyCode == KeyEvent.KEYCODE_SEARCH;
                    }});

                progressDialog.show();
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

			/* @Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				if (url.contains("www1.caixa.gov.br") == true )
					return false;

				return true;

			} */
        };

        // Get Web view
        myWebView = findViewById( R.id.webView1 ); //This is the id you gave
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setSupportZoom(true);       //Zoom Control on web (You don't need this if ROM supports Multi-Touch
        myWebView.getSettings().setBuiltInZoomControls(true); //Enable Multitouch if supported by ROM
        myWebView.getSettings().setLoadsImagesAutomatically(true); // Dont loud the images
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(false);

        /*

        if ( pMessage.equals("LOTOMANIA") || pMessage.equals("LOTOFACIL") )
            myWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        else */
        myWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        myWebView.setWebViewClient(myWebViewClient);

        // Load URL
        myWebView.loadUrl(url);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}
