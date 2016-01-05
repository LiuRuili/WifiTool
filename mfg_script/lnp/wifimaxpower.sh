#!/system/bin/sh

function logi {
	log -t "WifiTestCmd-maxpower" -p i $1
}


case $1 in

11b)
	pwr=0
	max_pwr=0
	result=$(at_cli_client "at@wlan_mvt:regulatory_get(164,13)")
	for var in $result
	do
		counter=$(($counter+1))
		# Get CCK ch1~ch13 power
		if [ $counter -ge 5 -a $counter -le 17 ]; then
			pwr=${var:0:2}
		fi
		# Get Max power
		if [ $pwr -gt $max_pwr ]; then
			max_pwr=$pwr
		fi
	done
	# max_pwr/2 => dBm value
	max_pwr=$(echo $max_pwr 2 | awk '{ printf "%0.1f\n" ,$1/$2}')
	logi "$1 max power = $max_pwr dBm"
	echo $max_pwr
	;;
11a|11g|11n|11ag|11an|11ac)
	pwr=0
	max_pwr=0
	result=$(at_cli_client "at@wlan_mvt:regulatory_get(178,13)")
	for var in $result
	do
		counter=$(($counter+1))
		# Get OFDM ch1~ch13 power
		if [ $counter -ge 5 -a $counter -le 17 ]; then
			pwr=${var:0:2}
		fi
		# Get Max power
		if [ $pwr -gt $max_pwr ]; then
			max_pwr=$pwr
		fi
	done
	# max_pwr/2 => dBm value
	max_pwr=$(echo $max_pwr 2 | awk '{ printf "%0.1f\n" ,$1/$2}')
	logi "$1 max power = $max_pwr dBm"
	echo $max_pwr
	;;
*)	echo "Unsupport Command ID"
	;;
esac