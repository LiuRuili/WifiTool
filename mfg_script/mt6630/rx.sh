#!/system/bin/sh

channel=$1
#bw=$2
status="PASS"

function logi {
	log -t "WifiTestCmd-rx" -p i $1
}

case $2 in
20)	bw=0
;;
40)	bw=1
;;
80)	bw=2
;;
*)	bw=0
;;
esac

logi "wifitesttool -r -n 0 -c $channel -b $bw"
result=$(wifitesttool -r -n 0 -c $channel -b $bw)
logi "$result"
for var in $result; do
	if [ $var = "(fail)" ]; then
		status="FAIL"
		break
	fi
done
echo $status