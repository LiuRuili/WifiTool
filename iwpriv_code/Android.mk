LOCAL_PATH := $(call my-dir)

ifneq (,$(filter Z370KL,$(TARGET_PROJECT)))
    include $(LOCAL_PATH)/wireless_tool/Android.mk
endif
