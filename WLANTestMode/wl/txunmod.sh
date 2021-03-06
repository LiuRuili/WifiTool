#!/system/bin/sh

echo "#*************************************************"
echo "# Script usage:"
echo "# $0 <channel #>"
echo "#"
echo "# Example: Send continuous TX single tone at channel 1"
echo "# $0 1"
echo "# Example: STOP continuous TX single tone"
echo "# $0 0"
echo "#*************************************************"
LOG_TAG="qcom-wifi"
LOG_NAME="${0}:"
logi ()
{
  /system/bin/log -t $LOG_TAG -p i "$LOG_NAME $@"
}
logi "enter txunmod.sh"

logi "$1 $2"
sleep 1
insmod /system/lib/modules/wlan.ko "con_mode=5"
sleep 1
logi "ifconfig wlan0 up"
echo "ifconfig wlan0 up"
ifconfig wlan0 up
sleep 1
logi "athtestcmd -i wlan0 --tx sine --txfreq $1 --txpwr 1"
echo "athtestcmd -i wlan0 --tx sine --txfreq $1 --txpwr 1"

iwpriv wlan0 set_channel $1
iwpriv wlan0 set_txpower 9
iwpriv wlan0 tx 1
sleep 1
echo "Script end."
