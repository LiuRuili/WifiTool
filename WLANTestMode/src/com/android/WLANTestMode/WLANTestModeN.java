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
/* 12/19/2012|Chen Ji               |bugID346399           |develop WLANTestM */
/*           |                      |                      |ode app java code */
/* ----------|----------------------|----------------------|----------------- */
/******************************************************************************/

package com.android.WLANTestMode;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.TctLog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import com.android.WLANTestMode.WLANTestModeNative;

public class WLANTestModeN extends Activity implements OnClickListener {
    private Spinner spinner_c;
    private Spinner spinner_m;
    private int spinner_channel_pos = 0;
    private int spinner_mcs_pos = 0;
    private WifiManager mWifiManager;
    private Button mStartBtn;
    private Button mStopBtn;
    private WLANTestModeNative mTSNative = null;
    private int wifi_State = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.wifi_signal_mode);
        setContentView(R.layout.n_test);

        find_and_modify_view();
        mStartBtn = (Button) findViewById(R.id.WiFi_Start);
        mStopBtn = (Button) findViewById(R.id.WiFi_Stop);
        mStartBtn.setOnClickListener(this);
        mStopBtn.setOnClickListener(this);
        mWifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
        mTSNative = new WLANTestModeNative();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        mWifiManager.setWifiTestModeEnabled(false);
    }

    public void onClick(View view) {
        // TODO Auto-generated method stub

        switch (view.getId()) {
        case R.id.WiFi_Start:
            onClickBtnTxStart();

            break;

        case R.id.WiFi_Stop:
            onClickBtnTxStop();

            break;

        default:
            break;
        }
    }

    private void onClickBtnTxStart() {
        mStartBtn.setEnabled(false);
        mTSNative.wifi_testmode_set_type(1);
        try {
            Process p = Runtime.getRuntime().exec("chmod 777 /data/wl/type");
            p.waitFor();
        } catch (Exception e) {
            TctLog.e("onClickBtnTxStart", "Chmod 777 /data/wl/type failed");
        }
      /*  mTSNative.wifi_testmode_set_channel(spinner_channel_pos);
        mTSNative.wifi_testmode_set_power(1);*/
        //mWifiManager.setWifiTestModeEnabled(true);
        mWifiManager.setWifiTestModeStartTx();
        showToast("Enter WLAN test mode successful, please wait for 20s to test.",Toast.LENGTH_SHORT);
    }

    private void onClickBtnTxStop() {
    	mTSNative.wifi_testmode_set_type(4);
    	try {
            Process p = Runtime.getRuntime().exec("chmod 777 /data/wl/type");
            p.waitFor();
        } catch (Exception e) {
            TctLog.e("onClickBtnTxStop", "Chmod 777 /data/wl/type failed");
        }
        //mWifiManager.setWifiTestModeEnabled(false);
        mWifiManager.setWifiTestModeStartTx();
        mStartBtn.setEnabled(true);
    }
    protected void showToast(final String string, int type) {

        View view = inflateView(R.layout.toast);

        TextView tv = (TextView) view.findViewById(R.id.content);
        tv.setText(string);

        Toast toast = new Toast(this);
        toast.setView(view);
        toast.setDuration(type);
        toast.show();
    }

    private View inflateView(int toast) {
        // TODO Auto-generated method stub
        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return vi.inflate(toast, null);
    }


    private void find_and_modify_view() {

    }

}
