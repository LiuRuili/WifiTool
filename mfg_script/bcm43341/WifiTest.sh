#!/system/bin/sh

function usage {
	echo "WifiTest Usage Instruction"
	echo "Command ID1: Enter/Exit Wifi MFG Test Mode"
	echo "Para1 = 1, Para2 = 0/1(off/on), Para3 = timeout"
	echo "Command ID2: Set WiFi Tx/Rx rate/channel/bandwidth"
	echo "For Rx Test"
	echo "Para1 = 2, Para2 = b/g/n/ag/an, Para3 = Rx, Para4 = channel, Para5 = bandwidth(HT20/HT40)"
	echo "For Tx Test"
	echo "Para1 = 2, Para2 = b/g/n/ag/an, Prar3 = Tx, Para4 = channel, Para5 = bandwidth(HT20/HT40)"
	echo "Command ID3: Set WiFi Tx power and output antenna"
	echo "Para1 = 3, Para2 = Power(0~18), Para3 = antenna 0(main)/1(salve)"
	echo "Command ID4: Start/Stop Rx"
	echo "Para1 = 4, Para2 = 0/1(start/stop), Para3 = antenna 0(main)/1(slave)"
	echo "Command ID5: Start/Stop Tx"
	echo "Para1 = 5, Para2 = 0/1(start/stop), Para3 = timeout"
	echo "Command ID6: Rx Related Command"
	echo "Start Rx: Para1 = 6, Para2 = mode a/b/g/n, Para3 = channel, Para4 = rate, Para5 = antenna 0(main)/1(slave), Para6 = bandwidth 20/40, Para7 = sideband u/l/c"
	echo "Stop Rx: Para1 = 6, Para2 = 0"
	echo "Get Report: Para1 = 6, Para2 = 1"
	echo "Command ID7: Tx Related Command"
	echo "Start Tx: Para1 = 7, Para2 = mode a/b/g/n, Para3 = channel, Para4 = rate, Para5 = antenna 0(main)/1(slave), Para6 = power (0~18), Para7 = bandwidth 20/40, Para8 = sideband u/l/c"
	echo "Stop Tx: Para1 = 7, Para2 = 0"
	echo "Command ID8: Unmodulted Tx Related Command"
	echo "Start Unmodulated Tx: Para1 = 8, Para2 = 1/0(enable/disable), Para3 = channel"
	echo "Stop Tx: Para1 = 8, Para2 = 0"
}

function logi {
	log -t "WifiTestCmd" -p i $1
}

function set_tx_mode {
	channel=$1
	bandwidth=$2
	logi "wl mpc 0"
	wl mpc 0
	logi "wl down"
	wl down
	logi "wl scansuppress 1"
	wl scansuppress 1
	logi "wl country ALL"
	wl country ALL
	logi "wl chanspec $channel"
	logi $(wl chanspec $channel)
	logi "wl phy_watchdog 0"
	wl phy_watchdog 0
	logi "wl up"
	wl up
	logi "wl nrate -r $rate"
	wl nrate -r $rate
	echo "PASS"
}

function set_rx_mode {
	channel=$1
	bandwidth=$2
	logi "wl mpc 0"
	wl mpc 0
	logi "wl down"
	wl down
	logi "wl scansuppress 1"
	wl scansuppress 1
	logi "wl country ALL"
	wl country ALL
	logi "wl chanspec $channel"
	logi $(wl chanspec $channel)
	logi "wl phy_watchdog 0"
	wl phy_watchdog 0
	logi "wl up"
	wl up
	echo "PASS"
}

function set_tx_n_mode {
	channel=$1
	bandwidth=$2
	logi "wl mpc 0"
	wl mpc 0
	logi "wl down"
	wl down
	logi "wl country ALL"
	wl country ALL
	logi "wl interference 0"
	wl interference 0
	logi "wl scansuppress 1"
	wl scansuppress 1
	logi "wl mimo_bw_cap 1"
	wl mimo_bw_cap 1

	case $bandwidth in
	HT40)	logi "wl mimo_txbw 4"
		wl mimo_txbw 4
		logi "wl chanspec ${channel}u"
		logi $(wl chanspec ${channel}u)
		;;
	*)	logi "wl mimo_txbw 2"
		wl mimo_txbw 2
		logi "wl chanspec $channel"
		logi $(wl chanspec $channel)
		;;
	esac

	logi "wl phy_watchdog 0"
	wl phy_watchdog 0
	logi "wl up"
	wl up
	logi "wl nrate -m $rate -s 0"
	wl nrate -m $rate -s 0
	echo "PASS"
}

function set_rx_n_mode {
	channel=$1
        bandwidth=$2
	logi "wl mpc 0"
	wl mpc 0
	logi "wl down"
	wl down
	logi "wl scansuppress 1"
	wl scansuppress 1
	logi "wl interference 0"
	wl interference 0
	logi "wl srl 1"
	wl srl 1
	logi "wl lrl 1"
	wl lrl 1
	logi "wl country ALL"
	wl country ALL
	logi "wl mimo_bw_cap 1"
	wl mimo_bw_cap 1

	case $bandwidth in
	HT40)	logi "wl chanspec ${channel}u"
		logi $(wl chanspec ${channel}u)
		;;
	*)	logi "wl chanspec $channel"
		logi $(wl chanspec $channel)
		;;
	esac

	logi "wl up"
	wl up
	echo "PASS"
}

function set_tx_power_ant {
	power=$1
        ant=$2
        logi "wl txpwr1 -o -d $power"
        wl txpwr1 -o -d $power
	logi "wl txant $2"
	wl txant $2
	logi "wl antdiv $2"
	wl antdiv $2
	echo "PASS"
}

function start_stop_rx {
	case $1 in
	0)	logi "wl txant $2"
		wl txant $2
		logi "wl antdiv $2"
		wl antdiv $2
		logi "wl pkteng_start 00:11:22:33:44:55 rx"
		wl pkteng_start 00:11:22:33:44:55 rx
		sh /etc/wifi/mfg_script/WifiTest.sh 6 1
		;;
	1)	logi "wl pkteng_stop rx"
		sh /etc/wifi/mfg_script/WifiTest.sh 6 0
		sh /etc/wifi/mfg_script/WifiTest.sh 6 1
		;;
	esac
}

function start_stop_tx {
	case $1 in
	1)	logi "wl pkteng_start 00:11:22:33:44:55 tx 100 1000 0"
		wl pkteng_start 00:11:22:33:44:55 tx 100 1000 0
		;;
	0)	logi "wl pkteng_stop tx"
		wl pkteng_stop tx
		;;
	esac
	echo "PASS"
}

found="False"

case $1 in

1)	case $2 in
	1)	service call wifi 13 i32 0 > /dev/null
		sleep 1
		echo "/system/vendor/firmware/bcm43341/fw_bcmdhd_mfg.bin" > /sys/module/bcmdhd/parameters/firmware_path
		begin=$(date +%s)
		while true
		do
		netcfg wlan0 up
		result=$(netcfg)
		for var in $result
		do
		if [ $var = "wlan0" ]; then
			found="True"
			continue
		fi
		if [ $found = "True" ]; then
		if [  $var = "UP" ]; then
			echo "PASS"
			return
		else
			found="False"
			break
		fi
		fi
		done
		sleep 1
		now=$(date +%s)
		diff=$(($now - $begin))
		if [ $diff -lt $3 ]; then
			found="False"
		else
			echo "FAIL"
			return
		fi
		done
		;;
	0)	service call wifi 13 i32 0 > /dev/null
		begin=$(date +%s)
		while true
		do
		netcfg wlan0 down
		result=$(netcfg)
		for var in $result
		do
		if [ $var = "wlan0" ]; then
			found="True"
			continue
		fi
		if [ $found = "True" ]; then
		if [ $var = "DOWN" ]; then
			echo "PASS"
			return
		else
			found="False"
			break
		fi
		fi
		done
		sleep 1
		now=$(date +%s)
		diff=$(($now - $begin))
		if [ $diff -lt $3 ]; then
			found="False"
		else
			echo "FAIL"
			return
		fi
		done
		;;
	*)	echo "Invalid Para2"
		;;
	esac
	;;
6)	case $2 in
	0)	rm /data/misc/wifi/tmp.txt
		wl pkteng_stop rx
		sh /etc/wifi/mfg_script/WifiTest.sh 6 1
		;;
	a)	sh /etc/wifi/mfg_script/bcm43341/abg_rx.sh $2 $3 $4 $5
		echo "PASS"
	    	;;
	b)	sh /etc/wifi/mfg_script/bcm43341/abg_rx.sh $2 $3 $4 $5
		echo "PASS"
		;;
	g)	sh /etc/wifi/mfg_script/bcm43341/abg_rx.sh $2 $3 $4 $5
		echo "PASS"
	    	;;
	n)	sh /etc/wifi/mfg_script/bcm43341/n_rx.sh $2 $3 $4 $5 $6 $7
		echo "PASS"
		;;
	1)	wl counters > /dev/null
		result=$(cat /data/misc/wifi/tmp.txt)
		for var in $result
		do
			if [ $found = "True" ]; then
				found="False"
				echo "Number of received packets=$var"
				break
			fi

			if [ $var = "pktengrxducast" ]; then
				found="True"
			fi
		done
		;;
	*)  	echo "Invalid Para2"
	    	;;
	esac
	;;
7)	case $2 in
	0)	wl pkteng_stop tx
		;;
	a)	sh /etc/wifi/mfg_script/bcm43341/abg_tx.sh $2 $3 $4 $5 $6
		;;
	b)	sh /etc/wifi/mfg_script/bcm43341/abg_tx.sh $2 $3 $4 $5 $6
		;;
	g)	sh /etc/wifi/mfg_script/bcm43341/abg_tx.sh $2 $3 $4 $5 $6
		;;
	n)	sh /etc/wifi/mfg_script/bcm43341/n_tx.sh $2 $3 $4 $5 $6 $7 $8
		;;
	*)	echo "Invalid Para2"
		;;
	esac
	echo "PASS"
	;;
8)	case $2 in
	1)	sh /etc/wifi/mfg_script/bcm43341/cw_tx.sh $2 $3
		;;
	0)	wl band auto
		wl fqacurcy 0
		wl down
		;;
	*)	echo "Invalid Para2"
		;;
	esac
	echo "PASS"
	;;
h)	usage
	;;
2)	case $2 in
	b)	rate=11
		case $3 in
		Tx) set_tx_mode $4 $5
		;;
		Rx) set_rx_mode $4 $5
		;;
		esac
	;;
	g)	rate=54
		case $3 in
		Tx) set_tx_mode $4 $5
		;;
		Rx) set_rx_mode $4 $5
		;;
		esac
	;;
	n)	rate=7
		case $3 in
		Tx) set_tx_n_mode $4 $5
		;;
		Rx) set_rx_n_mode $4 $5
		;;
		esac
	;;
	ag)	rate=54
		case $3 in
		Tx) set_tx_mode $4 $5
		;;
		Rx) set_rx_mode $4 $5
		;;
		esac
	;;
	an)	rate=7
		case $3 in
		Tx) set_tx_n_mode $4 $5
		;;
		Rx) set_rx_n_mode $4 $5
		;;
		esac
	;;
	esac
	;;
3)	set_tx_power_ant $2 $3
	;;
4)	start_stop_rx $2 $3
	;;
5)	start_stop_tx $2 $3
	;;
*)	echo "Unsupport Command ID"
	;;
esac
