//
// Created by xlj on 17-3-6.
//

#ifndef OBDEPTH2_OPENNIEX_H
#define OBDEPTH2_OPENNIEX_H

#include <stdio.h>
#include <android/log.h>
#include "Include/OpenNI.h"

#define LOG_TAG "OpenNIEx-Jni"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG, __VA_ARGS__)

#define READ_WAIT_TIMEOUT 2000 //2000ms
#define HISTSIZE 0xFFFF

using namespace openni;

class OpenNIEx{
public:
    OpenNIEx();
    ~OpenNIEx();
    int init();
    int enumerateDevices( int vid, int pid);
    int open(int vid, int pid);
    int waitAndUpdate();
    void close();
    int ConventToRGBA(uint8_t * dst, int w, int h);

private:

    void calcDepthHist(VideoFrameRef& frame);
    VideoStream mColor;
    VideoFrameRef mFrame;
    Device mDevice;
    uint32_t* m_histogram;
    int mWidth;
    int mHeight;
};

#endif //OBDEPTH2_OPENNIEX_H
