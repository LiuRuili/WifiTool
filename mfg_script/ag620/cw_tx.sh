#!/system/bin/sh

mode=$1
channel=$2

function logi {
	log -t "WifiTestCmd-cw_tx" -p i $1
}

if [ $channel -lt 15 ]; then
logi "set band b"
band=0
else
logi "set band a"
band=1
fi

result=$(at_cli_client "at@wlan:pt_rf_activate(0)")
for var in $result
do
	logi "$var"
done

result=$(at_cli_client "at@wlan:pt_tx_cw($band,$channel,8000,0,0,16)")
for var in $result
do
	logi "$var"
done