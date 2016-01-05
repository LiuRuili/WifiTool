#!/system/bin/sh

mode=$1
channel=$2
rate=$3
ant=$4
bandwidth=$5
sideband=$6

function logi {
	log -t "WifiTestCmd-ac_rx" -p i $1
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
logi "wl band a"
wl band a

case $bandwidth in
40) logi "wl chanspec $channel$sideband"
    wl chanspec $channel$sideband
    ;;
80) logi "wl chanspec $channel/80"
    wl chanspec $channel/80
    ;;
*)  logi "wl chanspec $channel/20"
    wl chanspec $channel/20
    ;;
esac

logi "wl phy_forcecal 1"
wl phy_forcecal 1
logi "wl scansuppress 1"
wl scansuppress 1
