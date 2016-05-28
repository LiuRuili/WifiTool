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

import java.util.ArrayList;

import com.android.WLANTestMode.WLANTestModeBG.MyOnItemSelectedListener;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.TctLog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import org.apache.http.util.EncodingUtils;


public class WLANTestModeReceive extends Activity implements OnClickListener, View.OnKeyListener {
    private Spinner spinner_c;
    private Spinner spinner_m;
    private int spinner_channel_pos = 0;
    private int spinner_mcs_pos = 0;
    private WifiManager mWifiManager;
    private Button mStartBtn;
    private Button mStopBtn;
    private Button mReceiveBtn;
    private EditText IPText;
    private EditText SSIDText;
    private WLANTestModeNative mTSNative = null;
    private int wifi_State = 0;
    private String IPAddr = null;
    private String mSSID = null;
    private boolean mIPAvailable = true;
    private boolean mSSIDAvailable = true;
    private int CMD_PREPARE_RX = 2;
    private int CMD_RECONNECT_RX=3;
    private int CMD_RMMOD_BCM = 4;
    private int CMD_RECONNECT_RXSTOP=6;
    private String display=null;
    private TextView rxport;
    private OnItemSelectedListener listener = new MyOnItemSelectedListener();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.wifi_rx_mode);
        setContentView(R.layout.test_receive);

        find_and_modify_view();
        mStartBtn = (Button) findViewById(R.id.WiFi_Start);
        mStopBtn = (Button) findViewById(R.id.WiFi_Stop);
        mStartBtn.setOnClickListener(this);
        mStopBtn.setOnClickListener(this);
        rxport = (TextView)findViewById(R.id.WIFI_rxreport);
         /*
        mReceiveBtn=(Button)findViewById(R.id.WiFi_receive);
        mReceiveBtn.setOnClickListener(this);
        IPText = (EditText) findViewById(R.id.wifi_test_rx_ip_addr);
        IPText.setSingleLine();
        IPText.setText("192.168.1.189");
        //IPText.setKeyListener(new DigitsKeyListener(false, true));
        IPText.setOnKeyListener(this);
        SSIDText = (EditText) findViewById(R.id.wifi_test_rx_network_ssid);
        SSIDText.setSingleLine();
        SSIDText.setText("MT8860B");
        SSIDText.setOnKeyListener(this);*/
        mWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        mTSNative = new WLANTestModeNative();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTSNative.wifi_testmode_set_type(CMD_RMMOD_BCM);
        mIPAvailable = true;
        mSSIDAvailable = true;
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

         /*
        case R.id.WiFi_receive:
            onClickBtnReceive();
        break;
         */

        default:
        break;
        }
    }
/**
 * set type 2  start prepare receive action
 */
    private void onClickBtnTxStart() {

/*

        String ssid = SSIDText.getText().toString();
        if(ip.equalsIgnoreCase("") || ssid.equalsIgnoreCase("")){
            showToast("Please input IP addr or SSID",Toast.LENGTH_SHORT);
            return;
        }*/
        if (true) {
            mStartBtn.setEnabled(false);
            mTSNative.wifi_testmode_set_type(CMD_PREPARE_RX);

            try {
                Process p = Runtime.getRuntime().exec("chmod 777 /data/wl/type");
                p.waitFor();
            } catch (Exception e) {
                TctLog.e("onClickBtnTxStart", "Chmod 777 /data/wl/type failed");
            }



            mTSNative.wifi_testmode_set_channel(spinner_channel_pos);
              /*
            mTSNative.wifi_testmode_set_ip(ip);
            mTSNative.wifi_testmode_set_ssid(ssid);
              */
            mWifiManager.setWifiTestModeEnabled(true);
            mWifiManager.setWifiTestModeStartTx();
            showToast("Enter WLAN test mode successful, please wait for 20s to test.",
                    Toast.LENGTH_SHORT);
        }
    }

    private void onClickBtnTxStop() {
        mTSNative.wifi_testmode_set_type(CMD_RECONNECT_RXSTOP);
        try {
            Process p = Runtime.getRuntime().exec("chmod 777 /data/wl/type");
            p.waitFor();
        } catch (Exception e) {
            TctLog.e("onClickBtnTxStart", "Chmod 777 /data/wl/type failed");
        }

        mWifiManager.setWifiTestModeStartTx();
        showToast("system will give rx report, please wait for 3s",
                Toast.LENGTH_SHORT);
        mStartBtn.setEnabled(true);

        try{
            Thread.sleep(3000);
        }catch(Exception e){
         }


       try
       {
           File file =new File("/data/wl/","rxreport");
           FileInputStream  in =new FileInputStream(file);
           int length = (int)file.length();
           byte[] tmp =new byte[length];
           in.read(tmp,0,length);
           display = EncodingUtils.getString(tmp, "UTF-8");
           in.close();
       }catch(Exception e)
       {

       }
       rxport.setMovementMethod(ScrollingMovementMethod.getInstance());
       rxport.setText(display);

        mIPAvailable = true;
        mSSIDAvailable = true;
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

    public boolean onKey(View v, int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        /*int viewid = v.getId();
        if (event.KEYCODE_ENTER == keyCode) {
            switch (viewid) {
            case R.id.wifi_test_rx_ip_addr:
                String ip = IPText.getText().toString();
                log("ip="+ip);
                checkIPAddr(mySplit(ip));
                IPAddr = ip;
            break;

            case R.id.wifi_test_rx_network_ssid:
                mSSID = SSIDText.getText().toString();
                if (mSSID.equalsIgnoreCase("")) {
                    showToast("SSID should not  NULL !!!", Toast.LENGTH_SHORT);
                    mSSIDAvailable = false;
                }
            break;
            }
        }*/

        return false;
    }
    /**
     * type 3 start receive action
     */
    private void onClickBtnReceive() {
        log("onClickBtnReceive: star button status= " + mStartBtn.isEnabled());
        if (!mStartBtn.isEnabled()) {
            mTSNative.wifi_testmode_set_type(CMD_RECONNECT_RX);
            try {
                Process p = Runtime.getRuntime().exec("chmod 777 /data/wl/type");
                p.waitFor();
            } catch (Exception e) {
                TctLog.e("onClickBtnTxStart", "Chmod 777 /data/wl/type failed");
            }
            mWifiManager.setWifiTestModeStartTx();
        }
    }
    private void log(String text){
        TctLog.e("WLANTestModeReceive",text);
    }
    private ArrayList<String>  mySplit(String value){
        ArrayList<String> list = new ArrayList<String>();
        int k = 0;
        for(int i=0;i<value.length();i++){
            if(String.valueOf(value.charAt(i)).equalsIgnoreCase(".")){
                log("k1="+k);
                String b = value.substring(k, i);
                list.add(b);
                k=i+1;
                log("subString="+b);
                log("k="+k);
            }
        }
        String c = value.substring(k, value.length());
        list.add(c);
        log("c="+c);
        return list;
    }
    private boolean checkIPAddr(ArrayList<String> value){
        int arraySize=value.size();

        if (arraySize != 4) {
            showToast("IP addr format error !!!", Toast.LENGTH_SHORT);
            log("ip addr length="+arraySize);
            mIPAvailable = false;
        } else {
            for (int i = 0; i < 4; i++) {
                try {
                    int b = Integer.valueOf(value.get(i));
                    log("b="+b);
                    if (b < 0 || b > 255) {
                        showToast("IP addr format error !!!", Toast.LENGTH_SHORT);
                        log("number is erro");
                        mIPAvailable = false;
                    }
                } catch (NumberFormatException e) {
                    showToast("IP addr format error !!!", Toast.LENGTH_SHORT);
                    log("valueOf error");
                    mIPAvailable = false;
                    break;
                }
            }
        }
        return true;
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
