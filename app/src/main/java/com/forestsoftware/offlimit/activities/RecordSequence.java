package com.forestsoftware.offlimit.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.forestsoftware.offlimit.R;
import com.forestsoftware.offlimit.models.keymodel.Key;
import com.forestsoftware.offlimit.services.KeyPressListener;
import com.forestsoftware.offlimit.services.Reciever;
import com.forestsoftware.offlimit.util.Const;
import com.forestsoftware.offlimit.util.Store;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by HP-PC on 5/21/2018.
 */

public class RecordSequence extends AppCompatActivity implements KeyPressListener {
    private static boolean showing = false;
    private ScrollView scrollview;
    private LinearLayout linearLayout,linearLayoutBeforeRecording,linearLayoutRecordingStarted;
    private LinearLayout.LayoutParams layoutParams;
    private static int i;
    private Button startRecoring, add_k,cancelRecord;
    public static boolean recodingSequence = false;
    private List<Key> combination;
    private List<Integer> combinationCode;
    String m = "";
    int typeId = 1;
    List<Integer> comb = new ArrayList();
    private Store store;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_sequence);
        store = new Store(RecordSequence.this);
        Intent i = getIntent();
        //typeId = i.getIntExtra("" + Const.DISTRESS_ID, 0);

        getSupportActionBar().setTitle("Set Lock HotKey");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Reciever.setKeyPressListener(this);



        linearLayout = (LinearLayout) findViewById(R.id.gamehistory);
        add_k = (Button) findViewById(R.id.add_key);
        startRecoring = (Button)findViewById(R.id.start_recording);


        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        combination = new ArrayList<>();
        combinationCode = new ArrayList<>();
        blinkingRecording();
        initViews();
    }

    private int iii;
//    StringBuilder bu = null;

    @Override
    public void keyPress(final Key key) {
        Log.wtf(RecordSequence.class.getSimpleName(), "Key is press" + key);
        combination.add(key);
        comb = combinationCode;

       // final Button keyButton = new Button(AllocsteDistressKeys.this);

        Button keyButton = (Button) View.inflate(RecordSequence.this, R.layout.lock_combination_button, null);

        keyButton.setText(++iii + " " + key.getName());
        final String kx = keyButton.getText().toString();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.setMargins(0,10,0,10);

        linearLayout.addView(keyButton, layoutParams);
        String k = "";

        add_k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                new Store(RecordSequence.this).storeCombination("" + typeId, combination);
                Log.wtf(RecordSequence.class.getSimpleName(), " " + m + " >> " + comb);
                Log.wtf("The Gotten Combination is: ", "" + store.getCombination2("" + typeId));
                finish();
                Intent i = new Intent(RecordSequence.this, CreateSequence.class);
                startActivity(i);


            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        showing = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        showing = false;
    }

    @Override
    public void onDestroy() {
        showing = false;
        super.onDestroy();
    }

    public static boolean isShowing() {
        return showing;
    }
    public static boolean isRecording()
    {
        return recodingSequence;
    }

    public void blinkingRecording() {
        TextView myText = (TextView) findViewById(R.id.record);

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(400); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        myText.startAnimation(anim);
    }
    public void doRecording()
    {

    }
    public void initViews()
    {
        linearLayoutBeforeRecording = (LinearLayout)findViewById(R.id.layout_before_recording_started);
        linearLayoutRecordingStarted = (LinearLayout)findViewById(R.id.layout_after_recording_started);
        cancelRecord = (Button)findViewById(R.id.cancel_record);
        scrollview = (ScrollView) findViewById(R.id.record_scrollview);

        startRecoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                recodingSequence = true;
                linearLayoutBeforeRecording.setVisibility(View.GONE);
                linearLayoutRecordingStarted.setVisibility(View.VISIBLE);
                scrollview.setVisibility(View.VISIBLE);
              //  Reciever.setKeyPressListener(AllocsteDistressKeys.this);

            }
        });
        cancelRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                linearLayoutBeforeRecording.setVisibility(View.VISIBLE);
                linearLayoutRecordingStarted.setVisibility(View.GONE);
                scrollview.setVisibility(View.GONE);
                linearLayout.removeAllViews();
            }
        });


    }


}