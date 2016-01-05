#!/system/bin/sh

#mode=$1
channel=$2
#rate=$3
ant=$4
power=$5
#bw=$6
sideband=$7
gi=$8
status="PASS"

function logi {
	log -t "WifiTestCmd-tx" -p i $1
}

case $1 in
a|b|g)
	mode=0
;;
n)	mode=1
;;
ac)	mode=2
;;
esac

if [ $mode = "0" ]; then
	case $3 in
	1)	rate=1
	;;
	2)	rate=2
	;;
	5.5)	rate=3
	;;
	6)	rate=4
	;;
	9)	rate=5
	;;
	11)	rate=6
	;;
	12)	rate=7
	;;
	18)	rate=8
	;;
	24)	rate=9
	;;
	36)	rate=10
	;;
	48)	rate=11
	;;
	54)	rate=12
	;;
	*)	rate=1
	;;
	esac
else
	rate=$3
fi

case $6 in
20)	bw=0
;;
40)	bw=1
;;
80)	bw=2
;;
esac

logi "wifitesttool -t $mode -n 0 -S 0 -c $channel -p $power -R $rate -s 1 -N $rate -g 1 -G $gi -b $bw"
result=$(wifitesttool -t $mode -n 0 -S 0 -c $channel -p $power -R $rate -s 1 -N $rate -g 1 -G $gi -b $bw)
logi "$result"
for var in $result; do
	if [ $var = "(fail)" ]; then
		status="FAIL"
		break
	fi
done
echo $status
