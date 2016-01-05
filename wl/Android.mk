LOCAL_PATH := $(call my-dir)

ifneq (,$(filter ME581CL ME581C Z581C SANTA,$(TARGET_PROJECT)))
    include $(LOCAL_PATH)/wl_4339/Android.mk
endif

ifneq (,$(filter TV500I,$(TARGET_PROJECT)))
    include $(LOCAL_PATH)/wl_4354/Android.mk
endif

ifneq (,$(filter ME375CL FE375CL ME176C-L RD001 Z300CL,$(TARGET_PROJECT)))
    include $(LOCAL_PATH)/wl_43430/Android.mk
endif

ifneq (,$(filter FE170CG ME170C ME70C TF103CG FE375CG ME572C ME572CL ME176C ME181C TF701T ME560CG TF303CL TF103C TF103CE ME176CE,$(TARGET_PROJECT)))
    include $(LOCAL_PATH)/wl_4334/Android.mk
endif
