package br.com.douglimar.surpresinhadiadesorte;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewResultActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    private ListView listView;
    private List<String> myList;
    private ArrayAdapter<String> adapter;
    private int iQtdeDeJogos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_result);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayoutNewResult);

        AdView adView = findViewById(R.id.adViewNewResult);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        TextView tvNewResult = findViewById(R.id.tvNewResult);
        tvNewResult.setText(R.string.numeros_da_sorte2);

        listView = findViewById(R.id.lstViewResult);
        myList = new ArrayList<>();

        Intent intent = getIntent();

        final String numerosGerados = intent.getStringExtra(MainActivity.EXTRA_MESSAGE2);
        iQtdeDeJogos = intent.getIntExtra("XPTO", 0);

        if (iQtdeDeJogos <=1)
            myList.add(numerosGerados);

        else {

            String[] aNumerosGerados = numerosGerados.split(";");

            /* Replacing the below FOR loop to a Collection -- Java Suggestion
            for (int i =0; i< iQtdeDeJogos; i++) {
                myList.add(aNumerosGerados[i]);
            } */

            myList.addAll(Arrays.asList(aNumerosGerados).subList(0, iQtdeDeJogos));
            myList.add("\n\n");
        }

        adapter = new ArrayAdapter<>(this, R.layout.item_row, R.id.tvItemRow, myList);

        listView.setAdapter(adapter);

        this.setTitle(R.string.app_name);

        final Surpresinha surpresinha = new Surpresinha();

        constraintLayout.setBackgroundResource(R.color.colorDiaDeSorte);

        FloatingActionButton fabNewGame = findViewById(R.id.fabNewGame);

        final int finalIQtdeDeJogos = iQtdeDeJogos;

        fabNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Result");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "FabButton");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                if (finalIQtdeDeJogos<=1) {
                    myList.clear();
                            myList.add(generateBet(surpresinha));

                    listView.setAdapter(adapter);
                }
                else {
                    myList.clear();

                    String[] aNumerosGerados = generateMultipleBetsList(surpresinha,finalIQtdeDeJogos).split(";");

                    /*for (int i =0; i< iQtdeDeJogos; i++) {

                        myList.add(aNumerosGerados[i]);
                    }*/

                    myList.addAll(Arrays.asList(aNumerosGerados).subList(0,iQtdeDeJogos));
                    myList.add("\n\n");

                    listView.setAdapter(adapter);
                }
            }
        });

        FloatingActionButton fabShare = findViewById(R.id.fabShare);

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

                StringBuilder strShareGame = new StringBuilder();

                for (int i = 0; i < myList.size()-1; i++) {

                    strShareGame.append(myList.get(i)).append("\n\n-------------------------\n");
                }

                shareContent(strShareGame.toString());

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

    private String generateMultipleBetsList(Surpresinha pSurpresinha, int iQtd) {

        StringBuilder retorno = new StringBuilder();

        int iControl;

        for (int i = 0; i < iQtd; i++) {

            iControl = i+1;

            retorno.append("Jogo ").append(iControl).append("\n\n").append(pSurpresinha.generateDiaDeSorteGame()).append(";");

            //list.add("Jogo " + iControl + "\n\n" + pSurpresinha.generateDiaDeSorteGame() + ";");
        }
        return retorno + "";
    }

    private String generateBet(Surpresinha pSurpresinha) {

        return  pSurpresinha.generateDiaDeSorteGame();
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
