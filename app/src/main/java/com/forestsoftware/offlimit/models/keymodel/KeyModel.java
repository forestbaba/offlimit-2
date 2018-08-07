package com.forestsoftware.offlimit.models.keymodel;

/**
 * Created by HP-PC on 5/21/2018.
 */

public class KeyModel
{
    private String keyname;
    private int code;

    public KeyModel(String keyname, int code) {
        this.keyname = keyname;
        this.code = code;
    }

    public String getKeyname() {
        return keyname;
    }

    public void setKeyname(String keyname) {
        this.keyname = keyname;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "KeyModel{" +
                "keyname='" + keyname + '\'' +
                ", code=" + code +
                '}';
    }
}
