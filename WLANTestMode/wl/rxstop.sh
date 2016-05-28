#!/system/bin/sh

echo "#*************************************************"
echo "# Script usage:"
echo "# rx"
echo "#"
echo "# Example: to stop rx"
echo "# rx"
echo "#*************************************************"
LOG_TAG="qcom-wifi"
LOG_NAME="${0}:"

logi ()
{
  /system/bin/log -t $LOG_TAG -p i "$LOG_NAME $@"
}
logi "./athtestcmd -i wlan0 --rx report "
echo "./athtestcmd -i wlan0 --rx report  "
cd /data/wl
myftm -x 0>rxreport
echo "Script end."
