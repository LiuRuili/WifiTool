LOCAL_PATH := $(call my-dir)

ifneq (,$(filter P1801-T P1802-T,$(TARGET_PROJECT)))
    include $(LOCAL_PATH)/bcm4324/Android.mk
endif

ifneq (,$(filter ME581CL ME581C Z581C SANTA,$(TARGET_PROJECT)))
    include $(LOCAL_PATH)/bcm4339/Android.mk
endif

ifneq (,$(filter TV500I,$(TARGET_PROJECT)))
    include $(LOCAL_PATH)/bcm4354/Android.mk
endif

ifneq (,$(filter ME560CG TF701T TF303CL ME176CE TF103C TF103CE,$(TARGET_PROJECT)))
    include $(LOCAL_PATH)/bcm43341/Android.mk
endif

ifneq (,$(filter ME375CL FE375CL ME176C-L RD001 Z300CL,$(TARGET_PROJECT)))
    include $(LOCAL_PATH)/bcm43430/Android.mk
endif

ifneq (,$(filter FE375CG ME572C ME572CL TF103CG FE170CG ME181C K013 K011,$(TARGET_PROJECT)))
    include $(LOCAL_PATH)/bcm43362/Android.mk
endif
