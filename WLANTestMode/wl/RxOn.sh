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
# 11/30/2012|Chen Ji               |bugID329061           |scripts are excuted
#           |                      |                      |for WLANTest
# ----------|----------------------|----------------------|--------------------
################################################################################

echo "#*************************************************"
echo "# Script usage:"
echo "# $0 <channel #> <legacy rate>"
echo "#"
echo "# Example: test RX with channel 1, 11Mbps"
echo "# $0 1 11"
echo "# Note: Please have an adhoc(name:test) network ready."
echo "#*************************************************"
LOG_TAG="qcom-wifi"
LOG_NAME="${0}:"

logi ()
{
  /system/bin/log -t $LOG_TAG -p i "$LOG_NAME $@"
}
logi "enter rx.sh"
logi "$1"

sleep 1
logi "ifconfig wlan0 up"
echo "ifconfig wlan0 up"
ifconfig wlan0 up
sleep 1
logi "#athtestcmd -i wlan0 --rx promis --rxfreq $1"
echo "#athtestcmd -i wlan0 --rx promis --rxfreq $1"
ptt_socket_app
iwpriv wlan0 ftm 1
iwpriv wlan0 ena_chain 1
iwpriv wlan0 set_channel $1
iwpriv wlan0 rx 0
iwpriv wlan0 clr_rxpktcnt 1
iwpriv wlan0 rx 1
logi "Script end."
echo "Script end."
