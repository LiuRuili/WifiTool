#!/bin/bash

function logi {
	log -t "WifiTestWatchdong" -p i $1
}
while getopts "t:" opt; do
  case "$opt" in
      t) timeout=$OPTARG ;;
  esac
done
shift $((OPTIND-1))
 
start_watchdog(){
#  timeout="$1"
  timeout=$(($1*1000000))
  (( i = timeout ))
  while (( i > 0 ))
  do
    kill -0 $$ || exit 0
    usleep 100000
    (( i -= 100000 ))
  done
 
  logi "WifiTest command timeout"
  echo "FAIL"
  system/xbin/busybox pkill -SIGKILL at_cli_client
  kill -SIGKILL $$
}
 
start_watchdog "$timeout" 2>/dev/null &
exec "$@"
