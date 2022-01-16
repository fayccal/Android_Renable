package com.example.android_renable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class addMessages extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Button btnAdd;
    private EditText textAdd;
    private ToggleButton tglbtn;
    private boolean deleteON = false;
    private ListView list;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;

    private static final String BACKUP_MSG_KEY = "BACKUP_MSG_KEY";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_messages);

        btnAdd = findViewById(R.id.btnplus);
        tglbtn = (ToggleButton) findViewById(R.id.tglbtn);
        textAdd = findViewById(R.id.editMessage);
        list = findViewById(R.id.list);
        ///on charge la liste
        arrayList = loadList();
        //si le chargement était vide on créée une nouvelle liste
        if(arrayList == null) {
            arrayList = new ArrayList<String>();
        }
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener((parent, view, position, id) -> {
            if(deleteON == true) {
                String selectedItem = (String) (list.getItemAtPosition(position));
                arrayAdapter.remove(selectedItem);
                saveList(arrayList);
            }
            else {
                ///si on click sur un message ajouté ca nous envoie sur l'activité text to speech
                String selectedItem = (String) (list.getItemAtPosition(position));
                Intent TSintent = new Intent(addMessages.this, text_speech.class);
                TSintent.putExtra("FROM", "DEFAULT");
                TSintent.putExtra("DATA", selectedItem);
                startActivity(TSintent);
            }
        });

        btnAdd.setOnClickListener(this);
        tglbtn.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == this.btnAdd) {
            ///si le message est non vide il est ajouté et sauvegarder
            if(!textAdd.getText().toString().equals("")) {
                arrayAdapter.add(textAdd.getText().toString());
                saveList(arrayList);
            }
            else {
                Toast.makeText(this, "Vous n'avez pas écrit de message", Toast.LENGTH_SHORT).show();
            }
        }
    }

    ///Vérifie si on a activé le mode suppression ou pas
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            deleteON = true;
        }
        else {
            deleteON = false;
        }

    }

    ///J'utilise la librairie Gson pour pouvoir stocker la list de messages par défaut car cela rend le code plus simple et compréhensible
    public void saveList(List<String> l) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(l);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(BACKUP_MSG_KEY, jsonString);
        editor.apply();
    }

    ///méthode pour charger la liste quand on commence l'activité
    public ArrayList<String> loadList() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if(pref.contains(BACKUP_MSG_KEY)) {
            String jsonString = pref.getString(BACKUP_MSG_KEY, "YO");
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            ArrayList<String> listP = gson.fromJson(jsonString, type);
            return listP;
        }
        else {
            ArrayList<String> listP = new ArrayList<String>();
            return listP;
        }
    }

}