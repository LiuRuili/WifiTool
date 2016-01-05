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

ifneq (,$(filter Z170C Z170CG Z370C Z370CG,$(TARGET_PRODUCT)))
    include $(LOCAL_PATH)/ag620/Android.mk
endif

ifneq (,$(filter Z370CL,$(TARGET_PROJECT)))
    include $(LOCAL_PATH)/lnp/Android.mk
endif

ifneq (,$(filter Z370KL,$(TARGET_PROJECT)))
    include $(LOCAL_PATH)/wcn3610/Android.mk
endif

ifneq (,$(filter full_asus8173_tb_a8s_l1 full_asus8176_tb_a8_m P026,$(TARGET_PRODUCT)))
    include $(LOCAL_PATH)/mt6630/Android.mk
endif
