#!/system/bin/sh

echo "#*************************************************"
echo "# Script usage:"
echo "# tx"
echo "#"
echo "# Example: to stop tx"
echo "# tx"
echo "#*************************************************"

echo "#stop TX mode "
echo "./athtestcmd -i wlan0 --tx off "
athtestcmd -i wlan0 --tx off
echo "Script end."
