package br.com.douglimar.surpresinhadiadesorte;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE  = "br.com.douglimar.surpresinha.MESSAGE";
    public static final String EXTRA_MESSAGE2 = "br.com.douglimar.surpresinha.MESSAGE2";

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnJogoUnico = findViewById(R.id.btnSingleGame);
        Button btnJogosMultiplos = findViewById(R.id.btnMultipleGames);
        Button btnLastResults = findViewById(R.id.btnLastResults);
        Button btnOpenGooglePlay = findViewById(R.id.btnMyApps);


        final Surpresinha surpresinha = new Surpresinha();
        final Intent intent = getIntent();
        final String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        //TextView tvTitulo = findViewById(R.id.tvSelectGameTitle2);
        //tvTitulo.setText(R.string.quina);

        // Create a AdView
        // Load Advertisement Banner
        AdView mAdView = findViewById(R.id.adViewMain);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        btnJogoUnico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "SelectGame");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "JogoUnicoClick");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                Intent intent2 = new Intent(getBaseContext(), ResultActivity.class);

                intent2.putExtra(MainActivity.EXTRA_MESSAGE, message);
                intent2.putExtra(MainActivity.EXTRA_MESSAGE2, surpresinha.generateDiaDeSorteGame());
                intent2.putExtra("XPTO", 1);

                startActivity(intent2);
            }
        });

        btnJogosMultiplos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "SelectGame");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "JogoMultiplo");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                Intent intent1 = new Intent(getApplicationContext(), ConfiguradorActivity.class);
                intent1.putExtra(MainActivity.EXTRA_MESSAGE, message);

                startActivity(intent1);
            }
        });

        btnLastResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                carregaWebView();

            }
        });

        btnOpenGooglePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGooglePlay();
            }
        });


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

    private void carregaWebView() {

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "SelectGame");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "CarregaWebView");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        Intent intent = new Intent(getApplicationContext(), WebviewActivity.class);
        intent.putExtra("URL", "http://www.loterias.caixa.gov.br/wps/portal/loterias/landing/diadesorte");

        startActivity(intent);
    }

    private void openGooglePlay() {

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "OpenGooglePlay");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "GooglePlay");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        final String appPackageName = "ddmsoftware"; // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/search?q=" + appPackageName)));
        }
    }
}
