package com.forestsoftware.offlimit.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.forestsoftware.offlimit.R;
import com.forestsoftware.offlimit.adapter.LockCombinationAdapter;
import com.forestsoftware.offlimit.models.keymodel.Key;
import com.forestsoftware.offlimit.models.keymodel.LockkeyModel;
import com.forestsoftware.offlimit.util.Store;

import java.util.ArrayList;
import java.util.List;

public class LockSequence extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Store store;
    private RecyclerView recyclerView;
    private LockCombinationAdapter lockCombinationAdapter;
    private List<LockkeyModel> locksequence;
    private LinearLayout linearLay;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_sequence);

        linearLay = (LinearLayout)findViewById(R.id.lin);
        locksequence = new ArrayList<>();
        store = new Store(LockSequence.this);
        store.getAllCombinations();


        List<Key> comb = store.getCombination("" + 1);
//        locksequence.get(0).setKeyCombinations(comb);
        lockCombinationAdapter = new LockCombinationAdapter(LockSequence.this, locksequence);
//        Log.e(TAG, "-------combo------: " + store.getAllCombinations());
        Log.e(TAG, "-------combo------: " + comb);


        // final List<Key> combinations = distressMessage.getKeyCombinations();
        if (comb == null) {
            Log.e(TAG, "onCreate: yeeeeeeeeeeeeeeeeee" );

            TextView view = new TextView(LockSequence.this);
            view.setText("No HotKey set");
            view.setTextColor(ContextCompat.getColor(LockSequence.this, R.color.hint_color));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(20, 0, 10, 0);
            view.setLayoutParams(lp);
            view.setTypeface(view.getTypeface(), Typeface.ITALIC);
            linearLay.addView(view);


            return;
        }

        for (Key k : comb) {
            Log.e(TAG, "onCreate: praaaaaaaaaaaaaaaaa" );
            TextView view = (TextView) View.inflate(LockSequence.this, R.layout.textview_pill, null);
            view.setHeight(50);
            view.setTextSize(12);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(10, 0, 10, 0);
            view.setLayoutParams(lp);

            view.setText(k.getName());
            linearLay.addView(view);
            // holder.t4.addView(view);
        }

        //   LinearLayoutManager horizontalRecyclerLayout = new LinearLayoutManager(SetDistressActivity.this, LinearLayoutManager.VERTICAL, false);
//        recyclerView = (RecyclerView) findViewById(R.id.lock_sequence_recycler);
//        recyclerView.setLayoutManager(new LinearLayoutManager(LockSequence.this));
//        recyclerView.setAdapter(lockCombinationAdapter);


    }
}
