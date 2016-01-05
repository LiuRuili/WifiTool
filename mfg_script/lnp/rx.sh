#!/system/bin/sh

mode=$1
channel=$2
rate=$3
ant=$4

function logi {
	log -t "WifiTestCmd-rx" -p i $1
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

#result=$(at_cli_client "at@wlan:pt_rx_start($band,$channel,8000,0)")
#for var in $result
#do
#	logi "$var"
#done
result=$(at_cli_client "at@wlan_mvt:rx_start(1,$channel,0,{0x00,0x11,0x22,0x33,0x44,0x66},{0x00,0x11,0x22,0x33,0x44,0x66},0)")
