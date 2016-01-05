#!/system/bin/sh

# Para1 = mode ( Tx=2, Rx=1 )
# Para2 = Channel number
# Para3 = Data Rate

function logi {
    log -t "WifiTestCmd" -p i $1
}

channel=$1
dGain=$2
rGain=$3

logi "iwpriv wlan0 tx 0"
iwpriv wlan0 tx 0
logi "iwpriv wlan0 rx 0"
iwpriv wlan0 rx 0
logi "iwpriv wlan0 set_channel $channel"
iwpriv wlan0 set_channel $channel
logi "iwpriv wlan0 set_tx_wf_gain $dGain $rGain"
iwpriv wlan0 set_tx_wf_gain $dGain $rGain
logi "iwpriv wlan0 tx_cw_rf_gen 1"
iwpriv wlan0 tx_cw_rf_gen 1
