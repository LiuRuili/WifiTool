#!/system/bin/sh

# Para1 = mode ( Tx=2, Rx=1 )
# Para2 = Channel number
# Para3 = Data Rate

function logi {
    log -t "WifiTestCmd" -p i $1
}

channel=$1

logi "iwpriv wlan0 tx 0"
iwpriv wlan0 tx 0
logi "iwpriv wlan0 rx 0"
iwpriv wlan0 rx 0
logi "iwpriv wlan0 ena_chain 1"
iwpriv wlan0 ena_chain 1
logi "iwpriv wlan0 set_channel $channel"
iwpriv wlan0 set_channel $channel
logi "iwpriv wlan0 rx 1"
iwpriv wlan0 rx 1
