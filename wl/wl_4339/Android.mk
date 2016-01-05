#
# Copyright (C) 2008 Broadcom Corporation
#
# $Id: Android.mk 326356 2012-04-07 20:38:31Z dboyce $
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
LOCAL_PATH:= $(call my-dir)

# Build WL Utility
include $(CLEAR_VARS)
LOCAL_SRC_FILES := \
	wlutil/exe/wlu.c \
	wlutil/exe/wlu_common.c \
	wlutil/exe/wlu_linux.c \
	wlutil/exe/wlu_cmd.c \
	wlutil/exe/wlu_iov.c \
	wlutil/exe/wlu_pipe.c \
	wlutil/exe/wlu_pipe_linux.c \
	wlutil/exe/wlu_client_shared.c \
	wlutil/exe/wlu_rates_matrix.c \
	shared/bcmutils.c \
	shared/bcmwifi/src/bcmwifi_channels.c \
	shared/miniopt.c \
	shared/bcm_app_utils.c \
	wlutil/ppr/src/wlc_ppr.c

LOCAL_MODULE := wl
LOCAL_CFLAGS := -DBCMWPA2 -DTARGETENV_android -DLINUX -Dlinux
LOCAL_CFLAGS += -DRWL_WIFI -DRWL_SOCKET -DRWL_DONGLE -DPPR_API -DWLPFN -DWLPFN_AUTO_CONNECT -DLINUX -DWLOFFLD
LOCAL_CFLAGS += -DWLC_HIGH -DWLNDOE -DWLP2P -DWLMCHAN -DWLTDLS
LOCAL_CFLAGS += -DD11AC_IOTYPES

LOCAL_C_INCLUDES += $(LOCAL_PATH)/include $(LOCAL_PATH)/shared/bcmwifi/include $(LOCAL_PATH)/common/include $(LOCAL_PATH)/wlutil/ppr/include
LOCAL_FORCE_STATIC_EXECUTABLE := true
LOCAL_STATIC_LIBRARIES := libc

LOCAL_MODULE_TAGS := debug eng
include $(BUILD_EXECUTABLE)
