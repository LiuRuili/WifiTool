#!/system/bin/sh

mode=$1
channel=$2

function logi {
	log -t "WifiTestCmd-cw_tx" -p i $1
}

result=$(wifitesttool -t 0 -n 0 -m 3 -c $channel -R 1 -S 0)
logi "$result"

