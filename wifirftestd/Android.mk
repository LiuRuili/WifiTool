LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := wifirftestd
LOCAL_MODULE_TAGS := eng debug
LOCAL_SHARED_LIBRARIES := libcutils
LOCAL_MODULE_CALSS := EXECUTABLES
LOCAL_SRC_FILES := server.c
include $(BUILD_EXECUTABLE)
