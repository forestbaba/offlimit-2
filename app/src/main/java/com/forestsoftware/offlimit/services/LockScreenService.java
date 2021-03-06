package com.forestsoftware.offlimit.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.forestsoftware.offlimit.R;
import com.forestsoftware.offlimit.activities.MainActivity;
import com.forestsoftware.offlimit.util.FullscreenHelper;

/**
 * Created by HP-PC on 7/7/2018.
 */


public class LockScreenService extends Service {
    private WindowManager mWindowManager;
    private View mFloatingView;
    private WindowManager.LayoutParams mParams;
    private View mCollapsedView;
    private View mExpandedView;
    private BroadcastReceiver mReceiver;
    private MainActivity mainActivity;
    private FullscreenHelper fullscreenHelper;

    private static final int PRECITION = 10; // Touch precition

    public LockScreenService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);

        initPosition();
        initUI();
       // allowFullScreen();
        //  calculateDrag();
        registerIntent();


    }


    private void calculateDrag() {
        mFloatingView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //Initial Pos
                        initialX = mParams.x;
                        initialY = mParams.y;

                        //Touch pos
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);

                        if (Xdiff < PRECITION && Ydiff < PRECITION) {
                            if (isViewCollapsed()) {
                                mCollapsedView.setVisibility(View.GONE);
                                mExpandedView.setVisibility(View.VISIBLE);
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculatetion
                        mParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                        mParams.y = initialY + (int) (event.getRawY() - initialTouchY);

                        mWindowManager.updateViewLayout(mFloatingView, mParams);
                        return true;
                }
                return false;
            }
        });
    }

    private void registerIntent() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new Reciever();
        registerReceiver(mReceiver, filter);
    }

    private void initUI() {
        mainActivity = new MainActivity();
        mCollapsedView = mFloatingView.findViewById(R.id.collapse_view);
        mExpandedView = mFloatingView.findViewById(R.id.expanded_container);

        //Inıt buttons
        ImageView closeButtonCollapsed = (ImageView) mFloatingView.findViewById(R.id.close_btn);
        closeButtonCollapsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSelf();
            }
        });

        Button firstButton = (Button) mFloatingView.findViewById(R.id.first_btn);
        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApplication(1);
            }

        });
        Button secondButton = (Button) mFloatingView.findViewById(R.id.second_btn);
        secondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApplication(2);
            }
        });

        ImageView closeButton = (ImageView) mFloatingView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCollapsedView.setVisibility(View.VISIBLE);
                mExpandedView.setVisibility(View.GONE);
            }
        });

    }

    private void initPosition() {
        mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        mParams.gravity = Gravity.TOP | Gravity.LEFT;
        mParams.x = 0;
        mParams.y = 150;

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, mParams);
    }

    private boolean isViewCollapsed() {
        return mFloatingView == null || mFloatingView.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
    }

    private void openApplication(int button) {
        Intent intent = new Intent(LockScreenService.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(MainActivity.BUTTON_KEY, button);
        startActivity(intent);

        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);

        unregisterReceiver(mReceiver);
        Log.i("onDestroy Reciever", "Called");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        boolean screenOn = intent.getBooleanExtra("screen_state", false);
        if (!screenOn) {
            Log.e("screenON", "Called");
            Toast.makeText(getApplicationContext(), "Awake", Toast.LENGTH_LONG).show();


        } else {
            // startService(new Intent(this, LockScreenService.class));

            Log.e("screenOFF", "Called");
            // Toast.makeText(getApplicationContext(), "Sleep",
            // Toast.LENGTH_LONG)
            // .show();
        }
    }

    public void allowFullScreen() {
        WindowManager manager = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
// this is to enable the notification to recieve touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
// Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        localLayoutParams.height = (int) (50 * getResources().getDisplayMetrics().scaledDensity);
        localLayoutParams.format = PixelFormat.TRANSPARENT;
        FullscreenHelper view = new FullscreenHelper(this);
        manager.addView(view, localLayoutParams);
    }

}
