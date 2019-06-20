package com.example.autgikar.srigapp;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

import static android.net.wifi.WifiManager.WIFI_STATE_DISABLED;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.example.autgikar.srigapp.action.FOO";
    private static final String ACTION_BAZ = "com.example.autgikar.srigapp.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.autgikar.srigapp.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.autgikar.srigapp.extra.PARAM2";

    public static String TAG ="IntntService";

    public MyIntentService() {
        super("MyIntentService");
    }
    public static Intent newIntentFoo(Context c) {
        Log.d(TAG, "new intent created in srvc");
        return new Intent(ACTION_FOO, null, c, MyIntentService.class);
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
        Log.d(TAG, "started intent with action "+ACTION_FOO);

    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Log.d(TAG, "Received intent "+action);
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
//        throw new UnsupportedOperationException("Not yet implemented");
        Context ctx = getApplicationContext();
        WifiManager wifiMgr = (WifiManager) ctx.getSystemService(this.WIFI_SERVICE);
        int wifiEnabled = wifiMgr.getWifiState();
        SharedPreferences shPref = PreferenceManager.getDefaultSharedPreferences(ctx);
        //Handler hndl = new Handler(Looper.getMainLooper());
        PowerManager pm = (PowerManager) ctx.getSystemService(this.POWER_SERVICE);
        boolean uiOn = pm.isInteractive();
        boolean wifiSet = false;
        String wifiStr="wifiSet";

        //if( ! uiOn) {

            wifiSet = shPref.getBoolean(wifiStr, false);
            Calendar today = Calendar.getInstance();
            int hrs = today.get(Calendar.HOUR_OF_DAY);
            int min = today.get(Calendar.MINUTE);
            String fromHour="fromHour";
            String fromMin="fromMinute";
            String toHour="toHour";
            String toMin="toMinute";
            int setHrs1 = shPref.getInt(fromHour,0);
            int setMin1 = shPref.getInt(fromMin,0);
            int setHrs2 = shPref.getInt(toHour,0);
            int setMin2 = shPref.getInt(toMin,0);

            Log.d(TAG, "wifiset " + wifiSet + " wifiEnabled: " + wifiEnabled + " handle action -- hrs = " +hrs + " & min = " + min);
            Log.d(TAG, " handle action from -- hrs = " +setHrs1+ " & min = " + setMin1);
            Log.d(TAG, " handle action till -- hrs = " +setHrs2+ " & min = " + setMin2);

            if(wifiSet) {
                if (wifiEnabled == WIFI_STATE_ENABLED) {
                    wifiMgr.setWifiEnabled(false);
                } else if (wifiEnabled == WIFI_STATE_DISABLED) {
                    if ((setHrs1 <= hrs) && (setMin1 <= min) &&
                        (setHrs2 >= hrs) && (setMin2 >= min)) {
                        Log.d(TAG, " handle action time within range of From & Till -- skip enabling");
                        // skip enabling.
                    } else {
                        Log.d(TAG, " handle action time outside range of From & Till -- enabling");
                        wifiMgr.setWifiEnabled(true);
                    }
            }

            }
            /*hndl.post(new Runnable() {
                @Override
                public void run() {
                  // Toast.makeText(MyIntentService.this, "foo event callback ", Toast.LENGTH_SHORT).show();
                }
            }); */
        //}
        Log.d(TAG, "Received intent for action Foo; WIFI: "+ wifiEnabled + " uiOn: "+uiOn);
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
