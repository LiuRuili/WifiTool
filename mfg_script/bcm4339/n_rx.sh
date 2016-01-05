#!/system/bin/sh

mode=$1
channel=$2
rate=$3
ant=$4
bandwidth=$5
sideband=$6

function logi {
	log -t "WifiTestCmd-n_rx" -p i $1
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

logi "wl down"
wl down

case $bandwidth in
40) logi "wl mimo_bw_cap 1"
    wl mimo_bw_cap 1
    logi "wl chanspec $channel$sideband"
    wl chanspec $channel$sideband
    logi "wl mimo_txbw 4"
    wl mimo_txbw 4
    ;;
*)  logi "wl chanspec $channel/20"
    wl chanspec $channel/20
    ;;
esac

logi "wl up"
wl up
logi "wl phy_forcecal 1"
wl phy_forcecal 1
logi "wl scansuppress 1"
wl scansuppress 1
