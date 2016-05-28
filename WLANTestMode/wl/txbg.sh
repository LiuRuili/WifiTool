
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
logi "Input parameters : $1 $2 $3 $4 $5 "

myftm -M $4 -a 1 -r $2 -c 0 -p $3 -f $1 -j $5 -t 3
logi "Script end."
