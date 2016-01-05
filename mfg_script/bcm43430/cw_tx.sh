#!/system/bin/sh

mode=$1
channel=$2

function logi {
	log -t "WifiTestCmd-cw_tx" -p i $1
}

logi "wl down"
wl down
logi "wl mpc 0"
wl mpc 0
logi "wl up"
wl up
logi "wl scansuppress 1"
wl scansuppress 1
logi "wl phy_watchdog 0"
wl phy_watchdog 0

if [ $channel -lt 15 ]; then
logi "wl band b"
wl band b
else
logi "wl band a"
wl band a
fi

logi "wl phy_txpwrctrl 0"
wl phy_txpwrctrl 0
logi "wl phy_txpwrindex 0"
wl phy_txpwrindex 0
logi "wl txchain 1"
wl txchain 1
logi "wl fqacurcy $channel"
wl fqacurcy $channel
