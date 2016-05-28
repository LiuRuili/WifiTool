
LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := eng debug userdebug optional
#LOCAL_MODULE_TAGS := optional debug eng
# This is the target being built.
LOCAL_MODULE:= libwifitestmodejni

# All of the source files that we will compile.
LOCAL_SRC_FILES:= \
  wifitestmode.cpp

# All of the shared libraries we link against.
# [DEGUB] MOD-BEGIN BY TCTNB.Ruili.Liu 1885375 20160431 Add SSID and IP item to WifiTestMode
LOCAL_SHARED_LIBRARIES := \
    libutils \
    libcutils \
    libnativehelper
# [DEGUB] MOD-END BY TCTNB.Ruili.Liu

# No static libraries.
LOCAL_STATIC_LIBRARIES :=

# Also need the JNI headers.
LOCAL_C_INCLUDES += \
    $(JNI_H_INCLUDE)

LOCAL_PRELINK_MODULE := false

include $(BUILD_SHARED_LIBRARY)
