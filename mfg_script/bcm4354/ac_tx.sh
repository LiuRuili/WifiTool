#!/system/bin/sh

mode=$1
channel=$2
rate=$3
ant=$(($4+1))
power=$5
bandwidth=$6
sideband=$7

function logi {
	log -t "WifiTestCmd-ac_tx" -p i $1
}

logi "wl ver"
wl ver
logi "wl txchain 3"
wl txchain 3
logi "wl rxchain 3"
wl rxchain 3
logi "wl mpc 0"
wl mpc 0
logi "wl phy_watchdog 0"
wl phy_watchdog 0
logi "wl country ALL"
wl country ALL

if [ $channel -lt 15 ]; then
	logi "wl band b"
	wl band b
else
	logi "wl band a"
	wl band a
fi

case $bandwidth in
40)	logi "wl chanspec $channel$bandwidth"
	wl chanspec $channel$bandwidth
	;;
80)	logi "wl chanspec $channel/$bandwidth"
	wl chanspec $channel/$bandwidth
	;;
*)	logi "wl chanspec $channel/$bandwidth"
	wl chanspec $channel/$bandwidth
	logi "wl down"
	wl down
	logi "wl vht_features 3"
	wl vht_features 3
	;;
esac

logi "wl up"
wl up
logi "wl phy_forcecal 1"
wl phy_forcecal 1
logi "wl scansuppress 1"
wl scansuppress 1

case $ant in
3)	#11n CDD mode
	logi "wl txbf 0"
	wl txbf 0
	logi "wl txchain 3"
	wl txchain 3
	logi "wl rxchain 3"
	wl rxchain 3
	logi "wl txcore -s 1 -c 3"
	wl txcore -s 1 -c 3
	stream=1
	;;
4)	#11n MIMO mode
	logi "wl txchain 3"
	wl txchain 3
	logi "wl rxchain 3"
	wl rxchain 3
	logi "wl txcore -s 2 -c 3"
	wl txcore -s 2 -c 3
	stream=2
	;;
*)	#SISO for core 0 / core 1
	logi "wl txchain $ant"
	wl txchain $ant
	logi "wl rxchain $ant"
	wl rxchain $ant
	logi "wl txcore -s 1 -c $ant"
	wl txcore -s 1 -c $ant
	stream=1
	;;
esac

if [ $channel -lt 15 ]; then
	logi "wl 2g_rate -v $rate -s $stream -b $bandwidth"
	wl 2g_rate -v $rate -s $stream -b $bandwidth
else
	logi "wl 5g_rate -v $rate -s $stream -b $bandwidth"
	wl 5g_rate -v $rate -s $stream -b $bandwidth
fi

logi "wl phy_txpwrctrl 1"
wl phy_txpwrctrl 1
logi "wl txpwr1 -o -d $power"
wl txpwr1 -o -d $power
logi "wl pkteng_start 00:11:22:33:44:55 tx 100 1024 0"
wl pkteng_start 00:11:22:33:44:55 tx 100 1024 0
