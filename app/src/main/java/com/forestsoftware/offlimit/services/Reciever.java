package com.forestsoftware.offlimit.services;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.KeyEvent;
import com.forestsoftware.offlimit.R;
import com.forestsoftware.offlimit.activities.RecordSequence;
import com.forestsoftware.offlimit.models.keymodel.Key;
import com.forestsoftware.offlimit.util.SessionManager;
import com.forestsoftware.offlimit.util.Store;
import com.forestsoftware.offlimit.util.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;



import static android.content.Context.NOTIFICATION_SERVICE;


public class Reciever extends BroadcastReceiver {
    private static final String TAG = Reciever.class.getSimpleName();
    private static long time = 0;
    private static List<Key> combinations;
    private static Map<String, List<Key>> savedCombList;

    public static boolean fired = false;
    public static boolean isMaxVolume = false;
    private MediaPlayer mediaPlayer;
    private static KeyPressListener keyPressListener;
    private int press_thresshold;
    private Store store;
    private Context contexts;
    private Vibrator vibrator;
    private SessionManager sessionManager;
    private long pattern[] = {0, 1000};
    private boolean screenOff;


    public void onReceive(Context context, Intent intent) {
        contexts = context;
        sessionManager = new SessionManager(context);
        press_thresshold = sessionManager.getThresholdValue();

        if (store == null)
            store = new Store(context);
        //if (savedCombList == null)
        savedCombList = store.getAllCombinations();

        if (intent != null) {
            if (combinations == null)
                combinations = new ArrayList<Key>();
//            if (RecordSequence.isShowing() && RecordSequence.isRecording()) {
            if (RecordSequence.isShowing()) {
                onSetDistressActivity(context, intent);
            } else {
                onDistressCall(context, intent);
            }
        }

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            onBootComplete(context);
        }

//        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
//        {
//            Log.e(TAG, "onReceive: Otiya ooooooooooooooooooooo" );
//            screenOff = true;
//        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON))
//        {
//            screenOff = false;
//        }
        Intent i = new Intent(context, LockScreenService.class);
        i.putExtra("screen_state", screenOff);
        context.startService(i);

    }

    private void onBootComplete(Context c) {
//        startMediaPlayer(c);
        notify(c, "Boot complete");
        Util.startMediaPlayer(c);

        //TODO; Retrieve combination and store in-memory
    }

    private void notify(Context c, String text) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(c, "0389n")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Notification")
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(c);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(10, mBuilder.build());
    }

    private void onSetDistressActivity(Context context, Intent intent) {
        notify(context, "On Setting activity ");
        Key keyPress = getKeyPress(context, intent);
        if (keyPress != null)
            keyPressListener.keyPress(keyPress);
    }

    private void onDistressCall(Context context, Intent intent) {

        notify(context, "Distress key fired");
        Key keyPress = getKeyPress(context, intent);
        if (keyPress != null) {
            keyPress.setTimeStamp(System.currentTimeMillis());
            combinations.add(keyPress);

            int matchedDistressType = compare();
            if (matchedDistressType > 0) {
                long pattern[] = {0, 100};
                //vibrator = (Vibrator) contexts.getSystemService(Context.VIBRATOR_SERVICE);
                //vibrator.vibrate(pattern, -1);

                //  dovibrate(3,context);

                notify(context, "MAtcheed ");
                Log.wtf(TAG, "onDistressCall: djnkkkkkkkfjk");
                // Sending corresspinding message for matched combination
            } else {
                //     Toast.makeText(context, "No Match found", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private Key getKeyPress(Context context, Intent intent) {

        Key key = null;
        if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
            if (!fired) {
                if (intent.getExtras() != null) {
                    int prevVolume = intent.getExtras().getInt("android.media.EXTRA_PREV_VOLUME_STREAM_VALUE", 0);
                    int currentValue = intent.getExtras().getInt("android.media.EXTRA_VOLUME_STREAM_VALUE", 0);
                    AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                    fired = true;
                    if (currentValue - prevVolume > 0 || maxVolume == currentValue) {
                        key = new Key(Key.KEY_UP, KeyEvent.ACTION_UP);
                    } else {
                        key = new Key(Key.KEY_DOWN, KeyEvent.ACTION_DOWN);
                    }
//                    if(currentValue == isMaxVolume){
//                        audio.setStreamVolume(AudioManager.STREAM_MUSIC, currentValue-1, 0);
//                    }
                }

            } else
                fired = false;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.wtf(">>>>>>>>>>>>>>>>>SCREEN", "SCREEN_OFF");
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.wtf(">>>>>>>>>>>>>>>>>SCREEN", "SCREEN_ON");
        }
        return key;
    }

    public static void setKeyPressListener(KeyPressListener keyPressListener) {
       // Reciever.keyPressListener = keyPressListener;
    }


    private int compare() {
        Key formerKey = null;

        // checking all combination to see if threshold
        for (Key key : combinations) {
            if (formerKey == null)
                formerKey = key;
            else {
                if ((key.getTimeStamp() - formerKey.getTimeStamp()) > (press_thresshold * 1000)) {
                    combinations.clear();
                    return 0;
                }
            }
        }

        // checking for combination matches
        int match = 0;

        if (savedCombList == null)
            match = 0;
        final Set<Map.Entry<String, List<Key>>> entries = savedCombList.entrySet();

        int idx = 0;
        for (Map.Entry<String, List<Key>> savedCombKeySet : entries) {
            final List<Key> savedComb = savedCombKeySet.getValue();

            for (int i = 0; i < savedComb.size(); i++) {
                if (combinations.size() != savedComb.size()) {
                    break;
                }

                if (savedComb.get(i).getCode() != combinations.get(i).getCode()) {
                    break;
                }
                if (i == savedComb.size() - 1) {
                    match = Integer.parseInt(savedCombKeySet.getKey());
                }
            }


            if (idx == savedCombList.size() - 1)
                //combinations.clear();
                if (match > 0)
                    break;
            idx++;
        }

        return match;
    }

}
