
LOG_TAG="qcom-wifi"
LOG_NAME="${0}:"

wifitestwg_pid=""

loge ()
{
  /system/bin/log -t $LOG_TAG -p e "$LOG_NAME $@"
}

logi ()
{
  /system/bin/log -t $LOG_TAG -p i "$LOG_NAME $@"
}

logi "start_wifitestwg"
#start_wifitesttxn()
#{
  channel=$(cat /data/wl/channel)
  #digitalgain=$(cat /data/wl/dgain)
  #rfgain=$(cat /data/wl/rfgain)

  #/system/bin/sh /data/wl/txstop.sh tx
  sleep 1
  #/system/bin/sh /data/wl/wavegenerator.sh $channel $digitalgain $rfgain
  /system/bin/sh /data/wl/wavegenerator.sh $channel

#  wifitesttxn_pid=$!
#  logi "start_wifitesttxn: pid = $wifitesttxn_pid"
#}

#kill_wifitesttxn ()
#{
#  logi "kill_wifitesttxn: pid = $wifitesttxn_pid"

#  kill -TERM $wifitesttxn_pid
#}

# mimic wifitesttxn options parsing -- maybe a waste of effort
#USAGE="wifitesttxn [-n] [-p] [-b] [-t timeout] [-s initial_speed] <tty> <type | id> [speed] [flow|noflow] [bdaddr]"

#shift $(($OPTIND-1))

# init does SIGTERM on ctl.stop for service
#trap "kill_wifitesttxn" TERM INT

#start_wifitesttxn

#wait $wifitesttxn_pid

#exit 0
