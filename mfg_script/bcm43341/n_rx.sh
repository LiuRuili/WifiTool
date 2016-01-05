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
logi "wl down"
wl down
logi "wl scansuppress 1"
wl scansuppress 1
logi "wl interference 0"
wl interference 0
logi "wl srl 1"
wl srl 1
logi "wl lrl 1"
wl lrl 1
logi "wl country ALL"
wl country ALL
logi "wl mimo_bw_cap 1"
wl mimo_bw_cap 1

case $bandwidth in
20) logi "wl chanspec $channel"
    wl chanspec $channel
    ;;
40) logi "wl chanspec $channel$sideband"
    wl chanspec $channel$sideband
    ;;
*)  logi "Invalid Argument"
    ;;
esac

logi "wl up"
wl up
logi "wl pkteng_start 00:11:22:33:44:55 rx"
wl pkteng_start 00:11:22:33:44:55 rx
