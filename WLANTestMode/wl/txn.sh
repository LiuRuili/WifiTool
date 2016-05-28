
echo "#*************************************************"
echo "# Script usage:"
echo "# $0 <channel #> <mcs index> <Tx power>"
echo "#"
echo "# Example: test TX with channel 1, mcs 7, 5db"
echo "# $0 1 7 5"
echo "#*************************************************"
LOG_TAG="qcom-wifi"
LOG_NAME="${0}:"

logi ()
{
  /system/bin/log -t $LOG_TAG -p i "$LOG_NAME $@"
}
logi "enter signaling (txn.sh)"

#logi "ifconfig wlan0 up"

#insmod /system/lib/modules/cfg80211.ko
insmod /system/lib/modules/wlan.ko

#ifconfig wlan0 up

#sleep 1

#wmiconfig -i wlan0 --roam 4 65535 0 0 0

sleep 2

logi "iwpriv wlan0 setPower Disable BMPS"

iwpriv wlan0 setPower 2

sleep 1

iwlist wlan0 scan

sleep 2

iwconfig wlan0 essid $1

logi "iwconfig wlan0 essid $1"

sleep 1

ifconfig wlan0 $2 up

logi "ifconfig wlan0 $2 up"

#logi "echo 0 >/mnt/debugfs/ieee80211/phy0/ath6kl/bgscan_interval"

#echo 0 >/mnt/debugfs/ieee80211/phy0/ath6kl/bgscan_interval


#sleep 1
#wmiconfig -i wlan0 --roam 4 65535 0 0 0
#sleep 1
#wmiconfig -i wlan0 --scan -bg=0
#sleep 1
#wmiconfig -i wlan0 --power maxperf
#sleep 1
echo "Script end."
logi "Script end."
