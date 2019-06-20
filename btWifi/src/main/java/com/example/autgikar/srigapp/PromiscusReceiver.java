package com.example.autgikar.srigapp;

import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class PromiscusReceiver extends BroadcastReceiver {
    public String TAG ="Receiver";
    private BluetoothManager btMgr;
    private WifiManager wifiMgr;
    private SharedPreferences dsPrefRx;
    public String wifiSet="wifiSet";

    public PromiscusReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //btMgr = (BluetoothManager) context.getSystemService(MainActivity.BLUETOOTH_SERVICE);
        wifiMgr = (WifiManager) context.getSystemService(MainActivity.WIFI_SERVICE);
        dsPrefRx = PreferenceManager.getDefaultSharedPreferences(context);
        boolean wifiSelect = dsPrefRx.getBoolean(wifiSet, false);

        Toast.makeText(context, "screen on/off; Wifi can be toggled", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Rcvd bcast intent"+ intent.getAction());
        if(intent.getAction().equals("android.intent.action.SCREEN_ON")) {
            if(wifiSelect) {
                //btMgr.getAdapter().enable();
                wifiMgr.setWifiEnabled(true);
                Log.d(TAG, "Rcvd intent ON setting wifi BT true");
            }

        } else if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
            if(wifiSelect) {
                // btMgr.getAdapter().disable();
                wifiMgr.setWifiEnabled(false);
                Log.d(TAG, "Rcvd intent OFF setting wifi BT false");
            }
        }

        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
