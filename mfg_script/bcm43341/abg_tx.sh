#!/system/bin/sh

mode=$1
channel=$2
rate=$3
ant=$4
power=$5

function logi {
	log -t "WifiTestCmd-abg_tx" -p i $1
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
logi "wl txpwr1 -o -d $power"
wl txpwr1 -o -d $power
logi "wl phy_watchdog 0"
wl phy_watchdog 0
logi "wl up"
wl up
logi "wl nrate -r $rate"
wl nrate -r $rate
logi "wl pkteng_start 00:11:22:33:44:55 tx 100 1000 0"
wl pkteng_start 00:11:22:33:44:55 tx 100 1000 0
