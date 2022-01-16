package com.example.android_renable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class info extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String BACKUP_MSG = "BACKUP_MSG";
    private static final String BACKUP_MSG_KEY1 = "BACKUP_MSG_KEY1";
    private static final String BACKUP_MSG_KEY2 = "BACKUP_MSG_KEY2";
    private static final String BACKUP_MSG_KEY3 = "BACKUP_MSG_KEY3";


    private TextView t1, t2, t3;
    private EditText e1, e2, e3;
    private Button registerButton;
    private ToggleButton tg;
    private String score_v;
    private boolean isRunning = false;
    private Intent Sintent; /*= new Intent(this, informationService.class);*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        t1 = findViewById(R.id.textD1);
        t2 = findViewById(R.id.textD2);
        t3 = findViewById(R.id.textD3);

        e1 = findViewById(R.id.info1);
        e2 = findViewById(R.id.info2);
        e3 = findViewById(R.id.info3);

        registerButton = findViewById(R.id.btnsave);
        tg = findViewById(R.id.tglON);
        Sintent = new Intent(this, informationService.class);

        ///Vérification que les clefs on été enregistrer si oui on load leur valeur
        if(getSharedPreferences(BACKUP_MSG, MODE_PRIVATE).contains(BACKUP_MSG_KEY1)) {
            score_v = getSharedPreferences(BACKUP_MSG, MODE_PRIVATE).getString(BACKUP_MSG_KEY1, "ERROR");
            t1.setText(score_v);
        }
        if(getSharedPreferences(BACKUP_MSG, MODE_PRIVATE).contains(BACKUP_MSG_KEY2)) {
            score_v = getSharedPreferences(BACKUP_MSG, MODE_PRIVATE).getString(BACKUP_MSG_KEY2, "ERROR");
            t2.setText(score_v);
        }
        if(getSharedPreferences(BACKUP_MSG, MODE_PRIVATE).contains(BACKUP_MSG_KEY3)) {
            score_v = getSharedPreferences(BACKUP_MSG, MODE_PRIVATE).getString(BACKUP_MSG_KEY3, "ERROR");
            t3.setText(score_v);
        }

        ///On load la valeur du toggle button si elle existe
        if(getSharedPreferences(BACKUP_MSG, MODE_PRIVATE).contains("BOOLTG")) {
            boolean tg_value = getSharedPreferences(BACKUP_MSG, MODE_PRIVATE).getBoolean("BOOLTG", false);
            tg.setChecked(tg_value);
            if(tg.isChecked()) {
                activate_service();
            }
        }
        registerButton.setOnClickListener(this);
        tg.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == registerButton) {
            t1.setText("Contact Number:");
            t1.append(e1.getText().toString());
            t2.setText("Doctor:");
            t2.append(e2.getText().toString());
            t3.setText("Other:");
            t3.append(e3.getText().toString());

            getSharedPreferences(BACKUP_MSG, MODE_PRIVATE).edit().putString(BACKUP_MSG_KEY1, t1.getText().toString()).apply();
            getSharedPreferences(BACKUP_MSG, MODE_PRIVATE).edit().putString(BACKUP_MSG_KEY2, t2.getText().toString()).apply();
            getSharedPreferences(BACKUP_MSG, MODE_PRIVATE).edit().putString(BACKUP_MSG_KEY3, t3.getText().toString()).apply();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            activate_service();
        }
        else {
            getSharedPreferences(BACKUP_MSG, MODE_PRIVATE).edit().putBoolean("BOOLTG", false).apply();
                stopService(Sintent);
                isRunning = false;
        }
    }

    ///active le service quand elle est appelée
    public void activate_service() {
        getSharedPreferences(BACKUP_MSG, MODE_PRIVATE).edit().putBoolean("BOOLTG", true).apply();
        Sintent.putExtra("BU_KEY", BACKUP_MSG);
        Sintent.putExtra("BU_V_KEY1", t1.getText().toString());
        Sintent.putExtra("BU_V_KEY2", t2.getText().toString());
        Sintent.putExtra("BU_V_KEY3", t3.getText().toString());
        Sintent.putExtra("TOGGLED_KEY", tg.isChecked());
        startService(Sintent);
        isRunning = true;
    }
}