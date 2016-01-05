LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := WifiTest
LOCAL_MODULE_TAGS := eng debug
LOCAL_SHARED_LIBRARIES := libcutils
LOCAL_MODULE_CLASS := EXECUTABLES
LOCAL_SRC_FILES := WifiTest.c
LOCAL_MODULE_PATH := $(TARGET_OUT)/bin
include $(BUILD_EXECUTABLE)

include $(CLEAR_VARS)
LOCAL_MODULE := WifiSetup
LOCAL_MODULE_TAGS := eng debug
LOCAL_SHARED_LIBRARIES := libcutils
LOCAL_MODULE_CLASS := EXECUTABLES
LOCAL_SRC_FILES := WifiSetup.c
LOCAL_MODULE_PATH := $(TARGET_OUT)/bin
include $(BUILD_EXECUTABLE)
