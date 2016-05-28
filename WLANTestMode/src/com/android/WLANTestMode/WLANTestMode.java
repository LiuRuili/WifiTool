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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.content.Intent;
import android.provider.Settings;
import static android.provider.Settings.System.SCREEN_OFF_TIMEOUT;

public class WLANTestMode extends Activity {
    /** Called when the activity is first created. */
    private List<Map<String, Object>> data;
    private ListView listView = null;
    private int FUNCTION_BG_TEST_ID = 0;
    private int FUNCTION_N_TEST_ID = 1;
    private int FUNCTION_RX_TEST_ID=3;
    private int FUNCTION_WAVE_GENERATOR = 2;
    private int FUNCTION_TX_UNMOD_TEST=4;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrepareData();
         Settings.System.putInt(getContentResolver(),
                SCREEN_OFF_TIMEOUT,Integer.MAX_VALUE);
        listView = new ListView(this);
        //layout
         SimpleAdapter adapter = new SimpleAdapter(this, data,
         android.R.layout.simple_list_item_1, new String[] { "function" },
         new int[] { android.R.id.text1 });

        listView.setAdapter(adapter);
        setContentView(listView);

        OnItemClickListener listener = new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
                if(position == FUNCTION_BG_TEST_ID){
                    wlanTestModeBG();
                }else if(position == FUNCTION_N_TEST_ID){
                    wlanTestModeN();
                }else if(position == FUNCTION_RX_TEST_ID){
                    wlanTestModeRx();
                }else if(position == FUNCTION_TX_UNMOD_TEST){
                    wlanTestModeTxUnMod();
                }else if (position == FUNCTION_WAVE_GENERATOR) {
                    wlanTestModeWaveGenerator();
                }

            }
        };
      listView.setOnItemClickListener(listener);
    }

    private void PrepareData() {
        data = new ArrayList<Map<String, Object>>();
        Map<String, Object> item;
        item = new HashMap<String, Object>();
        item.put("function", getResources().getString(R.string.wifi_tx_mode));
        data.add(item);

        item = new HashMap<String,Object>();
        item.put("function", getResources().getString(R.string.wifi_signal_mode));
        data.add(item);

        item = new HashMap<String,Object>();
        item.put("function", getResources().getString(R.string.wifi_wave_generator));
        data.add(item);

        /*item = new HashMap<String,Object>();
        item.put("function", getResources().getString(R.string.wifi_rx_mode));
        data.add(item);
        
        item = new HashMap<String,Object>();
        item.put("function", getResources().getString(R.string.wifi_unmodem_mode));
        data.add(item); */

    }

    private void wlanTestModeBG() {
        Intent intent = new Intent(WLANTestMode.this, WLANTestModeBG.class);
        startActivity(intent);
    }

    private void wlanTestModeN() {
        Intent intent = new Intent(WLANTestMode.this, WLANTestModeN.class);
        startActivity(intent);
    }
    private void wlanTestModeRx() {
        Intent intent = new Intent(WLANTestMode.this, WLANTestModeReceive.class);
        startActivity(intent);
    }
    private void wlanTestModeTxUnMod() {
        Intent intent = new Intent(WLANTestMode.this, WLANTestModeUnMod.class);
        startActivity(intent);
    }
    private void wlanTestModeWaveGenerator() {
        Intent intent = new Intent(WLANTestMode.this, WLANTestModeWaveGenerator.class);
        startActivity(intent);
    }
}
