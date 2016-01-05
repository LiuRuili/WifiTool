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
logi "wl phy_watchdog 0"
wl phy_watchdog 0
logi "wl up"
wl up
logi "wl country ALL"
wl country ALL

if [ $channel -lt 15 ]; then
logi "wl band b"
wl band b
else 
logi "wl band a"
wl band a
fi

logi "wl chanspec $channel"
wl chanspec $channel
logi "wl phy_forcecal 1"
wl phy_forcecal 1
logi "wl scansuppress 1"
wl scansuppress 1
