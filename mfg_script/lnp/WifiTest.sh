#!/system/bin/sh

function logi {
    log -t "WifiTestCmd" -p i $1
}

function start_stop_interface {
found="False"
begin=$(date +%s)
timeout=$2
svc wifi disable > /dev/null 2>&1
while true
do
if [ $1 = "UP" ]; then
netcfg wlan0 up > /dev/null 2>&1
else
netcfg wlan0 down > /dev/null 2>&1
fi
result=$(netcfg)
for var in $result
do
if [ $var = "wlan0" ]; then
    found="True"
    continue
fi
if [ $found = "True" ]; then
if [  $var = $1 ]; then
    echo "PASS"
    return
else
    found="False"
    break
fi
fi
done

now=$(date +%s)
diff=$(($now - $begin))
if [ $diff -lt $timeout ]; then
    found="False"
else
    echo "FAIL"
    return
fi
done
}

modules=$(lsmod)
for var in $modules
do
if [ $var = "iwlxvt" ]; then
    logi "Reloading WiFi driver..."
    rmmod iwlxvt
    rmmod iwlwifi
    sleep 2
    insmod system/lib/modules/compat_iwlwifi.ko nvm_file=nvmData
    insmod system/lib/modules/iwlmvm.ko
    sleep 4
fi
done

case $1 in
1) case $2 in
1) svc wifi disable
   start_stop_interface UP $3
;;
0) svc wifi disable
   start_stop_interface DOWN $3
;;
esac
;;
esac
