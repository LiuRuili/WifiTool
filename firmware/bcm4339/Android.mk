LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := bcm4339/fw_bcmdhd_mfg.bin
LOCAL_MODULE_TAGS := eng debug
LOCAL_MODULE_CLASS := EXECUTABLES
LOCAL_SRC_FILES := fw_bcmdhd_mfg.bin
LOCAL_MODULE_PATH := $(TARGET_OUT)/vendor/firmware
include $(BUILD_PREBUILT)
