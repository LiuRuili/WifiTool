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
# ----------|----------------------|----------------------|--------------------
# 23/12/1015| Liu Ruili            |bugID 1209820         |Upgrade WLANTestMode
#           |                      |                      |APK
# ----------|----------------------|----------------------|--------------------
################################################################################

echo "#*************************************************"
echo "# Script usage:"
echo "# $0 <channel #> <bg_rate> <Tx power>"
echo "#"
echo "# Example: test TX with channel 1, 54Mbps, 5db"
echo "# $0 1 54 5"
echo "#*************************************************"

LOG_TAG="qcom-wifi"
LOG_NAME="${0}:"

logi ()
{
  /system/bin/log -t $LOG_TAG -p i "$LOG_NAME $@"
}
logi "enter txbg.sh"
logi "Channel Bonding : $4 Channel : $1 Rate : $2 Power : $3"
logi "Input parameters : $1 $2 $3 $4 $5 $6"

iwpriv wlan0 ftm 1
iwpriv wlan0 tx 0
iwpriv wlan0 ena_chain 2
logi "set_cb $4"
iwpriv wlan0 set_cb $4

if [[ $4 -eq 0 || $4 -eq 2 || $4 -eq 5 ]];then
iwpriv wlan0 set_channel $1
logi "set_channel $1"
elif [[ $4 -eq 1 || $4 -eq 4 || $4 -eq 8 ]];then
iwpriv wlan0 set_channel $1-2
logi "set_channel $1-2"
elif [[ $4 -eq 3 || $4 -eq 6 || $4 -eq 9 ]];then
iwpriv wlan0 set_channel $1+2
logi "set_channel $1+2"
elif [ $4 -eq 7 ];then
iwpriv wlan0 set_channel $1-6
logi "set_channel $1-6"
elif [ $4 -eq 10 ];then
iwpriv wlan0 set_channel $1+6
logi "set_channel $1+6"
fi

logi "pwr_cntl_mode $5"
iwpriv wlan0 pwr_cntl_mode $5
logi "set_txrate $2"
iwpriv wlan0 set_txrate $2

iwpriv wlan0 set_txpktcnt 0
logi "set_txpktlen $6"
iwpriv wlan0 set_txpktlen $6
logi "set_txpower $3"
iwpriv wlan0 set_txpower $3

iwpriv wlan0 tx 1
logi "Script end."
