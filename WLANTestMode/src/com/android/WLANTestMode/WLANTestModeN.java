
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
import android.widget.EditText; //[DEGUB] MOD BY TCTNB.Ruili.Liu 1885375 20160431 Add SSID and IP item to WifiTestMode

public class WLANTestModeN extends Activity implements OnClickListener {
    private Spinner spinner_c;
    private Spinner spinner_m;
    private int spinner_channel_pos = 0;
    private int spinner_mcs_pos = 0;
    private WifiManager mWifiManager;
    private EditText mSSID;
    private EditText mIP;
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
        mSSID = (EditText) findViewById(R.id.WiFi_N_SSID_edittext);
        mIP = (EditText) findViewById(R.id.EditText_IP_edittext);
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
        //[DEGUB] ADD-BEGIN BY TCTNB.Ruili.Liu 1885375 20160431 Add SSID and IP item to WifiTestMode
        String ssid = mSSID.getText().toString();
        String ip = mIP.getText().toString();

        if (ssid.length() == 0 || ip.length() == 0 ) {
            showToast("Please enter correct ssid and ip",Toast.LENGTH_SHORT);
            return;
        }

        mStartBtn.setEnabled(false);
        mTSNative.wifi_testmode_set_type(1);
        mTSNative.wifi_testmode_set_ssid(ssid);
        mTSNative.wifi_testmode_set_ip(ip);
        //[DEGUB] ADD-END BY TCTNB.Ruili.Liu

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
