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
#  Email  :  JiChen@tcl.com
#  Role   :
#  Reference documents : refer bugID200662/161302
# -----------------------------------------------------------------------------
#  Comments :
#  File     : development/apps/WLANTestMode
#  Labels   :
# -----------------------------------------------------------------------------
# =============================================================================
#      Modifications on Features list / Changes Request / Problems Report
# -----------------------------------------------------------------------------
#    date   |        author        |         Key          |      comment
# ----------|----------------------|----------------------|--------------------
# 11/18/2013|Chen Ji               |        553538        |makefile document to
#           |                      |                      | control compile
# ----------|----------------------|----------------------|--------------------
################################################################################
#ifeq ($(HAVE_WIFI_TEST),true)
LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

#LOCAL_JAVA_LIBRARIES := frameworks_ext
LOCAL_JAVA_LIBRARIES := org.apache.http.legacy 

#LOCAL_MODULE_TAGS := user system eng
LOCAL_MODULE_TAGS := eng debug userdebug optional

LOCAL_SRC_FILES := $(call all-subdir-java-files)

LOCAL_PACKAGE_NAME := WLANTestMode
LOCAL_CERTIFICATE := platform

#LOCAL_JNI_SHARED_LIBRARIES := libwifitestmodejni

LOCAL_OVERRIDES_PACKAGES := Home

include $(BUILD_PACKAGE)
sinclude $(call all-makefiles-under,$(LOCAL_PATH))
#endif
