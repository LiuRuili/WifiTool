#!/system/bin/sh

function usage {
	echo "WifiTest Usage Instruction"
	echo "Command ID 1: Enter/Exit Wifi MFG Test Mode"
	echo "Para1 = 1, Para2 = 0/1(off/on), Para3 = timeout"

	echo "Command ID 6: Rx Related Command"
	echo "Start Rx:"
	echo "Para1 = 6, Para2 = mode(a/b/g/n/ac), Para3 = channel, Para4 = rate(11/54/7)"
	echo "Para5 = ant(0/1), Para6 = BW(20/40/80), Para7 = sideband(u/l/c)"
	echo "Stop Rx: Para1 = 6, Para2 = 0"
	echo "Get Report: Para1 = 6, Para2 = 1"

	echo "Command ID 7: Tx Related Command"
	echo "Start Tx:"
	echo "Para1 = 7, Para2 = mode(a/b/g/n/ac), Para3 = channel, Para4 = rate(11/54/7)"
	echo "Para5 = ant(0/1), Para6 = power(0~18),Para7 = BW(20/40/80)"
	echo "Para8 = sideband(u/l/c), Para9 = Guard Interval(0: LONG, 1:SHORT),default 0"
	echo "Stop Tx: Para1 = 7, Para2 = 0"

	echo "Command ID 8: Unmodulted Tx Related Command"
	echo "Start Unmodulated Tx:"
	echo "Para1 = 8, Para2 = 1/0(enable/disable), Para3 = channel"
	echo "Stop Tx: Para1 = 8, Para2 = 0"

	echo "Command ID 9: ATD Rx Command"
	echo "Start Rx:"
	echo "Para1 = 9, Para2 = mode(a/b/g/n/ac), Para3 = channel"
	echo "Para4 = ant(0/1), Para5 = BW(20/40/80), Para6 = rate(11/54/MCS7)"
	echo "Stop Rx: Para1 = 9, Para2 = 0"

	echo "Command ID 10: Tx Related Command"
	echo "Start Tx:"
	echo "Para1 = 10, Para2 = mode(a/b/g/n/ac), Para3 = channel"
	echo "Para4 = power(1~18), Para5 = ant(0/1), Para6 = BW(20/40/80)"
	echo "Para7 = rate(11/54/MCS7), Para8 = Guard Interval(LONG/SHORT),default LONG"
	echo "Para9 = Power Control(SCPC/OLPC/CLPC), Para10 = RF Gain, Para 11 = Digital Gain"
	echo "Stop Tx: Para1 = 10, Para2 = 0"

	echo "Command ID 12: Return WiFi Chip Solution"
}

function logi {
	log -t "WifiTestCmd" -p i $1
}

status="FAIL"
counter=0
pkt=0

case $1 in

1)	case $2 in
	1)	svc wifi disable
		logi "wifitesttool -O"
		result=$(wifitesttool -O)
		logi "$result"
		for var in $result; do
			if [ $var = "success" -o $var = "Already" ]; then
				status="PASS"
			fi
		done
		echo $status
	;;
	0)	logi "wifitesttool -C"
		result=$(wifitesttool -C)
		logi "$result"
		for var in $result; do
			if [ $var = "success" -o $var = "Not" ]; then
				status="PASS"
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
		logi "wifitesttool -T"
		wifitesttool -T > /dev/null 2>&1
	;;
	a|b|g|n|ac)
		logi "wifitesttool -T"
		wifitesttool -T > /dev/null 2>&1
		sh /etc/wifi/mfg_script/mt6630/rx.sh $3 $6
    ;;
	1)	logi "wifitesttool -q"
		result=$(wifitesttool -q)
		logi "$result"
		for var in $result; do
			counter=$(($counter+1))
			if [ $counter -eq 8 ]; then
				IFS="/"
				export IFS;
				for pkt in $var; do
					logi "Number of received packets=$pkt"
					echo "Number of received packets=$pkt"
					break
				done
			fi
		done
	;;
	*)  echo "Invalid Para2"
    ;;
	esac
;;
7)	case $2 in
	0)	logi "wifitesttool -T"
		wifitesttool -T > /dev/null 2>&1
		echo "PASS"
	;;
	a|b|g|n|ac)
		logi "wifitesttool -T"
		wifitesttool -T > /dev/null 2>&1
		sh /etc/wifi/mfg_script/mt6630/tx.sh $2 $3 $4 $5 $6 $7 $8 $9
	;;
	*)	echo "Invalid Para2"
	;;
	esac
;;
8)	case $2 in
	0)	logi "wifitesttool -T"
		wifitesttool -T > /dev/null 2>&1
	;;
	1)	sh /etc/wifi/mfg_script/mt6630/cw_tx.sh $2 $3
	;;
	*)	echo "Invalid Para2"
	;;
	esac
	echo "PASS"
;;
################### For ATD test ###################
9)	case $2 in
	0|1)	sh /etc/wifi/mfg_script/WifiTest.sh 6 $2
		;;
	a|b|g|n|ac)
		sh /etc/wifi/mfg_script/WifiTest.sh 6 $2 $3 $6 $4 $5
		sh /etc/wifi/mfg_script/WifiTest.sh 6 1
		;;
	*)	echo "Invalid Para2"
		;;
	esac
;;
10)	case $2 in
	0)	sh /etc/wifi/mfg_script/WifiTest.sh 7 0
	;;
	a|b|g)
		sh /etc/wifi/mfg_script/WifiTest.sh 7 $2 $3 $7 0 $4 20
	;;
	n|ac)
		rate=${7:3:1}
		gi=0
		if [ $8 = "SHORT" ]; then
			gi=1
		fi
		sh /etc/wifi/mfg_script/WifiTest.sh 7 $2 $3 $rate 0 $4 $6 0 $gi
	;;
	*)	echo "Invalid Para2"
	;;
	esac
;;
12)	echo "MT6630"

;;
h)	usage
;;
*)	echo "Unsupport Command ID"
;;
esac
