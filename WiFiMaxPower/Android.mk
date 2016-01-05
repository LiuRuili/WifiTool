LOCAL_PATH := $(call my-dir)

ifneq (,$(filter Z370KL,$(TARGET_PROJECT)))
    include $(LOCAL_PATH)/qcom/Android.mk
else
    ifneq (,$(filter full_asus8173_tb_a8s_l1 full_asus8176_tb_a8_m P026,$(TARGET_PRODUCT)))
        include $(LOCAL_PATH)/mtk/Android.mk
    else
        include $(LOCAL_PATH)/bcm/Android.mk
    endif
endif
