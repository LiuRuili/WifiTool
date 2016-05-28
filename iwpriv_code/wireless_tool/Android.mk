LOCAL_PATH := $(call my-dir)

WT_INCS := -I$(LOCAL_PATH)
WT_DEFS := -W -Wall -Wstrict-prototypes -Wmissing-prototypes -Wshadow -Wpointer-arith -Wcast-qual -Winline
WT_DEFS += -DWT_NOLIMB=y

include $(CLEAR_VARS)
LOCAL_SRC_FILES := iwlib.c
LOCAL_MODULE_TAGS := eng debug
LOCAL_MODULE := iwlib
LOCAL_CFLAGS := $(WT_INCS) $(WT_DEFS)
include $(BUILD_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_SRC_FILES := iwpriv.c
LOCAL_MODULE_TAGS := eng debug
LOCAL_MODULE_CLASS := EXECUTABLES
LOCAL_MODULE := iwpriv
LOCAL_STATIC_LIBRARIES := iwlib
LOCAL_CFLAGS := $(WT_INCS) $(WT_DEFS)
include $(BUILD_EXECUTABLE)
# Rubio- add for Wifi factory test
