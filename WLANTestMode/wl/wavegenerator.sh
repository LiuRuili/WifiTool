
echo "#*************************************************"
echo "# Script usage:"
echo "# $0 <Channel> <Digital Gain> and <RF Gain>"
echo "#"
echo "# Example: test TX with channel 1 digital gain 20,rf gain 23"
echo "# $0 1 20 23"
echo "#*************************************************"

LOG_TAG="qcom-wifi"
LOG_NAME="${0}:"

logi ()
{
  /system/bin/log -t $LOG_TAG -p i "$LOG_NAME $@"
}
logi "enter wavegenerator.sh"
logi "$1 $2 $3"
sleep 1
logi "#start WLAN wave generator test mode with channel $1, digital gain $2, and RF gain $3"
echo "#start WLAN wave generator test mode with channel $1, digital gain $2, and RF gain $3"
iwpriv wlan0 ftm 1
iwpriv wlan0 tx 0
iwpriv wlan0 ena_chain 2
iwpriv wlan0 set_channel $1
iwpriv wlan0 tx_cw_rf_gen 0
iwpriv wlan0 set_tx_wf_gain $2 $3
iwpriv wlan0 tx_cw_rf_gen 1
logi "Script end."
echo "Script end."
