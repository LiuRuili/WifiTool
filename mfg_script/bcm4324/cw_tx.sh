#!/system/bin/sh

mode=$1
channel=$2
ant=$(($3+1))

function logi {
	log -t "WifiTestCmd-cw_tx" -p i $1
}

logi "wl ver"
wl ver
logi "wl mpc 0"
wl mpc 0
logi "wl out"
wl out

logi "wl txchain $ant"
wl txchain $ant
logi "wl rxchain $ant"
wl rxchain $ant

if [ $channel -lt 15 ]; then
	logi "wl band b"
	wl band b
else
	logi "wl band a"
	wl band a
fi

logi "wl fqacurcy $channel"
wl fqacurcy $channel
