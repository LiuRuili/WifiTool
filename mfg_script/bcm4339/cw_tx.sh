#!/system/bin/sh

mode=$1
channel=$2

function logi {
	log -t "WifiTestCmd-cw_tx" -p i $1
}

logi "wl ver"
wl ver
logi "wl mpc 0"
wl mpc 0
logi "wl out"
wl out

if [ $channel -lt 15 ]; then
logi "wl band b"
wl band b
else
logi "wl band a"
wl band a
fi

logi "wl fqacurcy $channel"
wl fqacurcy $channel
