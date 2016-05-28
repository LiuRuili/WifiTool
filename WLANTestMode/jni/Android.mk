################################################################################
#                                                                  Date:10/2012
#                                 PRESENTATION
#
#         Copyright 2012 TCL Communication Technology Holdings Limited.
#
# This material is company confidential, cannot be reproduced in any form
# without the written permission of TCL Communication Technology Holdings
# Limited.
#
# -----------------------------------------------------------------------------
#  Author :  Chen Ji
#  Email  :  Ji.Chen@tcl-mobile.com
#  Role   :
#  Reference documents : refer bugID200662/161302
# -----------------------------------------------------------------------------
#  Comments :
#  File     : development/apps/WLANTestMode/jni
#  Labels   :
# -----------------------------------------------------------------------------
# =============================================================================
#      Modifications on Features list / Changes Request / Problems Report
# -----------------------------------------------------------------------------
#    date   |        author        |         Key          |      comment
# ----------|----------------------|----------------------|--------------------
# 10/22/2012|Chen Ji               |bugID321787           |makefile document to
#           |                      |                      | control compile
# ----------|----------------------|----------------------|--------------------
################################################################################

LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := eng debug userdebug optional
#LOCAL_MODULE_TAGS := optional debug eng
# This is the target being built.
LOCAL_MODULE:= libwifitestmodejni

# All of the source files that we will compile.
LOCAL_SRC_FILES:= \
  wifitestmode.cpp

# All of the shared libraries we link against.
LOCAL_SHARED_LIBRARIES := \
    libutils \
    libcutils

# No static libraries.
LOCAL_STATIC_LIBRARIES :=

# Also need the JNI headers.
LOCAL_C_INCLUDES += \
    $(JNI_H_INCLUDE)

LOCAL_PRELINK_MODULE := false

include $(BUILD_SHARED_LIBRARY)
