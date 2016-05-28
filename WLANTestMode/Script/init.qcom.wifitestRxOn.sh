
LOG_TAG="qcom-wifi-testRx"
LOG_NAME="${0}:"

wifitesttx_pid=""

loge ()
{
  /system/bin/log -t $LOG_TAG -p e "$LOG_NAME $@"
}

logi ()
{
  /system/bin/log -t $LOG_TAG -p i "$LOG_NAME $@"
}

logi "start_wifitestRxOn"
# start_wifitestRxOn()
#{
  channel_bonding=$(cat /data/wl/channel_bonding)
  channel=$(cat /data/wl/channel)
  rate=$(cat /data/wl/rate)
  logi $channel $rate $channel_bonding

  sleep 1
  /system/bin/sh /data/wl/RxOn.sh $channel $rate $channel_bonding

#  wifitesttx_pid=$$
#  logi "start_wifitestRxon: pid = $wifitesttx_pid"
#}

#kill_wifitestRxOn ()
#{
#  logi "kill_wifitestRxon: pid = $wifitesttx_pid"

#  kill -TERM $wifitesttx_pid
#}

# mimic wifitesttx options parsing -- maybe a waste of effort
#USAGE="wifitestRx [-n] [-p] [-b] [-t timeout] [-s initial_speed] <tty> <type | id> [speed] [flow|noflow] [bdaddr]"

#shift $(($OPTIND-1))

# init does SIGTERM on ctl.stop for service
#trap "kill_wifitestRxOn" TERM INT

#logi start....................
#start_wifitestRxOn
#logi waiit pid
#wait $wifitesttx_pid
#logi waiit endexit

#exit 0