#!/system/bin/sh

mode=$1
channel=$2
ant=$(($3+1))

function logi {
	log -t "WifiTestCmd-cw_tx" -p i $1
}

logi "wl down"
wl down
logi "wl mpc 0"
wl mpc 0
logi "wl up"
wl up
logi "wl band b"
wl band b
logi "wl phy_txpwrctrl 0"
wl phy_txpwrctrl 0
logi "wl phy_txpwrindex 0 0"
wl phy_txpwrindex 0 0
logi "wl txchain $ant"
wl txchain $ant
logi "wl out"
wl out
logi "wl fqacurcy $channel"
wl fqacurcy $channel
