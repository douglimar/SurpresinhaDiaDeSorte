package br.com.douglimar.surpresinhadiadesorte;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

public class ResultActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        AdView adView = findViewById(R.id.adViewResult);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        tvResult = findViewById(R.id.tvResult);
        CoordinatorLayout linearResult = findViewById(R.id.linearResult);

        Intent intent = getIntent();

        final String numerosGerados = intent.getStringExtra(MainActivity.EXTRA_MESSAGE2);
        int iQtdeDeJogos = intent.getIntExtra("XPTO", 0);

        this.setTitle(R.string.app_name);

        final Surpresinha surpresinha = new Surpresinha();

        linearResult.setBackgroundResource(R.color.colorDiaDeSorte);
        appBarLayout.setBackgroundResource(R.drawable.degrade_radial_diadesorte);

        tvResult.setText(getString(R.string.numeros_da_sorte, numerosGerados));

        //Toast.makeText(getBaseContext(), "Clique no botão azul para gerar novos números sem ter que sair da tela.", Toast.LENGTH_LONG).show();

        Snackbar.make(findViewById(R.id.linearResult), R.string.tip_generateNewBets, Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show();

        FloatingActionButton fab = findViewById(R.id.fab);
        final int finalIQtdeDeJogos = iQtdeDeJogos;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Snackbar.make(view, "Gerando novos números da sorte.", Snackbar.LENGTH_LONG)
                //          .setAction("Action", null).show();

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Result");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "FabButton");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                if (finalIQtdeDeJogos<=1)
                    tvResult.setText(generateBet(surpresinha));
                else
                    tvResult.setText(generateMultiBets(surpresinha, finalIQtdeDeJogos));
            }
        });


        FloatingActionButton fabShare = findViewById(R.id.fab2);

        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Result");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "FabShare");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                Snackbar.make(view, R.string.compartilhando, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                shareContent(tvResult.getText().toString());

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

    private String generateMultiBets(Surpresinha pSurpresinha,int iQtd) {

        StringBuilder retorno = new StringBuilder("Estes são os seus números da sorte:\n");
        String sQuebralinha = "\n____________________\n";
        int iControle;

        for(int i = 0; i < iQtd; i++) {

            iControle = i + 1;
            retorno.append("\nJogo ").append(iControle).append("\n\n").append(pSurpresinha.generateDiaDeSorteGame()).append(sQuebralinha);
        }

        return  retorno + "\n\n\n\n\n";
    }


    private String generateBet(Surpresinha pSurpresinha) {

        String retorno = "Estes são os seus números da sorte:\n\n";

        retorno.concat(pSurpresinha.generateDiaDeSorteGame());

        return  retorno;
    }

    private void shareContent(String pMessage) {

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "shareContent");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Share Content");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.surpresinha));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, pMessage + "\n" + getResources().getString(R.string.googlePlayURL));
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.shareWith)));
    }

}
