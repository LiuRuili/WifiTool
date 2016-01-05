#!/system/bin/sh

TAG="WifiRfTestCmd"

function usage {
    echo "WifiTest Usage Instruction"
    echo "Command ID1: Enter/Exit Wifi MFG Test Mode"
    echo "Para1 = 1, Para2 = 0/1(off/on), Para3 = timeout"
    echo "Command ID 6: Rx Related Command"
    echo "Start Rx:"
    echo "Para1 = 6, Para2 = mode(b/g/n), Para3 = channel"
    echo "Stop Rx: Para1 = 6, Para2 = 0"
    echo "Get Report: Para1 = 6, Para2 = 1"
    echo "Command ID 7: Tx Related Command"
    echo "Start Tx:"
    echo "Para1 = 7, Para2 = mode(b/g/n), Para3 = channel, Para4 = rate(11A_6_MBPS/MCS_6_5_MBPS)"
    echo "Para5 = Power Control(0OLPC/1SCPC/2CLPC), Para6 = power/dGain, Para7 = rGain"
    echo "Stop Tx: Para1 = 7, Para2 = 0"
    echo "Command ID 8: Unmodulted Tx Related Command"
    echo "Start Unmodulated Tx:"
    echo "Para1 = 8, Para2 = channel, Para3 = dGain(-39~24), Para4 = rGain(0~31)"
    echo "Stop Tx: Para1 = 8, Para2 = 0"
    echo "Command ID 9: ATD Rx Command"
    echo "Start Rx:"
    echo "Para1 = 9, Para2 = mode(b/g/n), Para3 = channel"
    echo "Para4 = ant(0/1), Para5 = BW(20/40/80), Para6 = rate(11/54/MCS7)"
    echo "Stop Rx: Para1 = 9, Para2 = 0"
    echo "Command ID 10: Tx Related Command"
    echo "Start Tx:"
    echo "Para1 = 10, Para2 = mode(a/b/g/n), Para3 = channel"
    echo "Para4 = power(1~18), Para5 = ant(0/1), Para6 = BW(20/40/80)"
    echo "Para7 = rate(11/54/MCS7) , Para8 = Power Control(SCPC/OLPC/CLPC)"
    echo "Para9 = RF Gain, Para10 = Digital Gain"
    echo "Stop Tx: Para1 = 10, Para2 = 0"
}

function logi {
    log -t "WifiTestCmd" -p i $1
}

function curstatus {
    ret=$(iwpriv wlan0 get_status 2>&1)
    for var in $ret
    do
        if [ $var = "private" ]; then
            echo "disabled"
            return
        fi
    done
    echo "enabled"
}

function currxstatus {
    ret=$(iwpriv wlan0 get_status 2>&1)
    flag="false"
    for var in $ret
    do
        if [ $flag = "true" ]; then
            rxstatus=$var
            break
        fi
        if [ $var = "rxmode:" ]; then
            flag="true"
        fi
    done
    echo $rxstatus
}

function curtxstatus {
    ret=$(iwpriv wlan0 get_status 2>&1)
    flag="false"
    for var in $ret
    do
        if [ $flag = "true" ]; then
            txstatus=$var
            break
        fi
        if [ $var = "txpktgen:" ]; then
            flag="true"
        fi
    done
    echo $txstatus
}

case $1 in
h)  usage
;;
# Command ID 1 : Enter/Exit Wifi FTM Mode
1)  case $2 in
    1)  # ATD FTM start
        svc wifi disable > /dev/null
        ret=$(curstatus)
        if [ $ret = "disabled" ]; then
            rmmod wlan > /dev/null 2>&1
            insmod /system/lib/modules/wlan.ko con_mode=5 > /dev/null 2>&1
            iwpriv wlan0 ftm 1
        fi
        ret=$(curstatus)
        if [ $ret = "enabled" ]; then
            echo "PASS"
        else
            echo "FAIL"
        fi
	;;
    0)  # FTM stop
        svc wifi disable > /dev/null
        iwpriv wlan0 ftm 0 > /dev/null 2>&1
        rmmod wlan > /dev/null 2>&1
        ret=$(curstatus)
        if [ $ret = "disabled" ]; then
            echo "PASS"
        else
            echo "FAIL"
        fi
        ;;
    *)  log -t $TAG -p i "Invalid Argument"
	;;
    esac
;;
# Command ID 6: Rx related command
6)  case $2 in
    b|g|n) 
    logi "iwpriv wlan0 clr_rxpktcnt 1"
    iwpriv wlan0 clr_rxpktcnt 1
    sh /etc/wifi/mfg_script/wcn3610/bgn_rx.sh $3
    ret=$(currxstatus)
    if [ $ret = "disable" ]; then
        echo "FAIL"
    else
        echo "PASS"
    fi
    ;;
    1) logi "iwpriv wlan0 get_rxpktcnt"
       ret=$(iwpriv wlan0 get_rxpktcnt)
       for var in $ret
       do
           pkt=${var##*:}
       done
       echo "Number of received packets=$pkt"
       ;;
    0) sh /etc/wifi/mfg_script/WifiTest.sh 6 1
       logi "iwpriv wlan0 rx 0"
       iwpriv wlan0 rx 0
       ;;
    esac
;;
#Command ID 7: Tx related command
7) case $2 in
   b|g|n) sh /etc/wifi/mfg_script/wcn3610/bgn_tx.sh $3 $4 $5 $6 $7
          ret=$(curtxstatus)
          if [ $ret = "started" ]; then
              echo "PASS"
          else
              echo "FAIL"
          fi
   ;;
   0) logi "iwpriv wlan0 tx 0"
      iwpriv wlan0 tx 0
      ret=$(curtxstatus)
      if [ $ret = "stopped" ]; then
          echo "PASS"
      else
          echo "FAIL"
      fi
   ;;
   esac
;;
8) case $2 in
   0) logi "iwpriv wlan0 tx_cw_rf_gen 0"
      iwpriv wlan0 tx_cw_rf_gen 0
      echo "PASS"
   ;;
   *) sh /etc/wifi/mfg_script/wcn3610/cw_tx.sh $2 $3 $4
      echo "PASS"
   ;;
   esac
;;
9) sh /etc/wifi/mfg_script/WifiTest.sh 6 $2 $3
;;
10) case $2 in
    b) sh /etc/wifi/mfg_script/WifiTest.sh 7 $2 $3 11B_LONG_11_MBPS 1 $4
    ;;
    g) sh /etc/wifi/mfg_script/WifiTest.sh 7 $2 $3 11A_54_MBPS 1 $4
    ;;
    n) sh /etc/wifi/mfg_script/WifiTest.sh 7 $2 $3 MCS_65_MBPS 1 $4
    ;;
    0) sh /etc/wifi/mfg_script/WifiTest.sh 7 0
    ;;
    esac
;;
esac
