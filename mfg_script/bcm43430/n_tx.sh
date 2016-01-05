#!/system/bin/sh

mode=$1
channel=$2
rate=$3
ant=$4
power=$5
bandwidth=$6
sideband=$7

function logi {
	log -t "WifiTestCmd-n_tx" -p i $1
}

logi "wl ver"
wl ver
logi "wl mpc 0"
wl mpc 0
logi "wl down"
wl down
logi "wl country ALL"
wl country ALL
logi "wl interference 0"
wl interference 0
logi "wl scansuppress 1"
wl scansuppress 1
logi "wl mimo_bw_cap 1"
wl mimo_bw_cap 1

case $bandwidth in
20) logi "wl mimo_txbw 2"
    wl mimo_txbw 2
    logi "wl chanspec $channel"
    wl chanspec $channel
    ;;
40) logi "wl mimo_txbw 4"
    wl mimo_txbw 4
    logi "wl chanspec $channel$sideband"
    wl chanspec $channel$sideband
    ;;
*)  logi "Invalid Argument"
esac

logi "wl txpwr1 -o -d $power"
wl txpwr1 -o -d $power
logi "wl phy_watchdog 0"
wl phy_watchdog 0
logi "wl up"
wl up
logi "wl nrate -m $rate -s 0"
wl nrate -m $rate -s 0
logi "wl pkteng_start 00:11:22:33:44:66 tx 100 1000 0"
wl pkteng_start 00:11:22:33:44:66 tx 100 1000 0
