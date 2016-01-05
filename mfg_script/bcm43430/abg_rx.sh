#!/system/bin/sh

mode=$1
channel=$2
rate=$3
ant=$4

function logi {
	log -t "WifiTestCmd-abg_rx" -p i $1
}

logi "wl ver"
wl ver
logi "wl mpc 0"
wl mpc 0
logi "wl down"
wl down
logi "wl scansuppress 1"
wl scansuppress 1
logi "wl country ALL"
wl country ALL
logi "wl chanspec $channel"
wl chanspec $channel
logi "wl phy_watchdog 0"
wl phy_watchdog 0
logi "wl up"
wl up
logi "wl pkteng_start 00:11:22:33:44:66 rx"
wl pkteng_start 00:11:22:33:44:66 rx
