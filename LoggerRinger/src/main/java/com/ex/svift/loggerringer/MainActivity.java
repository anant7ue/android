package com.ex.svift.loggerringer;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainActivity extends AppCompatActivity {
    public static String TAG="RingerLog";

    private SharedPreferences dshPref;
    private static String maxIndex ="MaxIndex";
    private static String keyStr = "KeyStr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dshPref = getDefaultSharedPreferences(MainActivity.this);

        dshPref.edit().putInt(maxIndex, 0).commit();
        dshPref.edit().putInt(keyStr+0, 0).commit();

        final EditText phNum = (EditText) findViewById(R.id.numText);
        NotificationManager notifMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (! notifMgr.isNotificationPolicyAccessGranted() ) {
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivityForResult( intent, 0 );
        }

        Button bAdd = (Button) findViewById(R.id.ringAdd);
        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numStr = phNum.getText().toString();
                try {
                    int num = Integer.parseInt(numStr);
                    int count = dshPref.getInt(maxIndex, 0);
                    Toast.makeText(MainActivity.this, "Received " + numStr+ " at index "+count, Toast.LENGTH_SHORT).show();
                    dshPref.edit().putInt(keyStr+count, num).commit();
                    count++;
                    dshPref.edit().putInt(keyStr+count, 0).commit();
                    dshPref.edit().putInt(maxIndex, count).commit();

                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Invalid value " + numStr, Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button bDel = (Button) findViewById(R.id.ringRemove);

        bDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numStr = phNum.getText().toString();
                try {
                    int num = Integer.parseInt(numStr);
                    int count = dshPref.getInt(maxIndex, 0);
                    boolean found = false;
                    for (int iter =0; iter < count; iter++) {
                        int tmp = dshPref.getInt(keyStr+iter,0);
                        if(tmp != num) continue;
                        found = true;
                        Toast.makeText(MainActivity.this, "Removing val from index "+ iter, Toast.LENGTH_SHORT).show();
                        if(iter < count -1) {
                            tmp = dshPref.getInt(keyStr+ (count-1),0);
                            dshPref.edit().putInt(keyStr+iter, tmp).commit();
                            dshPref.edit().putInt(keyStr+ (count-1), 0).commit();
                            dshPref.edit().putInt(maxIndex, (count-1) ).commit();
                        }
                    }
                    if(!found) {
                        Toast.makeText(MainActivity.this, "Value to remove not found ", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Invalid value " + numStr, Toast.LENGTH_SHORT).show();
                }

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
