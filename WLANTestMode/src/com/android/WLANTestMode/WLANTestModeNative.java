
package com.android.WLANTestMode;


public class WLANTestModeNative{
    static {
        System.loadLibrary("wifitestmodejni");
        }
    public native int wifi_testmode_set_channel(int value);
    public native int wifi_testmode_set_power(int value);
    public native int wifi_testmode_set_type(int value);
    public native int wifi_testmode_set_dgain(int value);
    public native int wifi_testmode_set_rfgain(int value);
    public native int wifi_testmode_set_channel_bonding(int value);
    public native int wifi_testmode_set_channel_bw(int bw,int value);
    public native int wifi_testmode_set_rate_bw(int bw,int value);
    public native int wifi_testmode_set_txpkglen(int value);
    public native int wifi_testmode_set_power_cntl_mode(int value);
    public native int wifi_testmode_set_ssid(String value);
    public native int wifi_testmode_set_ip(String value);
}
