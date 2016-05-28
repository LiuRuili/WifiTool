
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
#include <ScopedUtfChars.h> //[DEGUB] MOD BY TCTNB.Ruili.Liu 1885375 20160431 Add SSID and IP item to WifiTestMode

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
    char * power[] = {"00","10","11","12","13","14","15","16","17","18","19","20"};
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
    char * channel[][48] = {{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"},
	{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "36", "40", "44", "48", "52", "56", "60", "64","100", "104","108","112", "116","120","124","128","132","136","140","149","153","157", "161","165"},
    {"3", "4", "5", "6", "7", "8", "9", "10", "11", "38", "42", "46", "50", "54", "58", "62", "102", "106", "110", "114", "118", "122", "126", "130", "134", "138", "142", "151", "155", "159", "163"},
    {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"},
	{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "36", "40", "44", "48", "52", "56", "60", "64","100", "104","108","112", "116","120","124","128","132","136","140","149","153","157", "161","165"},
    {"3", "4", "5", "6", "7", "8", "9", "10", "11", "38", "42", "46", "50", "54", "58", "62", "102", "106", "110", "114", "118", "122", "126", "130", "134", "138", "142", "151", "155", "159", "163"},
    {"42", "46", "50", "54", "58", "106", "110", "114", "118", "122", "126", "130", "134", "138", "155", "159"}};
    int ret = -1;

    ret = wifi_testmode_set((const char *)channel[bw][value], "/data/wl/channel");

    return ret;
}

static jint wifi_testmode_set_channel_bonding(JNIEnv *env, jobject object, jint value)
{
    char * channelBongding[] = {"0", "1", "2", "3", "4", "5", "6"};
    int ret = -1;

    ret = wifi_testmode_set((const char *)channelBongding[value], "/data/wl/channel_bonding");

    return ret;
}

static jint wifi_testmode_set_rate_bw(JNIEnv *env, jobject object, jint bw , jint value)
{
    char * rate11b[] = {"1", "2", "3", "6"};
    char * rate11g[] = {"4", "5", "7", "8", "10", "12", "13", "14"};
    char * rate11n[] = {"15", "16", "17", "18", "19", "20", "21", "22"};
    char * rate11ac[] = {"15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};

    int ret = -1;
    if (bw == 0) {
        ret = wifi_testmode_set((const char *)rate11g[value], "/data/wl/rate");
    } else if (bw == 6) {
        ret = wifi_testmode_set((const char *)rate11ac[value], "/data/wl/rate");
    } else if (bw == 3) {
        ret = wifi_testmode_set((const char *)rate11b[value], "/data/wl/rate");
    } else {
        ret = wifi_testmode_set((const char *)rate11n[value], "/data/wl/rate");
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
    char * power_cntl_mode[] = {"0","1","2"};
    int ret = -1;
    ret = wifi_testmode_set(power_cntl_mode[value],"data/wl/power_cntl");
    return ret;
}

//[DEGUB] ADD-BEGIN BY TCTNB.Ruili.Liu 1885375 20160431 Add SSID and IP item to WifiTestMode
static jint wifi_testmode_set_ssid(JNIEnv *env, jobject object, jstring value) {
    ScopedUtfChars command(env, value);
    const char *ssid = command.c_str();
    if (ssid == NULL) {
        return 0;
    }
    int ret = -1;
    ret = wifi_testmode_set((char *)command.c_str(),"data/wl/ssid");
    return ret;
}

static jint wifi_testmode_set_ip(JNIEnv *env, jobject object, jstring value) {
    ScopedUtfChars command(env, value);
    const char *ip = command.c_str();
    if (ip == NULL) {
        return 0;
    }
    int ret = -1;
    ret = wifi_testmode_set((char *)command.c_str(),"data/wl/ip");
    return ret;
}
//[DEGUB] ADD-END BY TCTNB.Ruili.Liu

static JNINativeMethod methods[] = {
    {"wifi_testmode_set_channel", "(I)I", (void*)wifi_testmode_set_channel },
    {"wifi_testmode_set_type", "(I)I", (void*)wifi_testmode_set_type },
    {"wifi_testmode_set_power", "(I)I", (void*)wifi_testmode_set_power },
    {"wifi_testmode_set_dgain","(I)I", (void*)wifi_testmode_set_dgain },
    {"wifi_testmode_set_rfgain","(I)I", (void*)wifi_testmode_set_rfgain },
    {"wifi_testmode_set_channel_bonding","(I)I", (void*)wifi_testmode_set_channel_bonding },
    {"wifi_testmode_set_channel_bw", "(II)I", (void*)wifi_testmode_set_channel_bw },
    {"wifi_testmode_set_rate_bw", "(II)I", (void*)wifi_testmode_set_rate_bw },
    {"wifi_testmode_set_txpkglen","(I)I", (void*)wifi_testmode_set_txpkglen },
    //[DEGUB] ADD-BEGIN BY TCTNB.Ruili.Liu 1885375 20160431 Add SSID and IP item to WifiTestMode
    {"wifi_testmode_set_power_cntl_mode","(I)I", (void*)wifi_testmode_set_power_cntl_mode },
    {"wifi_testmode_set_ssid","(Ljava/lang/String;)I", (void*)wifi_testmode_set_ssid },
    {"wifi_testmode_set_ip","(Ljava/lang/String;)I", (void*)wifi_testmode_set_ip }
    //[DEGUB] ADD-END BY TCTNB.Ruili.Liu
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
