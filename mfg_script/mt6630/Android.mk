LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := WifiTest.sh
LOCAL_MODULE_TAGS := eng debug
LOCAL_MODULE_CLASS := ETC
LOCAL_SRC_FILES := WifiTest.sh
LOCAL_MODULE_PATH := $(TARGET_OUT)/etc/wifi/mfg_script
include $(BUILD_PREBUILT)

include $(CLEAR_VARS)
LOCAL_MODULE := mt6630/tx.sh
LOCAL_MODULE_TAGS := eng debug
LOCAL_MODULE_CLASS := ETC
LOCAL_SRC_FILES := tx.sh
LOCAL_MODULE_PATH := $(TARGET_OUT)/etc/wifi/mfg_script
include $(BUILD_PREBUILT)

include $(CLEAR_VARS)
LOCAL_MODULE := mt6630/rx.sh
LOCAL_MODULE_TAGS := eng debug
LOCAL_MODULE_CLASS := ETC
LOCAL_SRC_FILES := rx.sh
LOCAL_MODULE_PATH := $(TARGET_OUT)/etc/wifi/mfg_script
include $(BUILD_PREBUILT)

include $(CLEAR_VARS)
LOCAL_MODULE := mt6630/cw_tx.sh
LOCAL_MODULE_TAGS := eng debug
LOCAL_MODULE_CLASS := ETC
LOCAL_SRC_FILES := cw_tx.sh
LOCAL_MODULE_PATH := $(TARGET_OUT)/etc/wifi/mfg_script
include $(BUILD_PREBUILT)
