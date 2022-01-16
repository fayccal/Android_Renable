package com.example.android_renable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView cardST, cardTS, cardInfo, cardPlus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ///On récupère les cardview clickable et on leur set le click listener
        cardST = findViewById(R.id.cardST);
        cardTS = findViewById(R.id.cardTS);
        cardInfo = findViewById(R.id.cardInfo);
        cardPlus = findViewById(R.id.cardPlus);

        cardST.setOnClickListener(this);
        cardTS.setOnClickListener(this);
        cardInfo.setOnClickListener(this);
        cardPlus.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        ///On choisie l'activté que on veut lancher
        if(view == this.cardST) {
            Intent STintent = new Intent(MainActivity.this, speech_text.class);
            startActivity(STintent);
        }
        if(view == this.cardTS) {
            Intent TSintent = new Intent(MainActivity.this, text_speech.class);
            TSintent.putExtra("FROM", "MAIN");
            startActivity(TSintent);
        }
        if(view == this.cardInfo) {
            Intent INFOintent = new Intent(MainActivity.this, info.class);
            startActivity(INFOintent);
        }
        if(view == this.cardPlus) {
            Intent PLUSintent = new Intent(MainActivity.this, addMessages.class);
            startActivity(PLUSintent);
        }
    }
}