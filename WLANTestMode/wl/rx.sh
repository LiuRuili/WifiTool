#!/system/bin/sh
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
athtestcmd -i wlan0 --rx promis --rxfreq 6
logi "Script end."
echo "Script end."
