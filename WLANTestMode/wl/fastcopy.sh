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
#  File     : development/apps/WLANTestMode/wl
#  Labels   :
# -----------------------------------------------------------------------------
# =============================================================================
#      Modifications on Features list / Changes Request / Problems Report
# -----------------------------------------------------------------------------
#    date   |        author        |         Key          |      comment
# ----------|----------------------|----------------------|--------------------
# 10/22/2012|Chen Ji               |bugID321787           |scripts are excuted
#           |                      |                      |for WLANTest
# ----------|----------------------|----------------------|--------------------
################################################################################

sudo adb remount
#sudo adb push RxOn.sh /data/wl
#sudo adb push rmmod_wifi.sh /data/wl
#sudo adb push rx.sh /data/wl
#sudo adb push rxstop.sh /data/wl
#sudo adb push txbg_fixed.sh /data/wl
#sudo adb push txunmod.sh /data/wl
#sudo adb push rxn.sh /data/wl
#sudo adb push RxReconnect.sh /data/wl
#sudo adb push txbg.sh /data/wl
sudo adb push txn.sh /data/wl

