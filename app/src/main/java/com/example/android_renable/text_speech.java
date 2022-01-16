package com.example.android_renable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.Locale;
import java.util.UUID;

public class text_speech extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private ImageButton btnS;
    private EditText editS;
    private TextToSpeech TS;
    private Spinner dynamicSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_speech);

        btnS = findViewById(R.id.btnSpeak);
        editS = findViewById(R.id.spoken);
        dynamicSpinner = findViewById(R.id.spinner);

        ///liste des langues dans le spinner
        String[] items = new String[] { "Français", "English", "Japonais" };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dynamicSpinner.setAdapter(adapter);
        dynamicSpinner.setOnItemSelectedListener(this);

        ///On regarde quel Intent à lancer l'activité
        Intent nIntent = getIntent();
        if(nIntent != null) {
            ///On récupère la donnée FROM
            String from = nIntent.getExtras().getString("FROM");
            ///Si elle vient de l'activité Des messages par défaut on charge le message
            if(from.equals("DEFAULT")) {
                editS.setText(nIntent.getStringExtra("DATA"));
            }
        }

        ///On créée un TextToSpeech avec la langue par défaut
        TS = new TextToSpeech(this, status -> setLanguage(Locale.getDefault()));

        btnS.setOnClickListener(view -> speak());
    }

    ///met en pause le TS
    @Override
    public void onPause() {
        if (TS != null) {
            TS.stop();
        }
        super.onPause();
    }

    ///set la langue donnée
    private void setLanguage(Locale ln) {
        int res = TS.setLanguage(ln);
        Locale currentLanguage = TS.getVoice().getLocale();
    }

    ///récupère le text à lire et le lit à l'aide du TS
    private void speak() {
        String toSpeak = editS.getText().toString();
        String utteranceld = UUID.randomUUID().toString();
        TS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, utteranceld);
    }

    ///onDestroy méthode quand on quitte l'activity
    @Override
    protected void onDestroy() {
        super.onDestroy();
        TS.shutdown();
    }

    ///On change la langue selon l'élément du spinner choisie
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ///On ne peut pas utiliser la méthode setLanguage plus haut car elle comporte une ligne de trop qui fait crash l'app
        ///au changement de langue
        switch (position) {
            case 0:
                TS.setLanguage(new Locale("fr"));
                break;
            case 1:
                TS.setLanguage(new Locale("en-US"));
                break;
            case 2:
                TS.setLanguage(new Locale("ja_JP"));
                //setLanguage(Locale.getDefault());
                //TS = new TextToSpeech(this, status -> setLanguage(Locale.JAPAN));
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}