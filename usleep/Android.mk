LOCAL_PATH := $(call my-dir)

ifneq (,$(filter P1801-T P1802-T Z170C Z170CG Z370C Z370CG,$(TARGET_PROJECT)))
include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := eng debug
LOCAL_MODULE := usleep
LOCAL_SRC_FILES := usleep.c
LOCAL_MODULE_CLASS := EXECUTABLES
include $(BUILD_EXECUTABLE)
endif