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

public class WLANTestModeUnMod extends Activity implements OnClickListener {
    private Spinner spinner_c;
    private Spinner spinner_m;
    private OnItemSelectedListener listener = new MyOnItemSelectedListener();
    private int spinner_channel_pos = 0;
    private int spinner_mcs_pos = 0;
    private WifiManager mWifiManager;
    private Button mStartBtn;
    private Button mStopBtn;
    private WLANTestModeNative mTSNative = null;
    private int wifi_State = 0;
    private int CMD_WIFI_UNMOD_TEST = 5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.wifi_unmodem_mode);
        setContentView(R.layout.unmodem_test);

        find_and_modify_view();
        mStartBtn = (Button) findViewById(R.id.WiFi_Start);
        mStopBtn = (Button) findViewById(R.id.WiFi_Stop);
        mStartBtn.setOnClickListener(this);
        mStopBtn.setOnClickListener(this);
        mWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        mTSNative = new WLANTestModeNative();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

       // mWifiManager.setWifiTestModeEnabled(false);
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
        mTSNative.wifi_testmode_set_type(CMD_WIFI_UNMOD_TEST);
        try {
            Process p = Runtime.getRuntime().exec("chmod 777 /data/wl/type");
            p.waitFor();
        } catch (Exception e) {
            TctLog.e("onClickBtnTxStart", "Chmod 777 /data/wl/type failed");
        }
        mTSNative.wifi_testmode_set_channel(spinner_channel_pos);
        mTSNative.wifi_testmode_set_power(1);
        mWifiManager.setWifiTestModeEnabled(true);
        try{
            Thread.sleep(10000);
        }catch(Exception e){

        }
        mWifiManager.setWifiTestModeStartTx();
        showToast("Enter WLAN test mode successful, please wait for 20s to test.",
                Toast.LENGTH_SHORT);
    }

    private void onClickBtnTxStop() {
        mWifiManager.setWifiTestModeEnabled(false);
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
        spinner_c = (Spinner) findViewById(R.id.WiFi_Channel_Spinner);
        ArrayAdapter<CharSequence> adapter_channels = ArrayAdapter.createFromResource(this,
                R.array.channels, android.R.layout.simple_spinner_item);
        adapter_channels.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_c.setAdapter(adapter_channels);
        spinner_c.setOnItemSelectedListener(listener);
    }

    public class MyOnItemSelectedListener implements OnItemSelectedListener {

        public void onItemSelected(AdapterView parent, View view, int position, long id) {
            int pos;
            if (parent == spinner_c) {
                spinner_channel_pos = position;
            }
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }
}
