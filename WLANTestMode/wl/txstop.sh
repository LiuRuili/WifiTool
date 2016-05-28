#!/system/bin/sh

echo "#*************************************************"
echo "# Script usage:"
echo "# tx"
echo "#"
echo "# Example: to stop tx"
echo "# tx"
echo "#*************************************************"

echo "#stop TX mode "
echo "./athtestcmd -i wlan0 --tx off "

LOG_TAG="qcom-wifi"
LOG_NAME="${0}:"

logi ()
{
  /system/bin/log -t $LOG_TAG -p i "$LOG_NAME $@"
}

logi "enter txstop.sh"
iwpriv wlan0 ftm 0
echo "Script end."
