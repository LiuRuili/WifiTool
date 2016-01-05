#!/system/bin/sh

mode=$1
channel=$2
rate=$3
ant=$4
power=$5

function logi {
	log -t "WifiTestCmd-abg_tx" -p i $1
}

logi "wl down"
wl down
logi "wl mpc 0"
wl mpc 0
logi "wl phy_watchdog 0"
wl phy_watchdog 0
logi "wl country ALL"
wl country ALL

if [ $channel -lt 15 ]; then
logi "wl band b"
wl band b
logi "wl 2g_rate -r $rate"
wl 2g_rate -r $rate
else
logi "wl band a"
wl band a
logi "wl 5g_rate -r $rate"
wl 5g_rate -r $rate
fi

logi "wl channel $channel"
wl channel $channel
logi "wl scansuppress 1"
wl scansuppress 1
logi "wl txpwr1 -o -d $power"
wl txpwr1 -o -d $power
logi "wl up"
wl up
logi "wl phy_forcecal 1"
wl phy_forcecal 1
logi "wl pkteng_start 00:11:22:33:44:55 tx 100 1024 0"
wl pkteng_start 00:11:22:33:44:55 tx 100 1024 0
