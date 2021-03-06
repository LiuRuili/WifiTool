
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

public class WLANTestModeBG extends Activity implements OnClickListener {
    private Spinner spinner_pc;
    private Spinner spinner_cb;
    private Spinner spinner_c;
    private Spinner spinner_m;
    private Spinner spinner_r;
    private Spinner spinner_p;
    private Spinner spinner_len;
    private OnItemSelectedListener listener = new MyOnItemSelectedListener();
    private int spinner_power_cntl_mode_pos = 0;
    private int spinner_channel_bonding_pos = 0;
    private int spinner_channel_pos = 0;
    private int spinner_mode_pos = 0;
    private int spinner_rate_pos = 0;
    private int spinner_power_pos = 0;
    private int spinner_len_pos = 0;
    private WifiManager mWifiManager;
    private Button mStartBtn;
    private Button mStopBtn;
    private WLANTestModeNative mTSNative = null;

    private static final int CHANGE_FLAG_ALL = 0;
    private static final int CHANGE_FLAG_CHANNEL_BONDING = 1;
    private static final int CHANGE_FLAG_CHANNEL = 2;
    private static final int CHANGE_FLAG_MODE = 3;
    private static final int BANDWIDTH_20 = 0;
    private static final int BANDWIDTH_40 = 1;
    private static final int BANDWIDTH_80 = 2;
    private int mChangeFlag = CHANGE_FLAG_ALL;
    private int mBandWidth = BANDWIDTH_20;

    private int wifi_State = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.wifi_tx_mode);
        setContentView(R.layout.bg_test);

        find_and_update_view();
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

        //if(wifi_State == WIFI_TESTMODE_STATE_ON){
            mWifiManager.setWifiTestModeEnabled(false);
        //    wifi_State = WIFI_TESTMODE_STATE_OFF;
            //}
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
        if(spinner_power_pos == 0)
        {
            mTSNative.wifi_testmode_set_type(7);

        }

        if(spinner_power_pos >0)
        {
             mTSNative.wifi_testmode_set_type(0);

        }

        try {
            Process p = Runtime.getRuntime().exec("chmod 777 /data/wl/type");
            p.waitFor();
        }catch ( Exception e ) {
            TctLog.e("onClickBtnTxStart", "Chmod 777 /data/wl/type failed");
        }

        mTSNative.wifi_testmode_set_channel_bonding(spinner_channel_bonding_pos);



		mTSNative.wifi_testmode_set_rate_bw(spinner_channel_bonding_pos,spinner_rate_pos);
        mTSNative.wifi_testmode_set_channel_bw(spinner_channel_bonding_pos,spinner_channel_pos);
        mTSNative.wifi_testmode_set_txpkglen(spinner_len_pos);
        mTSNative.wifi_testmode_set_power(spinner_power_pos);
        mTSNative.wifi_testmode_set_power_cntl_mode(spinner_power_cntl_mode_pos);
        mWifiManager.setWifiTestModeEnabled(true);
        mWifiManager.setWifiTestModeStartTx();
        showToast("Enter WLAN test mode successful, please wait for 20s to test.", Toast.LENGTH_SHORT);
    }

    private void onClickBtnTxStop() {
        mWifiManager.setWifiTestModeStopTx();
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

    private void setPowerCntlModeView() {
     // Power Control Mode Spinner
        spinner_pc = (Spinner)findViewById(R.id.WiFi_Power_cntl_mode_Spinner);
        ArrayAdapter<CharSequence> adapter_power_cntl = ArrayAdapter.createFromResource(this, R.array.power_cntl_mode, android.R.layout.simple_spinner_item);
        adapter_power_cntl.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_pc.setAdapter(adapter_power_cntl);
        spinner_pc.setSelection(spinner_power_cntl_mode_pos);
        spinner_pc.setOnItemSelectedListener(listener);
    }

    private void setChannelBondingView() {
     // Channel Bonding Spinner
        spinner_cb = (Spinner)findViewById(R.id.WiFi_Channel_Bonding_Spinner);
        ArrayAdapter<CharSequence> adapter_channels_bonding = ArrayAdapter.createFromResource(this, R.array.channel_bonding, android.R.layout.simple_spinner_item);
        adapter_channels_bonding.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_cb.setAdapter(adapter_channels_bonding);
        spinner_cb.setSelection(spinner_channel_bonding_pos);
        spinner_cb.setOnItemSelectedListener(listener);
    }

    private void setPowerView() {
     // Power Spinner
        spinner_p = (Spinner)findViewById(R.id.WiFi_Power_Spinner);
        ArrayAdapter<CharSequence> adapter_power = ArrayAdapter.createFromResource(this, R.array.power, android.R.layout.simple_spinner_item);
        adapter_power.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_p.setAdapter(adapter_power);
        spinner_p.setSelection(spinner_power_pos);
        spinner_p.setOnItemSelectedListener(listener);
    }

    private void setPackageLenView() {
        // Power Spinner
           spinner_len = (Spinner)findViewById(R.id.WiFi_pkglen_Spinner);
           ArrayAdapter<CharSequence> adapter_len = ArrayAdapter.createFromResource(this, R.array.package_len, android.R.layout.simple_spinner_item);
           adapter_len.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
           spinner_len.setAdapter(adapter_len);
           spinner_len.setSelection(spinner_power_pos);
           spinner_len.setOnItemSelectedListener(listener);
       }

    private void setChannelView() {
        // Channel Spinner
        spinner_c = (Spinner)findViewById(R.id.WiFi_Channel_Spinner);
        ArrayAdapter<CharSequence> adapter_channels = ArrayAdapter.createFromResource(this, R.array.channels_20m, android.R.layout.simple_spinner_item);
        if (spinner_channel_bonding_pos == 0 || spinner_channel_bonding_pos == 3) { // 20M BW
            adapter_channels = ArrayAdapter.createFromResource(this, R.array.channels_24G, android.R.layout.simple_spinner_item);
        } else if (spinner_channel_bonding_pos == 1 && spinner_channel_bonding_pos == 4) { // 40M BW
            adapter_channels = ArrayAdapter.createFromResource(this, R.array.channels_20m, android.R.layout.simple_spinner_item);
        } else if (spinner_channel_bonding_pos == 2 && spinner_channel_bonding_pos == 5) { // 40M BW
            adapter_channels = ArrayAdapter.createFromResource(this, R.array.channels_40m, android.R.layout.simple_spinner_item);
        } else if (spinner_channel_bonding_pos == 6){ // 80M BW
            adapter_channels = ArrayAdapter.createFromResource(this, R.array.channels_80m, android.R.layout.simple_spinner_item);
        }
        adapter_channels.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_c.setAdapter(adapter_channels);
        spinner_c.setSelection(spinner_channel_pos);
        spinner_c.setOnItemSelectedListener(listener);
    }

    private void setRateView() {
        // Rate Spinner
        spinner_r = (Spinner)findViewById(R.id.WiFi_Rate_Spinner);
        ArrayAdapter<CharSequence> adapter_rate = ArrayAdapter.createFromResource(this, R.array.rates_11b, android.R.layout.simple_spinner_item);
        if (spinner_channel_bonding_pos == 0 ) { // 20M BW
            adapter_rate = ArrayAdapter.createFromResource(this, R.array.rates_11g, android.R.layout.simple_spinner_item);
        } else if (spinner_channel_bonding_pos == 1 ) { // 20M BW
            adapter_rate = ArrayAdapter.createFromResource(this, R.array.rates_11n_20m,android.R.layout.simple_spinner_item);
        } else if (spinner_channel_bonding_pos == 2 ) { // 20M BW
            adapter_rate = ArrayAdapter.createFromResource(this, R.array.rates_11n_40m,android.R.layout.simple_spinner_item);
        } else if (spinner_channel_bonding_pos == 3 ) { // 20M BW
            adapter_rate = ArrayAdapter.createFromResource(this, R.array.rates_11b,android.R.layout.simple_spinner_item);
        } else if (spinner_channel_bonding_pos == 4 ) { // 20M BW
            adapter_rate = ArrayAdapter.createFromResource(this,R.array.rates_11ac_20m,android.R.layout.simple_spinner_item);
        } else if (spinner_channel_bonding_pos == 5 ) { // 20M BW
            adapter_rate = ArrayAdapter.createFromResource(this,R.array.rates_11ac_40m,android.R.layout.simple_spinner_item);
        } else if (spinner_channel_bonding_pos == 6 ) { // 20M BW
            adapter_rate = ArrayAdapter.createFromResource(this,R.array.rates_11ac_80m,android.R.layout.simple_spinner_item);
        }
        adapter_rate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_r.setAdapter(adapter_rate);
        spinner_r.setSelection(spinner_rate_pos);
        spinner_r.setOnItemSelectedListener(listener);
    }

    private void find_and_update_view() {
        switch (mChangeFlag) {
            case CHANGE_FLAG_ALL:
                setPowerCntlModeView();
                setPowerView();
                setChannelBondingView();
                setChannelView();
                setRateView();
                setPackageLenView();
                break;
            case CHANGE_FLAG_CHANNEL_BONDING:
                setChannelView();
                setRateView();
                break;
            case CHANGE_FLAG_CHANNEL:
                setRateView();
                break;
            default:
                break;
        }

    }

    public class MyOnItemSelectedListener implements OnItemSelectedListener {

        public void onItemSelected(AdapterView parent, View view, int position, long id) {
            int pos;
            if (parent == spinner_pc) {
                spinner_power_cntl_mode_pos = position;
            } else if(parent == spinner_p){
                spinner_power_pos = position;
            }else if (parent == spinner_cb) {
                mChangeFlag = CHANGE_FLAG_CHANNEL_BONDING;
                spinner_channel_bonding_pos = position;
                spinner_channel_pos = 0;
                spinner_rate_pos = 0;
                find_and_update_view();
            } else if (parent == spinner_c) {
                mChangeFlag = CHANGE_FLAG_CHANNEL;
                spinner_channel_pos = position;
                spinner_rate_pos = 0;
                find_and_update_view();
			} else if (parent == spinner_r) {
                spinner_rate_pos = position;
            } else if (parent == spinner_len) {
                spinner_len_pos = position;
            }
        }

        public void onNothingSelected(AdapterView parent) {
          // Do nothing.
        }
    }
}
