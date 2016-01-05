LOCAL_PATH := $(call my-dir)
#
# wl34
#
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := eng debug

LOCAL_SRC_FILES := \
	src/wlu.c \
	src/wlu_common.c \
	src/wlu_linux.c \
	src/wlu_cmd.c \
	src/wlu_iov.c \
	src/wlu_pipe.c \
	src/wlu_pipe_linux.c \
	src/wlu_client_shared.c \
	src/bcmutils.c \
	src/bcm_app_utils.c \
	src/bcmwifi.c \
	src/miniopt.c

LOCAL_MODULE := wl

LOCAL_CFLAGS := \
	-DBCMWPA2 \
	-DTARGETENV_android \
	-DLINUX \
	-Dlinux \
	-DRWL_WIFI \
	-DRWL_SOCKET \
	-DRWL_DONGLE

LOCAL_C_INCLUDES := $(LOCAL_PATH)/include
LOCAL_FORCE_STATIC_EXECUTABLE := true
LOCAL_STATIC_LIBRARIES := \
	libc

include $(BUILD_EXECUTABLE)
