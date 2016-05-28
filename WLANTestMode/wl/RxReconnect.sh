#!/system/bin/sh
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
logi "RxReconnect.sh"
logi "$1 $2"
/data/wl/wlarm disassoc
sleep 2
/data/wl/wlarm scansuppress 0
sleep 2
/data/wl/wlarm scan
sleep 2
/data/wl/wlarm scanresults
sleep 2
/data/wl/wlarm join $1
sleep 2
ifconfig wlan0 $2 up
sleep 2
/data/wl/wlarm scansuppress 1
sleep 2
/data/wl/wlarm PM 0
sleep 2
/data/wl/wlarm assoc
