/******************************************************************************/
/*                                                               Date:10/2012 */
/*                                PRESENTATION                                */
/*                                                                            */
/*       Copyright 2012 TCL Communication Technology Holdings Limited.        */
/*                                                                            */
/* This material is company confidential, cannot be reproduced in any form    */
/* without the written permission of TCL Communication Technology Holdings    */
/* Limited.                                                                   */
/*                                                                            */
/* -------------------------------------------------------------------------- */
/*  Author :  Chen Ji                                                         */
/*  Email  :  Ji.Chen@tcl-mobile.com                                          */
/*  Role   :                                                                  */
/*  Reference documents : refer bugID200662/161302                            */
/* -------------------------------------------------------------------------- */
/*  Comments :                                                                */
/*  File     : development/apps/WLANTestMode/src/com/android/WLANTestMode     */
/*  Labels   :                                                                */
/* -------------------------------------------------------------------------- */
/* ========================================================================== */
/*     Modifications on Features list / Changes Request / Problems Report     */
/* -------------------------------------------------------------------------- */
/*    date   |        author        |         Key          |     comment      */
/* ----------|----------------------|----------------------|----------------- */
/* 10/22/2012|Chen Ji               |bugID321787           |develop WLANTestM */
/*           |                      |                      |ode app java code */
/* ----------|----------------------|----------------------|----------------- */
/******************************************************************************/

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
    public native int wifi_testmode_set_rate_bw(int bw,int mode, int value);
    public native int wifi_testmode_set_txpkglen(int value);
    public native int wifi_testmode_set_power_cntl_mode(int value);
}
