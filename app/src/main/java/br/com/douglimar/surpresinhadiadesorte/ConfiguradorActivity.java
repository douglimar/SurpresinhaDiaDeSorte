package br.com.douglimar.surpresinhadiadesorte;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;


public class ConfiguradorActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    private int iCount = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurador);

        AdView adView = findViewById(R.id.adViewConfigurador);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        TextView tvConfiguratorTitle  =  findViewById(R.id.tvConfiguratorTitle);

        tvNumero = findViewById(R.id.tvNumero);
        tvNumero.setText(String.valueOf(iCount));

        Intent intent = getIntent();

        final String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        tvConfiguratorTitle.setText(R.string.quina);

        Button btnMinus = findViewById(R.id.btnMinus);

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "btnMinus");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                iCount--;

                if (iCount < 2) {
                    Toast.makeText(getApplicationContext(), R.string.minimo_2_jogos, Toast.LENGTH_SHORT).show();
                    iCount = 2;
                }
                tvNumero.setText(String.valueOf(iCount));
            }
        });


        Button btnPlus = findViewById(R.id.btnPlus);

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "2");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "btnPlus");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                iCount++;

                if (iCount >10) {
                    iCount = 10;
                    Toast.makeText(getApplicationContext(), R.string.maximo10jogos, Toast.LENGTH_SHORT).show();
                }
                tvNumero.setText(String.valueOf(iCount));
            }
        });


        Button btnMultiBets = findViewById(R.id.btnGenerateMultipleBets);

        btnMultiBets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "3");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "btnMultiBets");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


                Surpresinha surpresinha = new Surpresinha();

                Intent intent1 = new Intent(getBaseContext(), ResultActivity.class);
                intent1.putExtra(MainActivity.EXTRA_MESSAGE, message);
                intent1.putExtra(MainActivity.EXTRA_MESSAGE2,  generateMultipleBets(surpresinha, iCount));
                intent1.putExtra("XPTO",  iCount);

                startActivity(intent1);
            }
        });

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    private TextView tvNumero;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {

            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @NonNull
    private String generateMultipleBets(Surpresinha pSurpresinha, int iQtd) {

        StringBuilder retorno = new StringBuilder();
        String sQuebralinha = "\n____________________\n";
        int iControle;

        for(int i = 0; i < iQtd; i++) {

            iControle = i+1;

            retorno.append("\nJogo ").append(iControle).append("\n\n").append(pSurpresinha.generateDiaDeSorteGame()).append(sQuebralinha);

        }

        return  retorno + "\n\n\n\n\n";
    }
}
