
LOG_TAG="qcom-wifi-testRxReconnect"
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

logi "start_wifitestRxReconnect"
#start_wifitestRxReconnect ()
#{
  ip=$(cat /data/wl/ip)
  ssid=$(cat /data/wl/ssid)
  logi $ip
  logi $ssid

  sleep 1
  /system/bin/sh /data/wl/RxReconnect.sh $ssid $ip

#  wifitesttx_pid=$$
#  logi "start_wifitestRxReconnect: pid = $wifitesttx_pid"
#}

#kill_wifitestRxReconnect ()
#{
#  logi "kill_wifitestRxReconnect: pid = $wifitesttx_pid"

#  kill -TERM $wifitesttx_pid
#}

# mimic wifitesttx options parsing -- maybe a waste of effort
#USAGE="wifitestRx [-n] [-p] [-b] [-t timeout] [-s initial_speed] <tty> <type | id> [speed] [flow|noflow] [bdaddr]"

#shift $(($OPTIND-1))

# init does SIGTERM on ctl.stop for service
#trap "kill_wifitestRxReconnect" TERM INT

#logi start_wifitestRxReconnect .........................
#start_wifitestRxReconnect

#wait $wifitesttx_pid

#exit 0
