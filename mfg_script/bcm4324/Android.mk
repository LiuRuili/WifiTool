LOCAL_PATH := $(call my-dir)

# For bcm4324
include $(CLEAR_VARS)
LOCAL_MODULE := WifiTest.sh
LOCAL_MODULE_TAGS := eng debug
LOCAL_MODULE_CLASS := ETC
LOCAL_SRC_FILES := WifiTest.sh
LOCAL_MODULE_PATH := $(TARGET_OUT)/etc/wifi/mfg_script
include $(BUILD_PREBUILT)

include $(CLEAR_VARS)
LOCAL_MODULE := bcm4324/abg_tx.sh
LOCAL_MODULE_TAGS := eng debug
LOCAL_MODULE_CLASS := ETC
LOCAL_SRC_FILES := abg_tx.sh
LOCAL_MODULE_PATH := $(TARGET_OUT)/etc/wifi/mfg_script
include $(BUILD_PREBUILT)

include $(CLEAR_VARS)
LOCAL_MODULE := bcm4324/abg_rx.sh
LOCAL_MODULE_TAGS := eng debug
LOCAL_MODULE_CLASS := ETC
LOCAL_SRC_FILES := abg_rx.sh
LOCAL_MODULE_PATH := $(TARGET_OUT)/etc/wifi/mfg_script
include $(BUILD_PREBUILT)

include $(CLEAR_VARS)
LOCAL_MODULE := bcm4324/n_tx.sh
LOCAL_MODULE_TAGS := eng debug
LOCAL_MODULE_CLASS := ETC
LOCAL_SRC_FILES := n_tx.sh
LOCAL_MODULE_PATH := $(TARGET_OUT)/etc/wifi/mfg_script
include $(BUILD_PREBUILT)

include $(CLEAR_VARS)
LOCAL_MODULE := bcm4324/n_rx.sh
LOCAL_MODULE_TAGS := eng debug
LOCAL_MODULE_CLASS := ETC
LOCAL_SRC_FILES := n_rx.sh
LOCAL_MODULE_PATH := $(TARGET_OUT)/etc/wifi/mfg_script
include $(BUILD_PREBUILT)

include $(CLEAR_VARS)
LOCAL_MODULE := bcm4324/n_tx_mimo.sh
LOCAL_MODULE_TAGS := eng debug
LOCAL_MODULE_CLASS := ETC
LOCAL_SRC_FILES := n_tx_mimo.sh
LOCAL_MODULE_PATH := $(TARGET_OUT)/etc/wifi/mfg_script
include $(BUILD_PREBUILT)

include $(CLEAR_VARS)
LOCAL_MODULE := bcm4324/n_rx_mimo.sh
LOCAL_MODULE_TAGS := eng debug
LOCAL_MODULE_CLASS := ETC
LOCAL_SRC_FILES := n_rx_mimo.sh
LOCAL_MODULE_PATH := $(TARGET_OUT)/etc/wifi/mfg_script
include $(BUILD_PREBUILT)

include $(CLEAR_VARS)
LOCAL_MODULE := bcm4324/cw_tx.sh
LOCAL_MODULE_TAGS := eng debug
LOCAL_MODULE_CLASS := ETC
LOCAL_SRC_FILES := cw_tx.sh
LOCAL_MODULE_PATH := $(TARGET_OUT)/etc/wifi/mfg_script
include $(BUILD_PREBUILT)
