//
// Created by Ryan on 2017/8/30.
//
#include <jni.h>
#include <string.h>
#include <fcntl.h>
#include <errno.h>


#define DEVICE_NAME "/dev/mtgpio"

#define BLUE "sys/class/leds/blue"
#define RED "sys/class/leds/red"
#define GREEN "sys/class/leds/green"

#include <android/log.h>
#define LOGI(...) \
  ((void)__android_log_print(ANDROID_LOG_INFO, "hello-jni::", __VA_ARGS__))
#define LOGE(...) \
    ((void)__android_log_print(ANDROID_LOG_ERROR, "hello-jni::", __VA_ARGS__))

int fd;

enum {
    CMD_GET_GPIO = 0,
    CMD_SET_GPIO ,
    CMD_RELEASE_GPIO
};


JNIEXPORT jint JNICALL
Java_rainhu_com_demostore_jniDemo_JniDemoActivity_getTextFromJni(JNIEnv *env, jobject instance) {
    // TODO
    LOGI("test");
    return (*env)->NewStringUTF(env, "Hello from JNI !  Compiled wit");
}

JNIEXPORT jstring JNICALL
Java_rainhu_com_demostore_jniDemo_JniDemoActivity_openMtGpio(JNIEnv *env, jobject instance) {
    // TODO

    fd = open(DEVICE_NAME, O_RDWR);//打开设备
    LOGE("vibClass_Init()-> fd = %d  /n",fd);
    if(fd == -1)
    {
        LOGE("open device %s  errorNo : %d \n",DEVICE_NAME, errno);//打印调试信息
        return 0;
    }
    else
    {
        LOGI("open succeed");
        return 1;
    }
}

JNIEXPORT jint JNICALL
Java_rainhu_com_demostore_jniDemo_JniDemoActivity_add(JNIEnv *env, jobject instance, jint a,
                                                      jint b) {
    // TODO

}

JNIEXPORT jint JNICALL
Java_rainhu_com_demostore_jniDemo_JniDemoActivity_openGpioDev(JNIEnv *env, jobject instance) {
    // TODO

}

JNIEXPORT jint JNICALL
Java_rainhu_com_demostore_jniDemo_JniDemoActivity_getGpio(JNIEnv *env, jobject instance, jint num) {

    // TODO

}

JNIEXPORT jint JNICALL
Java_rainhu_com_demostore_jniDemo_JniDemoActivity_releaseGpio(JNIEnv *env, jobject instance,
                                                              jint num) {

    // TODO

}

JNIEXPORT jint JNICALL
Java_rainhu_com_demostore_jniDemo_JniDemoActivity_setGpioState(JNIEnv *env, jobject instance,
                                                               jint num, jint state) {

    // TODO

}

JNIEXPORT jint JNICALL
Java_rainhu_com_demostore_jniDemo_LedActivity_openBlue(JNIEnv *env, jobject instance) {
    // TODO

    
}



