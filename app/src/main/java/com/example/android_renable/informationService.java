package com.example.android_renable;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class informationService extends Service {
    private BroadcastReceiver mReceiver;
    private boolean isShowing = false;
    private WindowManager windowManager;
    private TextView textview;

    private TextView[] textArray = new TextView[3];
    private String score_v;
    private String[] string_value = new String[3];
    private String[] string_value2 = new String[3];
    private boolean toggled;

    WindowManager.LayoutParams params;

    private static final String BACKUP_MSG = "BACKUP_MSG";
    private static final String BACKUP_MSG_KEY1 = "BACKUP_MSG_KEY1";
    private static final String BACKUP_MSG_KEY2 = "BACKUP_MSG_KEY2";
    private static final String BACKUP_MSG_KEY3 = "BACKUP_MSG_KEY3";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);

        for(int i = 0; i < 3; i++) {
            textArray[i] = new TextView(this);
            textArray[i].setText("");
            textArray[i].setBackgroundResource(R.drawable.bgcolor);
            textArray[i].setTextColor(ContextCompat.getColor(this, android.R.color.white));
            textArray[i].setTextSize(12f);
        }

        ///On récupère les valeur des clefs de cette facon car on ne peut pas utiliser getIntent de la meme facon dans un service que une activity
        ///Et vu que on cherche a charger les valeur dans onCreate c'est plus simple de cette manière
        if(getSharedPreferences(BACKUP_MSG, MODE_PRIVATE).contains(BACKUP_MSG_KEY1)) {
            string_value2[0] = getSharedPreferences(BACKUP_MSG, MODE_PRIVATE).getString(BACKUP_MSG_KEY1, "ERROR");
            textArray[0].setText(score_v);
        }
        if(getSharedPreferences(BACKUP_MSG, MODE_PRIVATE).contains(BACKUP_MSG_KEY2)) {
            string_value2[1] = getSharedPreferences(BACKUP_MSG, MODE_PRIVATE).getString(BACKUP_MSG_KEY2, "ERROR");
            textArray[1].setText(score_v);
        }
        if(getSharedPreferences(BACKUP_MSG, MODE_PRIVATE).contains(BACKUP_MSG_KEY3)) {
            string_value2[2] = getSharedPreferences(BACKUP_MSG, MODE_PRIVATE).getString(BACKUP_MSG_KEY3, "ERROR");
            textArray[2].setText(score_v);
        }


        textview = new TextView(this);
        textview.setText("");
        ///On rajoute tout les valeurs envoyer dans la textview
        for(int i = 0; i < 3; i++) {
            textview.append(string_value2[i]+ "\n");
        }
        textview.setBackgroundResource(R.drawable.bgcolor);
        textview.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        textview.setTextSize(12f);



        //set parameters for the textview
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.CENTER;

        //Register receiver for determining screen off and if user is present
        mReceiver = new LockScreenStateReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);

        registerReceiver(mReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ///onCreate s'éxecute avant donc on ne peut pas utiliser c'est valeur quand elle s'éxecute
        string_value[0] = (String) intent.getExtras().get("BU_V_KEY1");
        string_value[1] = (String) intent.getExtras().get("BU_V_KEY2");
        string_value[2] = (String) intent.getExtras().get("BU_V_KEY3");
        toggled = (boolean) intent.getExtras().get("TOGGLED_KEY");
        return START_STICKY;
    }

    ///Une classe broadcast qui nous sert à gérer l'affichage de notre message
    ///trouver sur stackoverflow
    public class LockScreenStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                //if screen is turn off show the textview
                if (!isShowing) {
                    windowManager.addView(textview, params);
                    isShowing = true;
                }
            }

            else if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
                //Handle resuming events if user is present/screen is unlocked remove the textview immediately
                if (isShowing) {
                    windowManager.removeViewImmediate(textview);
                    isShowing = false;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mReceiver != null) {
            //unregisterReceiver(mReceiver);
        }
        Intent broadcastIntent = new Intent(this, informationService.class);
        sendBroadcast(broadcastIntent);

        if (isShowing) {
            //windowManager.removeViewImmediate(textview);
            isShowing = false;
        }
        super.onDestroy();
    }
}
