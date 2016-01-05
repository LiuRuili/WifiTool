#!/system/bin/sh

function usage {
	echo "WifiTest Usage Instruction"
	echo "Command ID 1: Enter/Exit Wifi MFG Test Mode"
	echo "Para1 = 1, Para2 = 0/1(off/on), Para3 = timeout"

	echo "Command ID 6: Rx Related Command"
	echo "Start Rx:"
	echo "Para1 = 6, Para2 = mode(a/b/g/n), Para3 = channel, Para4 = rate(11/54/7)"
	echo "Para5 = ant(0/1), Para6 = BW(20/40/80), Para7 = sideband(u/l/c)"
	echo "Stop Rx: Para1 = 6, Para2 = 0"
	echo "Get Report: Para1 = 6, Para2 = 1"

	echo "Command ID 7: Tx Related Command"
	echo "Start Tx:"
	echo "Para1 = 7, Para2 = mode(a/b/g/n), Para3 = channel, Para4 = rate(11/54/7)"
	echo "Para5 = ant(0/1), Para6 = power(0~18),Para7 = BW(20/40/80), Para8 = sideband(u/l/c)"
	echo "Stop Tx: Para1 = 7, Para2 = 0"

	echo "Command ID 8: Unmodulted Tx Related Command"
	echo "Start Unmodulated Tx:"
	echo "Para1 = 8, Para2 = 1/0(enable/disable), Para3 = channel"
	echo "Stop Tx: Para1 = 8, Para2 = 0"

	echo "Command ID 9: ATD Rx Command"
	echo "Start Rx:"
	echo "Para1 = 9, Para2 = mode(a/b/g/n), Para3 = channel"
	echo "Para4 = ant(0/1), Para5 = BW(20/40/80), Para6 = rate(11/54/MCS7)"
	echo "Stop Rx: Para1 = 9, Para2 = 0"

	echo "Command ID 10: Tx Related Command"
	echo "Start Tx:"
	echo "Para1 = 10, Para2 = mode(a/b/g/n), Para3 = channel"
	echo "Para4 = power(1~18), Para5 = ant(0/1), Para6 = BW(20/40/80)"
	echo "Para7 = rate(11/54/MCS7) , Para8 = Power Control(SCPC/OLPC/CLPC)"
	echo "Para9 = RF Gain, Para 10 = Digital Gain"
	echo "Stop Tx: Para1 = 10, Para2 = 0"

	echo "Command ID 11: Wi-Fi self calibration Command"
}

function logi {
	log -t "WifiTestCmd" -p i $1
}

function init_wifi {
	if [ $1 = "ptest" ]; then
		modules=$(lsmod)
		for var in $modules
		do
		if [ $var = "iwlmvm" ]; then
			svc wifi disable
			sleep 1
			logi "Reloading WiFi driver..."
			rmmod iwlmvm
			rmmod iwlwifi
			insmod /system/lib/modules/compat_iwlwifi.ko nvm_file=nvmData xvt_default_mode=1
			insmod /system/lib/modules/compat_iwlxvt.ko
			logi "Try to perform at@wlan:pt_activate(0)"
			sh /etc/wifi/mfg_script/ag620/timeout.sh -t 1 at_cli_client "at@wlan:pt_activate(0)"
			sleep 1
		fi
		done
	else
		rmmod iwlxvt
		rmmod iwlwifi
		insmod /system/lib/modules/compat_iwlwifi.ko nvm_file=nvmData  > /dev/null 2> /dev/null
		insmod /system/lib/modules/iwlmvm.ko  > /dev/null 2> /dev/null
	fi
}

status="DEFAULT"
counter=0
pkt=0
mode="mvm"

init_wifi "ptest"

case $1 in

1)	case $2 in
	1)	begin=$(date +%s)
		while [ $status != "FAIL" -a $status != "PASS" ]
		do
			logi "at@wlan:pt_activate(0)"
			sh /etc/wifi/mfg_script/ag620/timeout.sh -t 2 at_cli_client "at@wlan:pt_activate(0)" > /dev/null 2> /dev/null
			usleep 500000
			logi "at@wlan:pt_activate(1)"
			result=$(sh /etc/wifi/mfg_script/ag620/timeout.sh -t 2 at_cli_client "at@wlan:pt_activate(1)")
			for var in $result
			do
				if [ $var = "OK" ]; then
					status="PASS"
				fi
			done

			now=$(date +%s)
			diff=$(($now - $begin))
			if [ $diff -ge $3 ]; then
				logi "command timeout"
				status="FAIL"
			fi
		done
		echo $status
		;;
	0)	begin=$(date +%s)
		while [ $status != "FAIL" -a $status != "PASS" ]
		do
			usleep 500000
			logi "at@wlan:pt_activate(0)"
			result=$(sh /etc/wifi/mfg_script/ag620/timeout.sh -t 2 at_cli_client "at@wlan:pt_activate(0)")
			for var in $result
			do
				if [ $var = "OK" ]; then
					status="PASS"
				fi
			done

			now=$(date +%s)
			diff=$(($now - $begin))
			if [ $diff -ge $3 ]; then
				logi "command timeout"
				status="FAIL"
			fi
		done

		echo $status
		;;
	*)	echo "Invalid Para2"
		;;
	esac
	;;
6)	case $2 in
	0)	sh /etc/wifi/mfg_script/WifiTest.sh 6 1
		logi "at@wlan:pt_rf_activate(0)"
		sh /etc/wifi/mfg_script/ag620/timeout.sh -t 2  at_cli_client "at@wlan:pt_rf_activate(0)" > /dev/null 2> /dev/null
		;;
	a|b|g|n)
		sh /etc/wifi/mfg_script/ag620/timeout.sh -t 5 sh /etc/wifi/mfg_script/ag620/rx.sh $2 $3 $4 $5
	    ;;
	1)	#result=$(sh /etc/wifi/mfg_script/ag620/timeout.sh -t 2  at_cli_client "at@wlan:pt_rx_read()")
		result=$(sh /etc/wifi/mfg_script/ag620/timeout.sh -t 2  at_cli_client "at@wlan_mvt:rx_stop()")
		for var in $result
		do
			counter=$(($counter+1))
			if [ $counter -eq 5 ]; then
				pkt=$var
				break
			fi
		done

		logi "Number of received packets=$pkt"
		echo "Number of received packets=$pkt"
		;;
	*)  echo "Invalid Para2"
	    ;;
	esac
	;;
7)	case $2 in
	0)	logi "at@wlan:pt_rf_activate(0)"
		sh /etc/wifi/mfg_script/ag620/timeout.sh -t 2 at_cli_client "at@wlan:pt_rf_activate(0)" > /dev/null 2> /dev/null
		echo "PASS"
		;;
	# 7 b chan rate ant power bw sb
	a|b|g|n)
		sh /etc/wifi/mfg_script/ag620/timeout.sh -t 5 sh /etc/wifi/mfg_script/ag620/tx.sh $2 $3 $4 $5 $6
		;;
	*)	echo "Invalid Para2"
		;;
	esac
	;;
8)	case $2 in
	0)	logi "at@wlan:pt_rf_activate(0)"
		sh /etc/wifi/mfg_script/ag620/timeout.sh -t 2  at_cli_client "at@wlan:pt_rf_activate(0)" > /dev/null 2> /dev/null
		;;
	1)	sh /etc/wifi/mfg_script/ag620/timeout.sh -t 5 sh /etc/wifi/mfg_script/ag620/cw_tx.sh $2 $3
		;;
	*)	echo "Invalid Para2"
		;;
	esac
	echo "PASS"
	;;

################### For ATD test ###################
9)	case $2 in
	0)	sh /etc/wifi/mfg_script/ag620/timeout.sh -t 5 sh /etc/wifi/mfg_script/WifiTest.sh 6 0
		;;
	a|b|g)
		sh /etc/wifi/mfg_script/ag620/timeout.sh -t 5 sh /etc/wifi/mfg_script/ag620/rx.sh $2 $3 $6 $4
	    ;;
	n)
		rate=${6:3:1}
		sh /etc/wifi/mfg_script/ag620/timeout.sh -t 5 sh /etc/wifi/mfg_script/ag620/rx.sh $2 $3 $rate $4
	    ;;
	1)	sh /etc/wifi/mfg_script/ag620/timeout.sh -t 5 sh /etc/wifi/mfg_script/WifiTest.sh 6 1
		;;
	*)  echo "Invalid Para2"
	    ;;
	esac
	;;
10)	case $2 in
	0)	sh /etc/wifi/mfg_script/ag620/timeout.sh -t 5 sh /etc/wifi/mfg_script/WifiTest.sh 7 0
		;;
	a|b|g)
		sh /etc/wifi/mfg_script/ag620/timeout.sh -t 5 sh /etc/wifi/mfg_script/ag620/tx.sh $2 $3 $7 $5 $4
		;;
	n)
		rate=${7:3:1}
		sh /etc/wifi/mfg_script/ag620/timeout.sh -t 5 sh /etc/wifi/mfg_script/ag620/tx.sh $2 $3 $rate $5 $4
		;;
	*)	echo "Invalid Para2"
		;;
	esac
	;;
11)	sh /etc/wifi/mfg_script/ag620/timeout.sh -t 10 sh /etc/wifi/mfg_script/ag620/self_cal.sh
	;;
11b|11a|11g|11n|11ag|11an|11ac)
	sh /etc/wifi/mfg_script/ag620/timeout.sh -t 5 sh /etc/wifi/mfg_script/ag620/wifimaxpower.sh $1
	;;
h)	usage
	;;
*)	echo "Unsupport Command ID"
	;;
esac
