#!/system/bin/sh

status="DEFAULT"

function logi {
	log -t "WifiTestCmd-self_cal" -p i $1
}

logi "at@ROCS:LoadAndStartFlow(\"WLAN_SELF_CAL\")"
result=$(at_cli_client "at@ROCS:LoadAndStartFlow(\"WLAN_SELF_CAL\")")

for var in $result
do
	logi $var
	if [ $var = "\"No" ]; then
		counter=$(($counter+1))
	fi
done

if [ $counter -eq 3 ]; then
	logi "at@wlan_mvt:nvm_commit()"
	result=$(at_cli_client "at@wlan_mvt:nvm_commit()")
	for var in $result
	do
		logi $var
		if [ $var = "OK" ]; then
			status="PASS"
		fi
	done
else
	echo "FAIL"
	return
fi

sleep 1

logi "at@wlan_mvt:wifi_sfe_settings_set(2,0xD0,12,12)"
result=$(at_cli_client "at@wlan_mvt:wifi_sfe_settings_set(2,0xD0,12,12)")
for var in $result
do
	logi $var
	if [ $var = "OK" ]; then
		status="SFE_SET_DONE"
	fi
done

if [ $status = "SFE_SET_DONE" ]; then
	logi "at@wlan_mvt:nvm_commit()"
	result=$(at_cli_client "at@wlan_mvt:nvm_commit()")
	for var in $result
	do
		logi $var
		if [ $var = "OK" ]; then
			echo "PASS"
			return
		fi
	done
fi

echo "FAIL"