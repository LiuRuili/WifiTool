#!/bin/bash

LOCAL_DIR=$(pwd)
TARGET_DIR=$LOCAL_DIR/../../hardware/broadcom/wlan/wifitest
OUT_DIR=$ANDROID_PRODUCT_OUT/system

echo "copy firmware"
rm -rf $TARGET_DIR/firmware
cp -rf $LOCAL_DIR/firmware $TARGET_DIR
echo "copy usleep"
rm -rf $TARGET_DIR/usleep
cp -rf $LOCAL_DIR/usleep $TARGET_DIR
echo "copy wifirfservice"
rm -rf $TARGET_DIR/wifirfservice
cp -rf $LOCAL_DIR/wifirfservice $TARGET_DIR
echo "cp mfg_script"
rm -rf $TARGET_DIR/mfg_script
cp -rf $LOCAL_DIR/mfg_script $TARGET_DIR
echo "cp WifiRfTest"
cp $OUT_DIR/app/WifiRfTest/WifiRfTest.apk $TARGET_DIR/WifiRfTest
echo "cp wifirftestd"
cp $OUT_DIR/bin/wifirftestd $TARGET_DIR/wifirftestd
echo "cp WifiTest"
cp $OUT_DIR/bin/WifiTest $TARGET_DIR/WifiTest
cp $OUT_DIR/bin/WifiSetup $TARGET_DIR/WifiTest/WifiSetup
echo "cp WiFiMaxPower"
cp $OUT_DIR/bin/WiFiMaxPower $TARGET_DIR/WiFiMaxPower
echo "cp wl/Android.mk"
cp $LOCAL_DIR/wl/Android.mk $TARGET_DIR/wl/
if [ $TARGET_PROJECT == "ME581C" -o $TARGET_PROJECT == "ME581CL" -o $TARGET_PROJECT == "Z581C" -o $TARGET_PROJECT == "SANTA" ];
then
    echo "cp wl_4339"
    cp $OUT_DIR/bin/wl $TARGET_DIR/wl/wl_4339
elif [ $TARGET_PROJECT == "ME375CL" -o $TARGET_PROJECT == "ME176C-L" -o $TARGET_PROJECT == "FE375CL" ];
then
    echo "cp wl_43430"
    cp $OUT_DIR/bin/wl $TARGET_DIR/wl/wl_43430
elif [ $TARGET_PROJECT == "TV500I" ];
then
    echo "cp wl_4354"
    cp $OUT_DIR/bin/wl $TARGET_DIR/wl/wl_4354
else
    echo "cp wl_4334"
    cp $OUT_DIR/bin/wl $TARGET_DIR/wl/wl_4334
fi

echo "porting done..."
