package com.forestsoftware.offlimit.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.forestsoftware.offlimit.R;
import com.forestsoftware.offlimit.services.LockScreenService;
import com.forestsoftware.offlimit.services.Reciever;

public class MainActivity extends AppCompatActivity {

    private Reciever mReceiver;
    private IntentFilter filter;

    private static final String TAG = MainActivity.class.getSimpleName();
    private Button createButton, lockScreen,viewCombination;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    public static final String BUTTON_KEY = "com.javatar.floatingwidget.BUTTON_KEY";


    //TODO://Add Option to vibrate on unlock
    //TODO;//Add Option to play sound on Unlock

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        uiInIt();
        registerIntent();
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RecordSequence.class));
            }
        });
        lockScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLockWidget();
                //startActivity(new Intent(MainActivity.this, RecordSequence.class));

            }
        });
        viewCombination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LockSequence.class));

            }
        });


        Intent intent = getIntent();
        int button = intent.getIntExtra(BUTTON_KEY, 0);
        if (button > 0)

        {
            //((TextView) findViewById(R.id.textview)).setText("Button " + button + " is pressed!");
        }

        //Ask permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this))

        {

            Intent permissionIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(permissionIntent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        }
    }

    private void registerIntent() {
        filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new Reciever();
        registerReceiver(mReceiver, filter);
    }

    private void uiInIt() {
        createButton = (Button) findViewById(R.id.create_button);
        lockScreen = (Button) findViewById(R.id.lock_screen);
        viewCombination = (Button) findViewById(R.id.view_combinations);
    }

    public void startLockWidget() {
        startService(new Intent(MainActivity.this, LockScreenService.class));
        Log.e("Service Started", "Check it out");
        //finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            if (resultCode == RESULT_OK) {
                startLockWidget();
            } else {
                //TODO: Permission is not available, create a warning
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_POWER) {
            // Do something here...
            //event.startTracking(); // Needed to track long presses
            //startLockWidget();
            Log.e(TAG, "onKeyDown: Down now pressed");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
//        unregisterReceiver(mReceiver);
        super.onStop();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = (MenuItem) menu.findItem(R.id.switchId);
        item.setActionView(R.layout.switch_layout);
        Switch switchAB = item
                .getActionView().findViewById(R.id.activate_offlimit);
        switchAB.setChecked(false);

        switchAB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getApplication(), "OffLimit Activated", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(getApplication(), "OffLimit Deactivated", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        return true;
    }
}
