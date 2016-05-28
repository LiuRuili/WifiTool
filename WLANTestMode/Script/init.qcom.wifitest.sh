
LOG_TAG="qcom-wifi"
LOG_NAME="${0}:"

wifitest_pid=""

loge ()
{
  /system/bin/log -t $LOG_TAG -p e "$LOG_NAME $@"
}

logi ()
{
  /system/bin/log -t $LOG_TAG -p i "$LOG_NAME $@"
}

start_wifitest ()
{
  loge "netcfg wlan0 up"
  #netcfg wlan0 up
  wifitest_pid=$!
  logi "start_wifitest: pid = $wifitest_pid"
}

kill_wifitest ()
{
  logi "kill_wifitest: pid = $wifitest_pid"

  kill -TERM $wifitest_pid
}

# mimic wifitest options parsing -- maybe a waste of effort
USAGE="wifitest [-n] [-p] [-b] [-t timeout] [-s initial_speed] <tty> <type | id> [speed] [flow|noflow] [bdaddr]"

shift $(($OPTIND-1))

# init does SIGTERM on ctl.stop for service
trap "kill_wifitest" TERM INT

start_wifitest

wait $wifitest_pid

logi "wifi test mode stopped"

exit 0
