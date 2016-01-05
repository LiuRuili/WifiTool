#!/bin/bash

LOCAL_DIR=$(pwd)
TARGET_DIR=$LOCAL_DIR/../../hardware/imc/wlan/wifitest
OUT_DIR=$ANDROID_PRODUCT_OUT/system

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
cp $OUT_DIR/bin/WifiTest $TARGET_DIR/WiFiMaxPower/WiFiMaxPower

echo "porting done..."
