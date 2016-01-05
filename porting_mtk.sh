#!/bin/bash

LOCAL_DIR=$(pwd)
TARGET_DIR=$LOCAL_DIR/../../hardware/mediatek/wlan/wifitest
OUT_DIR=$ANDROID_PRODUCT_OUT/system

mkdir $TARGET_DIR

echo "cp Android.mk"
cp -rf $LOCAL_DIR/Android.mk $TARGET_DIR
echo "cp wifirfservice"
rm -rf $TARGET_DIR/wifirfservice
cp -rf $LOCAL_DIR/wifirfservice $TARGET_DIR
echo "cp mfg_script"
rm -rf $TARGET_DIR/mfg_script
cp -rf $LOCAL_DIR/mfg_script $TARGET_DIR
echo "cp WifiRfTest"
mkdir $TARGET_DIR/WifiRfTest
cp $LOCAL_DIR/WifiRfTest/Android.mk.new $TARGET_DIR/WifiRfTest/Android.mk
cp $OUT_DIR/app/WifiRfTest/WifiRfTest.apk $TARGET_DIR/WifiRfTest
echo "cp wifirftestd"
mkdir $TARGET_DIR/wifirftestd
cp $LOCAL_DIR/wifirftestd/Android.mk.new $TARGET_DIR/wifirftestd/Android.mk
cp $OUT_DIR/bin/wifirftestd $TARGET_DIR/wifirftestd
echo "cp WifiTest"
mkdir $TARGET_DIR/WifiTest
cp $LOCAL_DIR/WifiTest/Android.mk.new $TARGET_DIR/WifiTest/Android.mk
cp $OUT_DIR/bin/WifiTest $TARGET_DIR/WifiTest
cp $OUT_DIR/bin/WifiSetup $TARGET_DIR/WifiTest/WifiSetup
echo "cp WiFiMaxPower"
mkdir $TARGET_DIR/WiFiMaxPower
cp $LOCAL_DIR/WiFiMaxPower/Android.mk.new $TARGET_DIR/WiFiMaxPower/Android.mk
cp $OUT_DIR/bin/WiFiMaxPower $TARGET_DIR/WiFiMaxPower

echo "porting done..."
