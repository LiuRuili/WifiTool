package com.android.WLANTestMode;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.util.TctLog;

public class WLANTestModeWaveGenerator extends Activity implements OnClickListener{
    private Spinner channel_Spinner;
    private Spinner dgain_Spinner;
    private Spinner rfgain_Spinner;
    private int spinner_channel_pos = 0;
    private int spinner_dgain_pos = 0;
    private int spinner_rfgain_pos = 0;
    private Button startButton;
    private Button stopButton;
    private WLANTestModeNative mWlanTestModeNative;
    private WifiManager mWifiManager;
    private OnItemSelectedListener listener = new MyOnItemSelectedListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wlantest_mode_wave_generator);
        channel_Spinner = (Spinner) findViewById(R.id.wave_channel_spinner);
        dgain_Spinner = (Spinner) findViewById(R.id.digital_gain_spinner);
        rfgain_Spinner = (Spinner) findViewById(R.id.rf_gain_spinner);
        channel_Spinner.setOnItemSelectedListener(listener);
        dgain_Spinner.setOnItemSelectedListener(listener);
        rfgain_Spinner.setOnItemSelectedListener(listener);
        startButton = (Button) findViewById(R.id.WiFi_Wave_Start);
        stopButton = (Button) findViewById(R.id.WiFi_Wave_Stop);
        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        mWlanTestModeNative = new WLANTestModeNative();
        mWifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        mWifiManager.setWifiTestModeEnabled(false);
    }

    public void onClick(View view) {
        switch(view.getId()) {
        case R.id.WiFi_Wave_Start:
            onClickBtnWaveStart();
            break;

        case R.id.WiFi_Wave_Stop:
            onClickBtnWaveStop();
            break;

        default:
            break;
        }

    }

    public void onClickBtnWaveStart() {
        startButton.setEnabled(false);
        mWlanTestModeNative.wifi_testmode_set_type(8);
        try {
            Process p = Runtime.getRuntime().exec("chmod 777 /data/wl/type");
            p.waitFor();
        } catch (Exception e) {
            TctLog.e("onClickBtnWaveStart", "Chmod 777 /data/wl/type failed");
        }
        mWlanTestModeNative.wifi_testmode_set_channel(spinner_channel_pos);
        mWlanTestModeNative.wifi_testmode_set_dgain(spinner_dgain_pos);
        mWlanTestModeNative.wifi_testmode_set_rfgain(spinner_rfgain_pos);

        mWifiManager.setWifiTestModeEnabled(true);
        mWifiManager.setWifiTestModeStartTx();
    }

    public void onClickBtnWaveStop() {
        mWifiManager.setWifiTestModeStopTx();
        mWifiManager.setWifiTestModeEnabled(false);
        startButton.setEnabled(true);
    }

    public class MyOnItemSelectedListener implements OnItemSelectedListener {

        public void onItemSelected(AdapterView parent, View view, int position, long id) {
            if (parent == channel_Spinner) {
                spinner_channel_pos = position;
            }else if (parent == dgain_Spinner) {
                spinner_dgain_pos = position;
            }else if (parent == rfgain_Spinner){
                spinner_rfgain_pos = position;
            }
        }

        public void onNothingSelected(AdapterView parent) {
          // Do nothing.
        }
    }
}
