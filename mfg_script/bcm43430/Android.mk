LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := WifiTest.sh
LOCAL_MODULE_TAGS := eng debug
LOCAL_MODULE_CLASS := ETC
LOCAL_SRC_FILES := WifiTest.sh
LOCAL_MODULE_PATH := $(TARGET_OUT)/etc/wifi/mfg_script
include $(BUILD_PREBUILT)

include $(CLEAR_VARS)
LOCAL_MODULE := bcm43430/abg_tx.sh
LOCAL_MODULE_TAGS := eng debug
LOCAL_MODULE_CLASS := ETC
LOCAL_SRC_FILES := abg_tx.sh
LOCAL_MODULE_PATH := $(TARGET_OUT)/etc/wifi/mfg_script
include $(BUILD_PREBUILT)

include $(CLEAR_VARS)
LOCAL_MODULE := bcm43430/abg_rx.sh
LOCAL_MODULE_TAGS := eng debug
LOCAL_MODULE_CLASS := ETC
LOCAL_SRC_FILES := abg_rx.sh
LOCAL_MODULE_PATH := $(TARGET_OUT)/etc/wifi/mfg_script
include $(BUILD_PREBUILT)

include $(CLEAR_VARS)
LOCAL_MODULE := bcm43430/n_tx.sh
LOCAL_MODULE_TAGS := eng debug
LOCAL_MODULE_CLASS := ETC
LOCAL_SRC_FILES := n_tx.sh
LOCAL_MODULE_PATH := $(TARGET_OUT)/etc/wifi/mfg_script
include $(BUILD_PREBUILT)

include $(CLEAR_VARS)
LOCAL_MODULE := bcm43430/n_rx.sh
LOCAL_MODULE_TAGS := eng debug
LOCAL_MODULE_CLASS := ETC
LOCAL_SRC_FILES := n_rx.sh
LOCAL_MODULE_PATH := $(TARGET_OUT)/etc/wifi/mfg_script
include $(BUILD_PREBUILT)

include $(CLEAR_VARS)
LOCAL_MODULE := bcm43430/cw_tx.sh
LOCAL_MODULE_TAGS := eng debug
LOCAL_MODULE_CLASS := ETC
LOCAL_SRC_FILES := cw_tx.sh
LOCAL_MODULE_PATH := $(TARGET_OUT)/etc/wifi/mfg_script
include $(BUILD_PREBUILT)
