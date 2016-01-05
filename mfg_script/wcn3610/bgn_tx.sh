#!/system/bin/sh

# Para1 = mode ( Tx=2, Rx=1 )
# Para2 = Channel number
# Para3 = Data Rate

function logi {
    log -t "WifiTestCmd" -p i $1
}

channel=$1
rate=$2
mode=$3

logi "iwpriv wlan0 tx 0"
iwpriv wlan0 tx 0
logi "iwpriv wlan0 rx 0"
iwpriv wlan0 rx 0
logi "iwpriv wlan0 ena_chain 2"
iwpriv wlan0 ena_chain 2
logi "iwpriv wlan0 set_channel $channel"
iwpriv wlan0 set_channel $channel
logi "iwpriv wlan0 set_txrate $rate"
iwpriv wlan0 set_txrate $rate
logi "iwpriv wlan0 pwr_cntl_mode $mode"
iwpriv wlan0 pwr_cntl_mode $mode
case $mode in
0) logi "iwpriv wlan0 set_tx_wf_gain $4 $5"
   iwpriv wlan0 set_tx_wf_gain $4 $5
   ;;
*) logi "iwpriv wlan0 set_txpower $4"
   iwpriv wlan0 set_txpower $4
   ;;
esac
logi "iwpriv wlan0 tx 1"
iwpriv wlan0 tx 1
