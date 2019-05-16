package com.orbbec.NativeNI;


import java.nio.ByteBuffer;

/**
 * Created by xlj on 17-3-7.
 */

public class OpenNIEx {

    static {
        System.loadLibrary("OpenNIEx");
    }

    public  native static int init();
    public  native static int open(int w, int h);
    public  native static int WaitAndUpdate();
    public  native static int ConvertTORGBA(ByteBuffer dst, int w, int h);
    public  native static void closedevice();
}
