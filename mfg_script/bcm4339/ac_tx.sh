#!/system/bin/sh

mode=$1
channel=$2
rate=$3
ant=$4
power=$5
bandwidth=$6
sideband=$7

function logi {
	log -t "WifiTestCmd-ac_tx" -p i $1
}

logi "wl down"
wl down
logi "wl mpc 0"
wl mpc 0
logi "wl country ALL"
wl country ALL
logi "wl phy_watchdog 0"
wl phy_watchdog 0
logi "wl band a"
wl band a
logi "wl 5g_rate -v $rate -s 1 -b $bandwidth"
wl 5g_rate -v $rate -s 1 -b $bandwidth

case $bandwidth in
40) logi "wl chanspec $channel$sideband"
    wl chanspec $channel$sideband
    ;;
80) logi "wl chanspec $channel/$bandwidth"
    wl chanspec $channel/$bandwidth
    ;;
*)  logi "wl chanspec $channel/$bandwidth"
    wl chanspec $channel/$bandwidth
    ;;
esac

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
