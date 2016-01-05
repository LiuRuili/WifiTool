#!/system/bin/sh

delay=100000
mode=$1
channel=$2
rate=$3
ant=$(($4+1))

function logi {
	log -t "WifiTestCmd-abg_rx" -p i $1
}

logi "wl ver"
wl ver
logi "wl down"
wl down
logi "wl mpc 0"
wl mpc 0
logi "wl country ALL"
wl country ALL
logi "wl up"
wl up
usleep $delay
logi "wl phy_watchdog 0"
wl phy_watchdog 0
logi "wl scansuppress 1"
wl scansuppress 1
logi "wl phy_oclscdenable 0"
wl phy_oclscdenable 0
usleep $delay
logi "wl down"
wl down

if [ $channel -lt 15 ]; then
	logi "wl band b"
	wl band b
else
	logi "wl band a"
	wl band a
fi

logi "wl mimo_preamble 0"
wl mimo_preamble 0
logi "wl mimo_bw_cap 0"
wl mimo_bw_cap 0
logi "wl mimo_txbw -1"
wl mimo_txbw -1
logi "wl chanspec $channel"
wl chanspec $channel
logi "wl up"
wl up
usleep $delay
logi "wl sgi_tx 0"
wl sgi_tx 0
logi "wl nrate -r $rate"
wl nrate -r $rate

logi "wl txchain $ant"
wl txchain $ant
logi "wl rxchain $ant"
wl rxchain $ant
logi "wl pkteng_start 00:11:22:33:44:55 rx"
wl pkteng_start 00:11:22:33:44:55 rx

