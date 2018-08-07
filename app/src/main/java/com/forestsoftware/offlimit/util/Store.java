package com.forestsoftware.offlimit.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.KeyEvent;

import com.forestsoftware.offlimit.models.keymodel.Key;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by olyjosh on 20/05/2018.
 */

public class Store {

    public static final String COMBINATION = "combination";
//    public static final String ALERT_TONE ="tone";
//    public static final String VIBRATE = "vibrate";

    private SharedPreferences.Editor editor;
    private SharedPreferences pref;
    private Context _context;

    private static final String SEP = "#@";
    private final String KEYS = "keys";

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public Store(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(Const.PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void storeCombination(String distressTypeId, List<Key> combination) {
        String combinationString = "";
        for (Key e : combination) {
            combinationString += e.getCode() + SEP;
        }
        editor.putString(distressTypeId, combinationString);
        editor.commit();
        storeCombinationKeys(distressTypeId);
    }

    public List<Key> getCombination(String key) {
        String string = pref.getString(key, "");
        if(string.isEmpty())
            return null;

        String[] comb = string.split(SEP);
        List<Key> combination = new ArrayList<>();
        for (String e : comb) {
            int code = Integer.parseInt(e);
            if (code == KeyEvent.ACTION_UP) {
                combination.add(new Key(Key.KEY_UP, code));
            } else if (code == KeyEvent.ACTION_DOWN) {
                combination.add(new Key(Key.KEY_DOWN, code));
            }
        }
        return combination.isEmpty() ? null : combination;
    }

    public List<Key> getCombination2(String key) {
        String string = pref.getString(key, "");
        String[] comb = string.split(SEP);
        List<Key> combination = new ArrayList<>();
        for (String e : comb) {
            int code = Integer.parseInt(e);
            if (code == KeyEvent.ACTION_UP)
            {
                combination.add(new Key(Key.KEY_UP));
            }
            else if (code == KeyEvent.ACTION_DOWN)
            {
                combination.add(new Key(Key.KEY_DOWN));
            }
        }
        return combination.isEmpty() ? null : combination;
    }

    /**
     * Store keys use in adding distress combination to storage
     * This is thesame as distress key
     *
     * @param distressTypeId
     */
    public void storeCombinationKeys(String distressTypeId) {
        Set<String> set = getCombinationKeys();
        if (set == null)
            set = new HashSet<String>();
        set.add(distressTypeId);
        editor.putStringSet(KEYS, set);
        editor.commit();
    }

    /**
     * Retrieve keys use in adding distress combination to storage
     * This is thesame as distress key
     */
    private Set<String> getCombinationKeys() {
        return pref.getStringSet(KEYS, null);
    }

    /**
     * Retrieves all combinations
     */
    public Map<String, List<Key>> getAllCombinations() {
        Set<String> combinationKeys = getCombinationKeys();
        Map<String, List<Key>> combinations = new HashMap<>();
        if (combinationKeys!=null)
        for (String key :combinationKeys) {
            final List<Key> combination = getCombination(key);
            combinations.put(key, combination);
        }
        return combinations;
    }

}
