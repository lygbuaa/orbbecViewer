//
// Created by xlj on 17-3-7.
//

#include <jni.h>
#include <stdio.h>
#include <android/log.h>
#include "OpenNIEx.h"

#define REGISTER_CLASS "com/orbbec/NativeNI/OpenNIEx"



OpenNIEx g_openNI;


jint init(JNIEnv* env, jobject obj){



    return 0;
}


jint open(JNIEnv* env, jobject obj, jint vid, jint pid){

    return  g_openNI.open(vid, pid);
}


jint WaitAndUpdate(JNIEnv* env, jobject obj){

    return  g_openNI.waitAndUpdate();
}


void closedevice(JNIEnv* env, jobject obj){
      g_openNI.close();
}


jint ConvertTORGBA(JNIEnv* env, jobject obj, jobject dst, jint w, jint h){
    if(dst == NULL){
        return -1;
    }
    uint8_t * dstBuf = (uint8_t*)env->GetDirectBufferAddress(dst);


    return  g_openNI.ConventToRGBA(dstBuf, w, h);
}



JNINativeMethod jniMethods[] = {
        { "init",                      "()I",                      (void*)&init},
        { "open",                      "(II)I",                      (void*)&open},
        { "WaitAndUpdate",                      "()I",                      (void*)&WaitAndUpdate},
        { "ConvertTORGBA",                      "(Ljava/nio/ByteBuffer;II)I",                      (void*)&ConvertTORGBA},
        { "closedevice",                      "()V",                      (void*)&closedevice},
};

jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
    JNIEnv* env;
    jclass gCallbackClass = nullptr;

    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }

    jclass clz     = env ->FindClass(REGISTER_CLASS);
    gCallbackClass = (jclass)env ->NewGlobalRef(clz);
    env ->RegisterNatives(clz, jniMethods, sizeof(jniMethods) / sizeof(JNINativeMethod));
    env ->DeleteLocalRef(clz);
    LOGD("OpenNIEx JNI_OnLoad");
    return JNI_VERSION_1_6;
}
