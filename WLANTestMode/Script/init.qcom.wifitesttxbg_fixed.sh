
LOG_TAG="qcom-wifi"
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

logi "start_wifitesttx"
#start_wifitesttx()
#{
  channel=$(cat /data/wl/channel)
  rate=$(cat /data/wl/rate)
  power=$(cat /data/wl/power)

  /system/bin/sh  /data/wl/txstop.sh
  sleep 1
   /system/bin/sh  /data/wl/txbg_fixed.sh $channel $rate $power

#  wifitesttx_pid=$!
 # logi "start_wifitesttx: pid = $wifitesttx_pid"
#}

#kill_wifitesttx ()
#{
# logi "kill_wifitesttx: pid = $wifitesttx_pid"

#  kill -TERM $wifitesttx_pid
#}

# mimic wifitesttx options parsing -- maybe a waste of effort
#USAGE="wifitesttx [-n] [-p] [-b] [-t timeout] [-s initial_speed] <tty> <type | id> [speed] [flow|noflow] [bdaddr]"

#shift $(($OPTIND-1))

# init does SIGTERM on ctl.stop for service
#trap "kill_wifitesttx" TERM INT

#start_wifitesttx

#wait $wifitesttx_pid

#exit 0
