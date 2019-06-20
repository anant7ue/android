package com.example.autgikar.srigapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.example.autgikar.srigapp.R.id.wifiBox;

public class MainActivity extends AppCompatActivity {

    private Button mSaveButton;
    private WifiManager wifiMgr;
    private PromiscusReceiver mPromRcvr;
    private Context ctxt;
    private SharedPreferences dsPref;
    private int wifiState;
    public String prd ="Periodicity";
    public String load="loadFactor";
    public String fromHour="fromHour";
    public String fromMin="fromMinute";
    public String toHour="toHour";
    public String toMin="toMinute";
    public String btSet="btSet";
    public String wifiSet="wifiSet";
    public String fromBtHour="fromBtHour";
    public String fromBtMin="fromBtMinute";
    public String fromWifiState="fromWifi";
    public int hr, min, period;
    public int hr2, min2;
    public TimePicker tFrom;
    public TimePicker tTill;
    public String TAG ="debug";
    public int delayAlarm = 5*100; //5k msec but alarm runs with 60 sec always


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dsPref = getDefaultSharedPreferences(MainActivity.this);

        hr = dsPref.getInt(fromHour, 0);
        min = dsPref.getInt(fromMin, 0);
        hr2 = dsPref.getInt(toHour, 0);
        min2 = dsPref.getInt(toMin, 0);
        Log.d(TAG, "loaded: from hr " +hr+" min "+min + " toHr " + hr2 + " toMin "+ min2);

        ctxt = getBaseContext(); // which ctxt -- may not be mainActivity
        // compare with MainActivity ctxt using ctxt.getClass()

        IntentFilter screenFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        screenFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mPromRcvr = new PromiscusReceiver();
        registerReceiver(mPromRcvr, screenFilter);

        wifiMgr = (WifiManager) getSystemService(MainActivity.this.WIFI_SERVICE);
        wifiState = wifiMgr.getWifiState();
        Log.d(TAG, "system wifi state " + wifiState);
        wifiState = dsPref.getInt(fromWifiState, 0);
        Log.d(TAG, "wifi state retrieved as " + wifiState);


        Intent itt = MyIntentService.newIntentFoo(MainActivity.this);
        //this.startService(itt); // superseded by setInexactRepeating

        PendingIntent pi = PendingIntent.getService(this.ctxt, 0, itt, 0);
        AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), delayAlarm, pi);

        tFrom = (TimePicker) findViewById(R.id.fromTime);
        tFrom.setHour(hr);
        tFrom.setMinute(min);
        Log.d(TAG, "set loaded hr " +hr+" min "+min);

        tTill = (TimePicker) findViewById(R.id.tillTime);
        tTill.setHour(hr2);
        tTill.setMinute(min2);
        Log.d(TAG, "set loaded hr " +hr2+" min "+min2);

        mSaveButton = (Button) findViewById(R.id.saveId);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckBox wBox = (CheckBox) findViewById(R.id.wifiBox);
                boolean wifiSelect = wBox.isChecked();
                int btSelect = 0;

                hr = tFrom.getHour();
                min = tFrom.getMinute();
                hr2 = tTill.getHour();
                min2 = tTill.getMinute();
                Log.d(TAG, "click saved hr " +hr+" min "+min + " toHr " + hr2 + " toMin "+ min2);
                //if(wifiSelect) {
                Toast.makeText(MainActivity.this, "Wifi disabling", Toast.LENGTH_SHORT).show();
                dsPref.edit().putInt(fromHour, hr).putInt(fromMin, min).putInt(fromWifiState, 1).commit();
                dsPref.edit().putInt(toHour, hr2).putInt(toMin, min2).putBoolean(wifiSet, wifiSelect).commit();

                Log.d(TAG, "wifi state changed to " + wifiState);
                // set alarm
                dsPref.getInt(fromHour, hr);
                dsPref.getInt(fromMin, min);
                Log.d(TAG, "verify readback loaded hr " +hr+" min "+min);

                Toast.makeText(MainActivity.this, "Wifi disabled", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onStart() {
        dsPref = getDefaultSharedPreferences(MainActivity.this);

        hr = dsPref.getInt(fromHour, 0);
        min = dsPref.getInt(fromMin, 0);
        Log.d(TAG, "starting --loaded hr " +hr+" min "+min);

        hr2 = dsPref.getInt(toHour, 0);
        min2 = dsPref.getInt(toMin, 0);
        Log.d(TAG, "starting: from hr " +hr+" min "+min + " toHr " + hr2 + " toMin "+ min2);

        tFrom = (TimePicker) findViewById(R.id.fromTime);
        tFrom.setHour(hr);
        tFrom.setMinute(min);
        Log.d(TAG, "starting -- set loaded hr " +hr+" min "+min);

        tTill = (TimePicker) findViewById(R.id.tillTime);
        tTill.setHour(hr2);
        tTill.setMinute(min2);
        Log.d(TAG, "starting loaded hr2 " +hr2+" min2 "+min2);

        super.onStart();

    }

    @Override
    public void onStop() {
        dsPref.getInt(fromHour, hr);
        dsPref.getInt(fromMin, min);
        Log.d(TAG, "stopping --loaded hr " +hr+" min "+min);

        super.onStop();

    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "stopping --service n receiver ");
    }
}
