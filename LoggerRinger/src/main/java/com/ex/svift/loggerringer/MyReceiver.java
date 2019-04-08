package com.ex.svift.loggerringer;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class MyReceiver extends BroadcastReceiver {
    private final String TAG="DEBUG";
    private static String maxIndex ="MaxIndex";
    private static String keyStr = "KeyStr";
    private SharedPreferences shPref;

    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        shPref = getDefaultSharedPreferences(context);
        final int count = shPref.getInt(maxIndex,0);

        Log.d(TAG, "shared pref got maxindex as "+ count);

        TelephonyManager phMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final AudioManager aMgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        final NotificationManager notifMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        phMgr.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                int iter = 0;
                boolean found = true;

                super.onCallStateChanged(state, incomingNumber);
                Log.d(TAG, " incoming number " + incomingNumber);
                for (iter = 0; iter < count; iter++) {
                    int tmp = shPref.getInt(keyStr+iter, 0);
                    try {
                        if (tmp == Integer.parseInt(incomingNumber)) {
                            found = true;
                            Log.d(TAG, "Enable audio");
                            break;
                        }
                    } catch (NumberFormatException e) {
                        Log.d(TAG, "not valid number");
                    }
                }
                if (found) {
                    if(aMgr.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
                        notifMgr.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);

                        aMgr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        //Toast.makeText(MainActivity.this, "Ringer was silent; made normal", Toast.LENGTH_SHORT);
                        Log.d(TAG, "Ringer was silent; made normal");
                    }
                }
                Handler delayed = new Handler();
                delayed.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(MyReceiver.this, "Delayed toast", Toast.LENGTH_SHORT).show();
                        // set ringer silent
                        Log.d(TAG, "Delayed exec by 10 sec");
                    }
                }, 10000);
                // if Silent & found in defShPref, ring
            }

        }, PhoneStateListener.LISTEN_CALL_STATE);

        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
