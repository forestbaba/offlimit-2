package com.forestsoftware.offlimit.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.forestsoftware.offlimit.R;

/**
 * Created by olyjosh on 23/05/2018.
 */

public class Util {


    private static MediaPlayer mediaPlayer;

    public static void startMediaPlayer(Context c) {

        if (mediaPlayer == null) {
            mediaPlayer = android.media.MediaPlayer.create(c, R.raw.sound);
            mediaPlayer.setVolume(0, 0);
            mediaPlayer.setLooping(true);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.start();
        }
        if(!mediaPlayer.isPlaying())
            mediaPlayer.start();

    }
}
