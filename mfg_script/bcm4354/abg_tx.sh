#!/system/bin/sh

mode=$1
channel=$2
rate=$3
ant=$(($4+1))
power=$5

function logi {
	log -t "WifiTestCmd-abg_tx" -p i $1
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

logi "wl channel $channel"
wl channel $channel
logi "wl up"
wl up
logi "wl phy_forcecal 1"
wl phy_forcecal 1
logi "wl scansuppress 1"
wl scansuppress 1
logi "wl txchain $ant"
wl txchain $ant
logi "wl rxchain $ant"
wl rxchain $ant
logi "wl txcore -k $ant -o $ant"
wl txcore -k $ant -o $ant

if [ $channel -lt 15 ]; then
	logi "wl 2g_rate -r $rate -b 20"
	wl 2g_rate -r $rate -b 20
else
	logi "wl 5g_rate -r $rate -b 20"
	wl 5g_rate -r $rate -b 20
fi

logi "wl phy_txpwrctrl 1"
wl phy_txpwrctrl 1
logi "wl txpwr1 -o -d $power"
wl txpwr1 -o -d $power
logi "wl pkteng_start 00:11:22:33:44:55 tx 100 1024 0"
wl pkteng_start 00:11:22:33:44:55 tx 100 1024 0
