package com.forestsoftware.offlimit.models.keymodel;

/**
 * Created by olyjosh on 20/05/2018.
 */

public class Key {

    public static final String KEY_UP = "Volume UP";
    public static final String KEY_DOWN = "Volume DOWN";

    private String name;
    private int code;
    public long timeStamp;


    public Key(String name, int code) {
        this.name = name;
        this.code = code;
    }



//    public Key(String name, int code, long timeStamp) {
//        this.name = name;
//        this.code = code;
//        this.timeStamp = timeStamp;
//    }

    public Key(String name) {
        this.name = name;
    }

    public Key() {
    }

    @Override
    public String toString() {
        return code+"_"+name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
