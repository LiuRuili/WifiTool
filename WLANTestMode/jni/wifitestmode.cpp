/******************************************************************************/
/*                                                               Date:10/2012 */
/*                                PRESENTATION                                */
/*                                                                            */
/*       Copyright 2012 TCL Communication Technology Holdings Limited.        */
/*                                                                            */
/* This material is company confidential, cannot be reproduced in any form    */
/* without the written permission of TCL Communication Technology Holdings    */
/* Limited.                                                                   */
/*                                                                            */
/* -------------------------------------------------------------------------- */
/*  Author :  Chen Ji                                                         */
/*  Email  :  Ji.Chen@tcl-mobile.com                                          */
/*  Role   :                                                                  */
/*  Reference documents : refer bugID200662/161302                            */
/* -------------------------------------------------------------------------- */
/*  Comments :                                                                */
/*  File     : development/apps/WLANTestMode                                  */
/*  Labels   :                                                                */
/* -------------------------------------------------------------------------- */
/* ========================================================================== */
/*     Modifications on Features list / Changes Request / Problems Report     */
/* -------------------------------------------------------------------------- */
/*    date   |        author        |         Key          |     comment      */
/* ----------|----------------------|----------------------|----------------- */
/* 10/22/2012|Chen Ji               |bugID321787           |jni methods are d */
/*           |                      |                      |efined in this fi */
/*           |                      |                      |le                */
/* ----------|----------------------|----------------------|----------------- */
/* 11/23/2012|Chen Ji               |bugID329061           |jni methods are d */
/*           |                      |                      |efined in this fi */
/*           |                      |                      |le                */
/* ----------|----------------------|----------------------|----------------- */
/******************************************************************************/

#include <utils/Log.h>

#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdarg.h>
#include <errno.h>
#include <string.h>
#include "jni.h"

static int wifi_testmode_set(const char* value, const char* path) {
    int sz;
    int fd = -1;
    int ret = -1;

    remove(path);
    fd = open(path, O_WRONLY | O_CREAT,0777);
    if (fd < 0) {
        ALOGE("open(%s) for write failed: %s (%d)", path,
             strerror(errno), errno);
        goto out;
    }

    sz = write(fd, value, strlen(value));
    if (sz < 0) {
       ALOGE("write(%s) failed: %s (%d)", path, strerror(errno),
            errno);
        goto out;
    }
    ret = 0;

out:
    if (fd >= 0) close(fd);
    return ret;
}

static jint wifi_testmode_set_channel(JNIEnv *env, jobject object, jint value)
{
    char * channel[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "36", "40", "44", "48", "52", "56", "60", "64","100", "104","108","112", "116","120","124","128","132","136","140","149","153","157", "161","165", "38", "46","54","62", "102","110","118","126","134","151", "159"};
    int ret = -1;

    ret = wifi_testmode_set((const char *)channel[value], "/data/wl/channel");

    return ret;
}

static jint wifi_testmode_set_type(JNIEnv *env, jobject object, jint value)
{
    char * type[] = {"0","1","2","3","4","5","6","7","8"};
    int ret = -1;

    ret = wifi_testmode_set((const char *)type[value], "/data/wl/type");

    return ret;
}

static jint wifi_testmode_set_power(JNIEnv *env, jobject object, jint value)
{
    char * power[] = {"00","10","11","12","13","14","15","16","17","18","19","20","21","22"};
    int ret = -1;

    ret = wifi_testmode_set((const char *)power[value], "/data/wl/power");

    return ret;
}

static jint wifi_testmode_set_dgain(JNIEnv *env, jobject object, jint value) {
	char * dgain[] = {"-39","-37","-35","-33","-31","-29","-27","-25","-23","-21","-19","-17","-15",
	        "-13","-11","-9","-7","-5","-3","-1","0","1","3","5","7","9","11","13","15","17","19","21","23","24"};
	int ret = -1;
	ret = wifi_testmode_set((const char *)dgain[value], "/data/wl/dgain");
    return ret;
}

static jint wifi_testmode_set_rfgain(JNIEnv *env, jobject object, jint value) {
	char * rfgain[] = {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17",
			"18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
	int ret = -1;
	ret = wifi_testmode_set((const char *)rfgain[value], "/data/wl/rfgain");
    return ret;
}

static jint wifi_testmode_set_channel_bw(JNIEnv *env, jobject object, jint bw, jint value)
{
    char * channel[][48] = {{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "36", "40", "44", "48", "52", "56", "60", "64","100", "104","108","112", "116","120","124","128","132","136","140","149","153","157", "161","165"},
    {"3", "4", "5", "6", "7", "8", "9", "10", "11", "38", "42", "46", "50", "54", "58", "62", "102", "106", "110", "114", "118", "122", "126", "130", "134", "138", "142", "151", "155", "159", "163"},
    {"42", "46", "50", "54", "58", "106", "110", "114", "118", "122", "126", "130", "134", "138", "155", "159"}};
    int ret = -1;

    ret = wifi_testmode_set((const char *)channel[bw][value], "/data/wl/channel");

    return ret;
}

static jint wifi_testmode_set_channel_bonding(JNIEnv *env, jobject object, jint value)
{
    char * channelBongding[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    int ret = -1;

    ret = wifi_testmode_set((const char *)channelBongding[value], "/data/wl/channel_bonding");

    return ret;
}

static jint wifi_testmode_set_rate_bw(JNIEnv *env, jobject object, jint bw , jint mode , jint value)
{
    char * rate11b[] = {"11B_LONG_1_MBPS", "11B_LONG_2_MBPS", "11B_LONG_5_5_MBPS", "11B_LONG_11_MBPS", "11B_SHORT_2_MBPS", "11B_SHORT_5_5_MBPS","11B_SHORT_11_MBPS"};
    char * rate11ag[] = {"11A_6_MBPS", "11A_9_MBPS", "11A_12_MBPS", "11A_18_MBPS","11A_24_MBPS", "11A_36_MBPS", "11A_48_MBPS", "11A_54_MBPS"};
    char * rate11n[2][16] = {{"MCS_6_5_MBPS","MCS_13_MBPS", "MCS_19_5_MBPS", "MCS_26_MBPS", "MCS_39_MBPS", "MCS_52_MBPS","MCS_58_5_MBPS", "MCS_65_MBPS","MCS_SG_7_2_MBPS","MCS_SG_14_4_MBPS","MCS_SG_21_7_MBPS","MCS_SG_28_9_MBPS","MCS_SG_43_3_MBPS","MCS_SG_57_8_MBPS","MCS_SG_65_MBPS","MCS_SG_72_2_MBPS"},
                           {"MCS_CB_13_5_MBPS", "MCS_CB_27_MBPS", "MCS_CB_40_5_MBPS", "MCS_CB_54_MBPS", "MCS_CB_81_MBPS", "MCS_CB_108_MBPS", "MCS_CB_121_5_MBPS", "MCS_CB_135_MBPS", "MCS_CB_15_MBPS", "MCS_CB_30_MBPS","MCS_CB_45_MBPS", "MCS_CB_60_MBPS", "MCS_CB_90_MBPS", "MCS_CB_120_MBPS", "MCS_CB_135_MBPS", "MCS_CB_150_MBPS"}};
    char * rate11ac[3][20] = {{"MCS_VHT20_NGI_6_5_MBPS","MCS_VHT20_NGI_13_MBPS","MCS_VHT20_NGI_19_5_MBPS","MCS_VHT20_NGI_26_MBPS","MCS_VHT20_NGI_39_MBPS","MCS_VHT20_NGI_52_MBPS","MCS_VHT20_NGI_58_5_MBPS","MCS_VHT20_NGI_65_MBPS","MCS_VHT20_NGI_78_MBPS","MCS_VHT20_NGI_86_5_MBPS","MCS_VHT20_SGI_7_2_MBPS","MCS_VHT20_SGI_14_4_MBPS", "MCS_VHT20_SGI_21_6_MBPS", "MCS_VHT20_SGI_28_8_MBPS", "MCS_VHT20_SGI_43_3_MBPS", "MCS_VHT20_SGI_57_7_MBPS", "MCS_VHT20_SGI_65_MBPS", "MCS_VHT20_SGI_72_2_MBPS", "MCS_VHT20_SGI_86_6_MBPS", "MCS_VHT20_SGI_96_1_MBPS"},
                              {"MCS_VHT40_NGI_CB_13_5_MBPS", "MCS_VHT40_NGI_CB_27_MBPS", "MCS_VHT40_NGI_CB_40_5_MBPS", "MCS_VHT40_NGI_CB_54_MBPS", "MCS_VHT40_NGI_CB_81_MBPS", "MCS_VHT40_NGI_CB_108_MBPS", "MCS_VHT40_NGI_CB_121_5_MBPS","MCS_VHT40_NGI_CB_135_MBPS", "MCS_VHT40_NGI_CB_162_MBPS", "MCS_VHT40_NGI_CB_180_MBPS","MCS_VHT40_SGI_CB_15_MBPS", "MCS_VHT40_SGI_CB_30_MBPS", "MCS_VHT40_SGI_CB_45_MBPS", "MCS_VHT40_SGI_CB_60_MBPS", "MCS_VHT40_SGI_CB_90_MBPS", "MCS_VHT40_SGI_CB_120_MBPS","MCS_VHT40_SGI_CB_135_MBPS", "MCS_VHT40_SGI_CB_150_MBPS", "MCS_VHT40_SGI_CB_180_MBPS", "MCS_VHT40_SGI_CB_200_MBPS"},
                              {"MCS_VHT80_NGI_CB_29_3_MBPS", "MCS_VHT80_NGI_CB_58_5_MBPS", "MCS_VHT80_NGI_CB_87_8_MBPS", "MCS_VHT80_NGI_CB_117_MBPS", "MCS_VHT80_NGI_CB_175_5_MBPS","MCS_VHT80_NGI_CB_234_MBPS", "MCS_VHT80_NGI_CB_263_3_MBPS", "MCS_VHT80_NGI_CB_292_5_MBPS", "MCS_VHT80_NGI_CB_351_MBPS", "MCS_VHT80_NGI_CB_390_MBPS","MCS_VHT80_SGI_CB_32_5_MBPS", "MCS_VHT80_SGI_CB_65_MBPS", "MCS_VHT80_SGI_CB_97_5_MBPS", "MCS_VHT80_SGI_CB_130_MBPS", "MCS_VHT80_SGI_CB_195_MBPS","MCS_VHT80_SGI_CB_260_MBPS", "MCS_VHT80_SGI_CB_292_5_MBPS", "MCS_VHT80_SGI_CB_325_MBPS", "MCS_VHT80_SGI_CB_390_MBPS", "MCS_VHT80_SGI_CB_433_3_MBPS"}};                       

    int ret = -1;
    if (mode == 0) {
        ret = wifi_testmode_set((const char *)rate11b[value], "/data/wl/rate");
    } else if (mode == 1) {
        ret = wifi_testmode_set((const char *)rate11ag[value], "/data/wl/rate");
    } else if (mode == 2) {
        ret = wifi_testmode_set((const char *)rate11n[bw][value], "/data/wl/rate");
    } else if (mode == 3) {
        ret = wifi_testmode_set((const char *)rate11ac[bw][value], "/data/wl/rate");
    }

    return ret;
}

static jint wifi_testmode_set_txpkglen(JNIEnv *env, jobject object, jint len) {
    char * length[] = {"1000","2000","3000","4000"};
    int ret = -1;
    ret = wifi_testmode_set(length[len],"data/wl/pkglen");
    return ret;
}

static jint wifi_testmode_set_power_cntl_mode(JNIEnv *env, jobject object, jint value) {
    char * power_cntl_mode[] = {"1","2"};
    int ret = -1;
    ret = wifi_testmode_set(power_cntl_mode[value],"data/wl/power_cntl");
    return ret;
}

static JNINativeMethod methods[] = {
    {"wifi_testmode_set_channel", "(I)I", (void*)wifi_testmode_set_channel },
    {"wifi_testmode_set_type", "(I)I", (void*)wifi_testmode_set_type },
    {"wifi_testmode_set_power", "(I)I", (void*)wifi_testmode_set_power },
    {"wifi_testmode_set_dgain","(I)I", (void*)wifi_testmode_set_dgain },
    {"wifi_testmode_set_rfgain","(I)I", (void*)wifi_testmode_set_rfgain },
    {"wifi_testmode_set_channel_bonding","(I)I", (void*)wifi_testmode_set_channel_bonding },
    {"wifi_testmode_set_channel_bw", "(II)I", (void*)wifi_testmode_set_channel_bw },
    {"wifi_testmode_set_rate_bw", "(III)I", (void*)wifi_testmode_set_rate_bw },
    {"wifi_testmode_set_txpkglen","(I)I", (void*)wifi_testmode_set_txpkglen },
    {"wifi_testmode_set_power_cntl_mode","(I)I", (void*)wifi_testmode_set_power_cntl_mode }
};


/*
 * Register several native methods for one class.
 */
static int registerNativeMethods(JNIEnv* env, const char* className,
    JNINativeMethod* gMethods, int numMethods)
{
    jclass clazz;

    clazz = env->FindClass(className);
    if (clazz == NULL) {
        ALOGE("Native registration unable to find class '%s'", className);
        return JNI_FALSE;
    }
    if (env->RegisterNatives(clazz, gMethods, numMethods) < 0) {
        ALOGE("RegisterNatives failed for '%s'", className);
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

/*
 * Register native methods for all classes we know about.
 *
 * returns JNI_TRUE on success.
 */
static int registerNatives(JNIEnv* env)
{
  if (!registerNativeMethods(env, "com/android/WLANTestMode/WLANTestModeNative",
                 methods, sizeof(methods) / sizeof(methods[0]))) {
    return JNI_FALSE;
  }

  return JNI_TRUE;
}


// ----------------------------------------------------------------------------

/*
 * This is called by the VM when the shared library is first loaded.
 */

typedef union {
    JNIEnv* env;
    void* venv;
} UnionJNIEnvToVoid;

jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
    UnionJNIEnvToVoid uenv;
    uenv.venv = NULL;
    jint result = -1;
    JNIEnv* env = NULL;

    ALOGI("JNI_OnLoad");

    if (vm->GetEnv(&uenv.venv, JNI_VERSION_1_4) != JNI_OK) {
        ALOGE("ERROR: GetEnv failed");
        goto bail;
    }
    env = uenv.env;

    if (registerNatives(env) != JNI_TRUE) {
        ALOGE("ERROR: registerNatives failed");
        goto bail;
    }

    result = JNI_VERSION_1_4;

bail:
    return result;
}
