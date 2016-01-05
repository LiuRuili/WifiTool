#!/system/bin/sh

mode=$1
channel=$2
#rate=$3
ant=$4
power=$5
status="FAIL"

function logi {
	log -t "WifiTestCmd-tx" -p i $1
}

if [ $channel -lt 15 ]; then
logi "set band b"
band=0
else
logi "set band a"
band=1
fi

case $mode in
n)	rate=$(($3+64))
	;;
*)  rate=$3
    ;;
esac

result=$(at_cli_client "at@wlan:pt_rf_activate(0)")
for var in $result
do
	logi "$var"
done

result=$(at_cli_client "at@wlan:pt_tx($band,$channel,8000,$rate,$power,0,1,0,1000)")
for var in $result
do
	logi "$var"
	if [ $var = "OK" ]; then
		status="PASS"
	fi
done

echo $status