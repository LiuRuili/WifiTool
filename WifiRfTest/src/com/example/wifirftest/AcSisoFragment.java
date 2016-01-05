package com.example.wifirftest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import android.util.Log;

import android.widget.ToggleButton;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

import android.content.DialogInterface;

import java.lang.Runtime;
import java.lang.Process;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.BufferedReader;


public class AcSisoFragment extends Fragment {
    private String TAG = "WifiAcSiso";

    private ToggleButton mFTMStartStop;
    private ToggleButton mStartTxRx;
    private Button mCaptureRecvPktBtn;

    private TextView mTxRxText;
    private TextView mModeText;
    private TextView mChannelText;
    private TextView mRateText;
    private TextView mPowerText;
    private TextView mAntennaText;
    private TextView mBandwidthText;
    private TextView mGuardIntervalText;

    private Spinner mTxRxSpin;
    private Spinner mModeSpin;
    private Spinner mChannel5GSpin;
    private Spinner mChannel2GSpin;
    private Spinner mChannel5G40Spin;
    private Spinner mChannel5G80Spin;
    private Spinner mChannelAllSpin;
    private Spinner mRateBSpin;
    private Spinner mRateASpin;
    private Spinner mRateNSpin;
    private Spinner mRateACSpin;
    private Spinner mPowerSpin;
    private Spinner mPower11ASpin;
    private Spinner mPower11BSpin;
    private Spinner mPower11GSpin;
    private Spinner mPower11NSpin;
    private Spinner mPower11ACSpin;
    private Spinner mAntennaSpin;
    private Spinner mBandwidthNSpin;
    private Spinner mBandwidthACSpin;
    private Spinner mGuardIntervalSpin;

    private int mTxRx = 0;
    private String mMode = "b";
    private String mChannel = "1";
    private String mRate = "1";
    private String mPower = "8";
    private String mAntenna = "0";
    private String mBandwidth = "20";
    private String mSideband = "u";
    private String mGuardInterval = "0";

    private String tmpbuf;
    private String substr;

    AlertDialog.Builder dialog;
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.acsiso_view, container, false);
	}
	
	@Override
    public void onStart() {
        super.onStart();
        
        mFTMStartStop = (ToggleButton) getView().findViewById(R.id.FTMStartStop);
        mStartTxRx = (ToggleButton) getView().findViewById(R.id.StartTxRx);
        mCaptureRecvPktBtn = (Button) getView().findViewById(R.id.CaptureRecvPktBtn);

        mTxRxText = (TextView) getView().findViewById(R.id.TxRxText);
        mModeText = (TextView) getView().findViewById(R.id.ModeText);
        mChannelText = (TextView) getView().findViewById(R.id.ChannelText);
        mRateText = (TextView) getView().findViewById(R.id.RateText);
        mAntennaText = (TextView) getView().findViewById(R.id.AntennaText);
        mPowerText = (TextView) getView().findViewById(R.id.PowerText);
        mBandwidthText = (TextView) getView().findViewById(R.id.BandwidthText);
        mGuardIntervalText = (TextView) getView().findViewById(R.id.GuardIntervalText);

        mTxRxSpin = (Spinner) getView().findViewById(R.id.TxRxSpin);
        mModeSpin = (Spinner) getView().findViewById(R.id.ModeSpin);
        mChannel5GSpin = (Spinner) getView().findViewById(R.id.Channel5GSpin);
        mChannel2GSpin = (Spinner) getView().findViewById(R.id.Channel2GSpin);
        mChannel5G40Spin = (Spinner) getView().findViewById(R.id.Channel5G40Spin);
        mChannel5G80Spin = (Spinner) getView().findViewById(R.id.Channel5G80Spin);
        mChannelAllSpin = (Spinner) getView().findViewById(R.id.ChannelAllSpin);
        mRateBSpin = (Spinner) getView().findViewById(R.id.RateBSpin);
        mRateASpin = (Spinner) getView().findViewById(R.id.RateASpin);
        mRateNSpin = (Spinner) getView().findViewById(R.id.RateNSpin);
        mRateACSpin = (Spinner) getView().findViewById(R.id.RateACSpin);
        mPowerSpin = (Spinner) getView().findViewById(R.id.PowerSpin);
        mPower11ASpin = (Spinner) getView().findViewById(R.id.Power11ASpin);
        mPower11BSpin = (Spinner) getView().findViewById(R.id.Power11BSpin);
        mPower11GSpin = (Spinner) getView().findViewById(R.id.Power11GSpin);
        mPower11NSpin = (Spinner) getView().findViewById(R.id.Power11NSpin);
        mPower11ACSpin = (Spinner) getView().findViewById(R.id.Power11ACSpin);
        mAntennaSpin = (Spinner) getView().findViewById(R.id.AntennaSpin);
        mBandwidthNSpin = (Spinner) getView().findViewById(R.id.BandwidthNSpin);
        mBandwidthACSpin = (Spinner) getView().findViewById(R.id.BandwidthACSpin);
        mGuardIntervalSpin = (Spinner) getView().findViewById(R.id.GuardIntervalSpin);

        dialog = new AlertDialog.Builder(getActivity());
        removeSettings();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_spinner_item, new String[]{"Tx","Rx", "Tx Unmodulated"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTxRxSpin.setAdapter(adapter);
        mTxRxSpin.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView,
                    View view, int position, long id) {
                switch(position) {
                case 1:
                    mTxRx = 1;
                    break;
                case 2:
                    mTxRx = 2;
                    break;
                default:
                    mTxRx = 0;
                    break;
                }
                updateSettings(mTxRx, mMode, "20");
            }
            public void onNothingSelected(AdapterView arg0) {
                mTxRx = 0;
            }
        });
        adapter = new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_spinner_item,
                new String[]{"802.11b", "802.11g", "802.11n", "802.11a", "802.11ac"});
        mModeSpin.setAdapter(adapter);
        mModeSpin.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView,
                    View view, int position, long id) {
                switch(position) {
                case 1:
                    mMode = "g";
                    mBandwidth = "20";
                    break;
                case 2:
                    mMode = "n";
                    break;
                case 3:
                    mMode = "a";
                    mBandwidth = "20";
                    break;
                case 4:
                    mMode = "ac";
                    mBandwidth = "20";
                    break;
                default:
                    mMode = "b";
                    mBandwidth = "20";
                    break;
                }
                updateSettings(mTxRx, mMode, "20");
            }
            public void onNothingSelected(AdapterView arg0) {
                mMode = "b";
            }
        });
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                new String[]{"20MHz", "40MHz"});
        mBandwidthNSpin.setAdapter(adapter);
        mBandwidthNSpin.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView,
                    View view, int position, long id) {
                tmpbuf = adapterView.getSelectedItem().toString();
                substr = tmpbuf.substring(0, tmpbuf.indexOf("M"));
                //Toast.makeText(MainActivity.this, substr, Toast.LENGTH_LONG).show();
                mBandwidth = substr;
                updateSettings(mTxRx, mMode, mBandwidth);
            }
            public void onNothingSelected(AdapterView arg0) {
                mBandwidth = "20";
            }
        });
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                new String[]{"20MHz", "40MHz", "80MHz"});
        mBandwidthACSpin.setAdapter(adapter);
        mBandwidthACSpin.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView,
                    View view, int position, long id) {
                tmpbuf = adapterView.getSelectedItem().toString();
                substr = tmpbuf.substring(0, tmpbuf.indexOf("M"));
                //Toast.makeText(MainActivity.this, substr, Toast.LENGTH_LONG).show();
                mBandwidth = substr;
                updateSettings(mTxRx, mMode, mBandwidth);
            }
            public void onNothingSelected(AdapterView arg0) {
                mBandwidth = "20";
            }
        });
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,
                new String[]{"34(5170)", "36(5180)", "38(5190)", "40(5200)",
                             "42(5210)", "44(5220)", "46(5230)", "48(5240)",
                             "52(5260)", "56(5280)", "60(5300)", "64(5320)",
                             "100(5500)", "104(5520)", "108(5540)", "112(5560)",
                             "116(5580)", "120(5600)", "124(5620)", "128(5640)",
                             "132(5660)", "136(5680)", "140(5700)", "144(5720)",
                             "149(5745)", "153(5765)", "157(5785)", "161(5805)",
                             "165(5825)"});
        mChannel5GSpin.setAdapter(adapter);
        mChannel5GSpin.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView,
                    View view, int position, long id) {
                tmpbuf = adapterView.getSelectedItem().toString();
                substr = tmpbuf.substring(0, tmpbuf.indexOf("("));
                //Toast.makeText(MainActivity.this, substr, Toast.LENGTH_LONG).show();
                mChannel = substr;
            }
            public void onNothingSelected(AdapterView arg0) {

            }
        });
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                new String[]{"1(2412)", "2(2417)", "3(2422)", "4(2427)",
                             "5(2432)", "6(2437)", "7(2442)", "8(2447)",
                             "9(2452)", "10(2457)", "11(2462)", "12(2467)",
                             "13(2472)"});
        mChannel2GSpin.setAdapter(adapter);
        mChannel2GSpin.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView,
                    View view, int position, long id) {
                tmpbuf = adapterView.getSelectedItem().toString();
                substr = tmpbuf.substring(0, tmpbuf.indexOf("("));
                //Toast.makeText(MainActivity.this, substr, Toast.LENGTH_LONG).show();
                mChannel = substr;
            }
            public void onNothingSelected(AdapterView arg0) {
                mChannel = "1";
            }
        });
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                new String[]{"38(5190)", "46(5230)", "54(5270)", "62(5310)",
                             "102(5510)", "110(5550)", "118(5590)", "126(5630)",
                             "134(5670)", "142(5710)", "151(5755)", "159(5895)"});
        mChannel5G40Spin.setAdapter(adapter);
        mChannel5G40Spin.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView,
                    View view, int position, long id) {
                tmpbuf = adapterView.getSelectedItem().toString();
                substr = tmpbuf.substring(0, tmpbuf.indexOf("("));
                //Toast.makeText(MainActivity.this, substr, Toast.LENGTH_LONG).show();
                if (substr.equals("38")) {
                    mChannel = "36";
                    mSideband = "l";
                } else if (substr.equals("46")) {
                    mChannel = "48";
                    mSideband = "u";
                } else if (substr.equals("54")) {
                    mChannel = "56";
                    mSideband = "u";
                } else if (substr.equals("62")) {
                    mChannel = "64";
                    mSideband = "u";
                } else if (substr.equals("102")) {
                    mChannel = "104";
                    mSideband = "u";
                } else if (substr.equals("110")) {
                    mChannel = "112";
                    mSideband = "u";
                } else if (substr.equals("118")) {
                    mChannel = "120";
                    mSideband = "u";
                } else if (substr.equals("126")) {
                    mChannel = "128";
                    mSideband = "u";
                } else if (substr.equals("134")) {
                    mChannel = "136";
                    mSideband = "u";
                } else if (substr.equals("142")) {
                    mChannel = "144";
                    mSideband = "u";
                } else if (substr.equals("151")) {
                    mChannel = "153";
                    mSideband = "u";
                } else if (substr.equals("159")) {
                    mChannel = "161";
                    mSideband = "u";
                }
            }
            public void onNothingSelected(AdapterView arg0) {
                mChannel = "36";
                mSideband = "l";
            }
        });
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,
                new String[]{"42(5210)", "58(5290)", "106(5530)", "122(5610)",
                             "138(5690)", "155(5775)"});
        mChannel5G80Spin.setAdapter(adapter);
        mChannel5G80Spin.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView,
                    View view, int position, long id) {
                int intChannel;
                tmpbuf = adapterView.getSelectedItem().toString();
                substr = tmpbuf.substring(0, tmpbuf.indexOf("("));
                //Toast.makeText(MainActivity.this, substr, Toast.LENGTH_LONG).show();
                intChannel = Integer.parseInt(substr) - 6;
                mChannel = Integer.toString(intChannel);
            }
            public void onNothingSelected(AdapterView arg0) {
                mChannel = "36";
            }
        });
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                new String[]{"1(2412)", "2(2417)", "3(2422)", "4(2427)",
                             "5(2432)", "6(2437)", "7(2442)", "8(2447)",
                             "9(2452)", "10(2457)", "11(2462)", "12(2467)",
                             "13(2472)",
                             "34(5170)", "36(5180)", "38(5190)", "40(5200)",
                             "42(5210)", "44(5220)", "46(5230)", "48(5240)",
                             "52(5260)", "56(5280)", "60(5300)", "64(5320)",
                             "100(5500)", "104(5520)", "108(5540)", "112(5560)",
                             "116(5580)", "120(5600)", "124(5620)", "128(5640)",
                             "132(5660)", "136(5680)", "140(5700)", "144(5720)",
                             "149(5745)", "153(5765)", "157(5785)", "161(5805)",
                             "165(5825)"});
        mChannelAllSpin.setAdapter(adapter);
        mChannelAllSpin.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView,
                    View view, int position, long id) {
                tmpbuf = adapterView.getSelectedItem().toString();
                substr = tmpbuf.substring(0, tmpbuf.indexOf("("));
                //Toast.makeText(MainActivity.this, substr, Toast.LENGTH_LONG).show();
                mChannel = substr;
            }
            public void onNothingSelected(AdapterView arg0) {
                mChannel = "1";
            }
        });
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                new String[]{"1", "2", "5.5", "11"});
        mRateBSpin.setAdapter(adapter);
        mRateBSpin.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView,
                    View view, int position, long id) {
                tmpbuf = adapterView.getSelectedItem().toString();
                //Toast.makeText(MainActivity.this, tmpbuf, Toast.LENGTH_LONG).show();
                mRate = tmpbuf;

            }
            public void onNothingSelected(AdapterView arg0) {
                mRate = "1";
            }
        });
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                new String[]{"6", "9", "12", "18", "24", "36", "48", "54"});
        mRateASpin.setAdapter(adapter);
        mRateASpin.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView,
                    View view, int position, long id) {
                tmpbuf = adapterView.getSelectedItem().toString();
                //Toast.makeText(MainActivity.this, tmpbuf, Toast.LENGTH_LONG).show();
                mRate = tmpbuf;
            }
            public void onNothingSelected(AdapterView arg0) {
                mRate = "6";
            }
        });
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                new String[]{"MCS0", "MCS1", "MCS2", "MCS3", "MCS4", "MCS5", "MCS6", "MCS7"});
        mRateNSpin.setAdapter(adapter);
        mRateNSpin.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView,
                    View view, int position, long id) {
                tmpbuf = adapterView.getSelectedItem().toString();
                substr = tmpbuf.substring(tmpbuf.indexOf("S")+1, tmpbuf.length());
                //Toast.makeText(MainActivity.this, substr, Toast.LENGTH_LONG).show();
                mRate = substr;
            }
            public void onNothingSelected(AdapterView arg0) {
                mRate = "0";
            }
        });
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                new String[]{"MCS0", "MCS1", "MCS2", "MCS3", "MCS4", "MCS5", "MCS6", "MCS7", "MCS8", "MCS9"});
        mRateACSpin.setAdapter(adapter);
        mRateACSpin.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView,
                    View view, int position, long id) {
                tmpbuf = adapterView.getSelectedItem().toString();
                substr = tmpbuf.substring(tmpbuf.indexOf("S")+1, tmpbuf.length());
                //Toast.makeText(MainActivity.this, substr, Toast.LENGTH_LONG).show();
                mRate = substr;
            }
            public void onNothingSelected(AdapterView arg0) {
                mRate = "0";
            }
        });
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                new String[]{"4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19"});
        mPowerSpin.setAdapter(adapter);
        mPowerSpin.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView,
                    View view, int position, long id) {
                tmpbuf = adapterView.getSelectedItem().toString();
                //Toast.makeText(MainActivity.this, tmpbuf, Toast.LENGTH_LONG).show();
                mPower = tmpbuf;
            }
            public void onNothingSelected(AdapterView arg0) {
                mPower = "4";
            }
        });
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                new String[]{"4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18"});
        mPower11ASpin.setAdapter(adapter);
        mPower11ASpin.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView,
                    View view, int position, long id) {
                tmpbuf = adapterView.getSelectedItem().toString();
                //Toast.makeText(MainActivity.this, tmpbuf, Toast.LENGTH_LONG).show();
                mPower = tmpbuf;
            }
            public void onNothingSelected(AdapterView arg0) {
                mPower = "4";
            }
        });
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                new String[]{"4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18"});
        mPower11BSpin.setAdapter(adapter);
        mPower11BSpin.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView,
                    View view, int position, long id) {
                tmpbuf = adapterView.getSelectedItem().toString();
                //Toast.makeText(MainActivity.this, tmpbuf, Toast.LENGTH_LONG).show();
                mPower = tmpbuf;
            }
            public void onNothingSelected(AdapterView arg0) {
                mPower = "4";
            }
        });
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                new String[]{"4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18"});
        mPower11GSpin.setAdapter(adapter);
        mPower11GSpin.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView,
                    View view, int position, long id) {
                tmpbuf = adapterView.getSelectedItem().toString();
                //Toast.makeText(MainActivity.this, tmpbuf, Toast.LENGTH_LONG).show();
                mPower = tmpbuf;
            }
            public void onNothingSelected(AdapterView arg0) {
                mPower = "4";
            }
        });
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                new String[]{"4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18"});
        mPower11NSpin.setAdapter(adapter);
        mPower11NSpin.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView,
                    View view, int position, long id) {
                tmpbuf = adapterView.getSelectedItem().toString();
                //Toast.makeText(MainActivity.this, tmpbuf, Toast.LENGTH_LONG).show();
                mPower = tmpbuf;
            }
            public void onNothingSelected(AdapterView arg0) {
                mPower = "4";
            }
        });
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                new String[]{"4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18"});
        mPower11ACSpin.setAdapter(adapter);
        mPower11ACSpin.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView,
                    View view, int position, long id) {
                tmpbuf = adapterView.getSelectedItem().toString();
                //Toast.makeText(MainActivity.this, tmpbuf, Toast.LENGTH_LONG).show();
                mPower = tmpbuf;
            }
            public void onNothingSelected(AdapterView arg0) {
                mPower = "4";
            }
        });
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                new String[]{"0(main)", "1(aux)", "2(CDD)", "3(SDM)"});
        mAntennaSpin.setAdapter(adapter);
        mAntennaSpin.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView,
                    View view, int position, long id) {
                tmpbuf = adapterView.getSelectedItem().toString();
                substr = tmpbuf.substring(0, tmpbuf.indexOf("("));
                //Toast.makeText(MainActivity.this, substr, Toast.LENGTH_LONG).show();
                mAntenna = substr;
            }
            public void onNothingSelected(AdapterView arg0) {
                mAntenna = "0";
            }
        });
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                new String[]{"LONG", "SHORT"});
        mGuardIntervalSpin.setAdapter(adapter);
        mGuardIntervalSpin.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView,
                    View view, int position, long id) {
                tmpbuf = adapterView.getSelectedItem().toString();
                //Toast.makeText(MainActivity.this, substr, Toast.LENGTH_LONG).show();
                if(tmpbuf.equals("SHORT"))
                    mGuardInterval = "1";
                else
                    mGuardInterval = "0";
            }
            public void onNothingSelected(AdapterView arg0) {
                mGuardInterval = "0";
            }
        });
        mFTMStartStop.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Do something in response to button click
                    try {
                        // Executes the command.
                        Process process = Runtime.getRuntime().exec("/system/bin/WifiTest 1 1 10");

                        // Waits for the command to finish.
                        process.waitFor();

                        InputStream is = process.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr);

                        while ((tmpbuf = br.readLine()) != null) {
                            log(tmpbuf);
                            if (tmpbuf.equals("PASS")) {
                                updateSettings(mTxRx, mMode, "20");
                            } else
                                Toast.makeText(getActivity(), "FAIL", Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    // Do something in response to button click
                    try {
                        // Executes the command.
                        Process process = Runtime.getRuntime().exec("/system/bin/WifiTest 1 0 10");

                        // Waits for the command to finish.
                        process.waitFor();

                        InputStream is = process.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr);

                        while ((tmpbuf = br.readLine()) != null) {
                            log(tmpbuf);
                            if (tmpbuf.equals("PASS"))
                                removeSettings();
                            else
                                Toast.makeText(getActivity(), "FAIL", Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });
        mStartTxRx.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    disableSpin();
                    if (mTxRx == 0) {
                        // Do something in response to button click
                        try {
                            tmpbuf = "/system/bin/WifiTest 7" + " " + mMode + " " + mChannel
                                    + " " + mRate + " " + mAntenna + " " + mPower + " " + mBandwidth
                                    + " " + mSideband + " " + mGuardInterval;

                            // Executes the command.
                            Process process = Runtime.getRuntime().exec(tmpbuf);

                            // Waits for the command to finish.
                            process.waitFor();

                            InputStream is = process.getInputStream();
                            InputStreamReader isr = new InputStreamReader(is);
                            BufferedReader br = new BufferedReader(isr);

                            while ((tmpbuf = br.readLine()) != null) {
                                log(tmpbuf);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (mTxRx == 2) {
                        // Do something in response to button click
                        try {
                            tmpbuf = "/system/bin/WifiTest 8" + " " + "1" + " " + mChannel + " " + mAntenna;

                            // Executes the command.
                            Process process = Runtime.getRuntime().exec(tmpbuf);

                            // Waits for the command to finish.
                            process.waitFor();

                            InputStream is = process.getInputStream();
                            InputStreamReader isr = new InputStreamReader(is);
                            BufferedReader br = new BufferedReader(isr);

                            while ((tmpbuf = br.readLine()) != null) {
                                log(tmpbuf);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        // Do something in response to button click
                        try {
                            tmpbuf = "/system/bin/WifiTest 6" + " " + mMode + " " + mChannel
                                    + " " + mRate + " " + mAntenna + " " + mBandwidth + " " + mSideband + " " + mGuardInterval;
                            // Executes the command.
                            Process process = Runtime.getRuntime().exec(tmpbuf);

                            // Waits for the command to finish.
                            process.waitFor();

                            InputStream is = process.getInputStream();
                            InputStreamReader isr = new InputStreamReader(is);
                            BufferedReader br = new BufferedReader(isr);

                            while ((tmpbuf = br.readLine()) != null) {
                                log(tmpbuf);
                            }

                            mCaptureRecvPktBtn.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    enableSpin();
                    if (mTxRx == 0) {
                        // Do something in response to button click
                        try {
                            // Executes the command.
                            Process process = Runtime.getRuntime().exec("/system/bin/WifiTest 7 0");

                            // Waits for the command to finish.
                            process.waitFor();

                            InputStream is = process.getInputStream();
                            InputStreamReader isr = new InputStreamReader(is);
                            BufferedReader br = new BufferedReader(isr);

                            while ((tmpbuf = br.readLine()) != null) {
                                log(tmpbuf);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (mTxRx == 2) {
                        // Do something in response to button click
                        try {
                            // Executes the command.
                            Process process = Runtime.getRuntime().exec("/system/bin/WifiTest 8 0");

                            // Waits for the command to finish.
                            process.waitFor();

                            InputStream is = process.getInputStream();
                            InputStreamReader isr = new InputStreamReader(is);
                            BufferedReader br = new BufferedReader(isr);

                            while ((tmpbuf = br.readLine()) != null) {
                                log(tmpbuf);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        // Do something in response to button click
                        try {
                            // Executes the command.
                            Process process = Runtime.getRuntime().exec("/system/bin/WifiTest 6 0");

                            // Waits for the command to finish.
                            process.waitFor();

                            InputStream is = process.getInputStream();
                            InputStreamReader isr = new InputStreamReader(is);
                            BufferedReader br = new BufferedReader(isr);

                            while ((tmpbuf = br.readLine()) != null) {
                                log(tmpbuf);
                            }
                            mCaptureRecvPktBtn.setVisibility(View.GONE);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

            }
        });
        mCaptureRecvPktBtn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    // Executes the command.
                    Process process = Runtime.getRuntime().exec("/system/bin/WifiTest 6 1");

                    // Waits for the command to finish.
                    process.waitFor();

                    InputStream is = process.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);

                    while ((tmpbuf = br.readLine()) != null) {
                        dialog.setMessage(tmpbuf);
                        dialog.setCancelable(false);
                        dialog.show();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }

        });

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // 按下"收到"以後要做的事情
                dialog.dismiss();
            }
        });
    }
	
	void updateSettings(int TxRx, String mode, String bandwidth) {
        if (TxRx == 0 && mode.equals("g")) {
            removeSettings();
            mTxRxText.setVisibility(View.VISIBLE);
            mTxRxSpin.setVisibility(View.VISIBLE);
            mModeText.setVisibility(View.VISIBLE);
            mModeSpin.setVisibility(View.VISIBLE);
            mChannelText.setVisibility(View.VISIBLE);
            mChannel2GSpin.setVisibility(View.VISIBLE);
            mChannel2GSpin.setSelection(0);
            mChannel = "1";
            mRateText.setVisibility(View.VISIBLE);
            mRateASpin.setVisibility(View.VISIBLE);
            mRateASpin.setSelection(0);
            mRate = "6";
            mPowerText.setVisibility(View.VISIBLE);
            mPower11GSpin.setVisibility(View.VISIBLE);
            mPower11GSpin.setSelection(0);
            mPower = "4";
            //mAntennaText.setVisibility(View.VISIBLE);
            //mAntennaSpin.setVisibility(View.VISIBLE);
            //mAntennaSpin.setSelection(0);
            mAntenna = "0";
        } else if (TxRx == 0 && mode.equals("a")) {
            removeSettings();
            mTxRxText.setVisibility(View.VISIBLE);
            mTxRxSpin.setVisibility(View.VISIBLE);
            mModeText.setVisibility(View.VISIBLE);
            mModeSpin.setVisibility(View.VISIBLE);
            mChannelText.setVisibility(View.VISIBLE);
            mChannel5GSpin.setVisibility(View.VISIBLE);
            mChannel5GSpin.setSelection(0);
            mChannel = "36";
            mRateText.setVisibility(View.VISIBLE);
            mRateASpin.setVisibility(View.VISIBLE);
            mRateASpin.setSelection(0);
            mRate = "6";
            mPowerText.setVisibility(View.VISIBLE);
            mPower11ASpin.setVisibility(View.VISIBLE);
            mPower11ASpin.setSelection(0);
            mPower = "4";
            //mAntennaText.setVisibility(View.VISIBLE);
            //mAntennaSpin.setVisibility(View.VISIBLE);
            //mAntennaSpin.setSelection(0);
            mAntenna = "0";
        } else if (TxRx == 0 && mode.equals("n") && bandwidth.equals("20")) {
            removeSettings();
            mTxRxText.setVisibility(View.VISIBLE);
            mTxRxSpin.setVisibility(View.VISIBLE);
            mModeText.setVisibility(View.VISIBLE);
            mModeSpin.setVisibility(View.VISIBLE);
            mBandwidthText.setVisibility(View.VISIBLE);
            mBandwidthNSpin.setVisibility(View.VISIBLE);
            mBandwidthNSpin.setSelection(0);
            mChannelText.setVisibility(View.VISIBLE);
            mChannelAllSpin.setVisibility(View.VISIBLE);
            mChannelAllSpin.setSelection(0);
            mChannel = "1";
            mRateText.setVisibility(View.VISIBLE);
            mRateNSpin.setVisibility(View.VISIBLE);
            mRateNSpin.setSelection(0);
            mRate = "0";
            mPowerText.setVisibility(View.VISIBLE);
            mPower11NSpin.setVisibility(View.VISIBLE);
            mPower11NSpin.setSelection(0);
            mPower = "4";
            //mAntennaText.setVisibility(View.VISIBLE);
            //mAntennaSpin.setVisibility(View.VISIBLE);
            //mAntennaSpin.setSelection(0);
            mAntenna = "0";
            mBandwidth = "20";
            mGuardIntervalText.setVisibility(View.VISIBLE);
            mGuardIntervalSpin.setVisibility(View.VISIBLE);
            mGuardIntervalSpin.setSelection(0);
        } else if (TxRx == 0 && mode.equals("n") && bandwidth.equals("40")) {
            removeSettings();
            mTxRxText.setVisibility(View.VISIBLE);
            mTxRxSpin.setVisibility(View.VISIBLE);
            mModeText.setVisibility(View.VISIBLE);
            mModeSpin.setVisibility(View.VISIBLE);
            mBandwidthText.setVisibility(View.VISIBLE);
            mBandwidthNSpin.setVisibility(View.VISIBLE);
            mBandwidthNSpin.setSelection(1);
            mChannelText.setVisibility(View.VISIBLE);
            mChannel5G40Spin.setVisibility(View.VISIBLE);
            mChannel5G40Spin.setSelection(0);
            mChannel = "36";
            mSideband = "l";
            mRateText.setVisibility(View.VISIBLE);
            mRateNSpin.setVisibility(View.VISIBLE);
            mRateNSpin.setSelection(0);
            mRate = "0";
            mPowerText.setVisibility(View.VISIBLE);
            mPower11NSpin.setVisibility(View.VISIBLE);
            mPower11NSpin.setSelection(0);
            mPower = "4";
            //mAntennaText.setVisibility(View.VISIBLE);
            //mAntennaSpin.setVisibility(View.VISIBLE);
            //mAntennaSpin.setSelection(0);
            mAntenna = "0";
            mBandwidth = "40";
            mGuardIntervalText.setVisibility(View.VISIBLE);
            mGuardIntervalSpin.setVisibility(View.VISIBLE);
            mGuardIntervalSpin.setSelection(0);
        } else if (TxRx == 0 && mode.equals("ac") && bandwidth.equals("20")) {
            removeSettings();
            mTxRxText.setVisibility(View.VISIBLE);
            mTxRxSpin.setVisibility(View.VISIBLE);
            mModeText.setVisibility(View.VISIBLE);
            mModeSpin.setVisibility(View.VISIBLE);
            mBandwidthText.setVisibility(View.VISIBLE);
            mBandwidthACSpin.setVisibility(View.VISIBLE);
            mBandwidthACSpin.setSelection(0);
            mChannelText.setVisibility(View.VISIBLE);
            mChannel5GSpin.setVisibility(View.VISIBLE);
            mChannel5GSpin.setSelection(0);
            mChannel = "36";
            mRateText.setVisibility(View.VISIBLE);
            mRateACSpin.setVisibility(View.VISIBLE);
            mRateACSpin.setSelection(0);
            mRate = "0";
            mPowerText.setVisibility(View.VISIBLE);
            mPower11ACSpin.setVisibility(View.VISIBLE);
            mPower11ACSpin.setSelection(0);
            mPower = "4";
            //mAntennaText.setVisibility(View.VISIBLE);
            //mAntennaSpin.setVisibility(View.VISIBLE);
            //mAntennaSpin.setSelection(0);
            mAntenna = "0";
            mBandwidth = "20";
            mGuardIntervalText.setVisibility(View.VISIBLE);
            mGuardIntervalSpin.setVisibility(View.VISIBLE);
            mGuardIntervalSpin.setSelection(0);
        } else if (TxRx == 0 && mode.equals("ac") && bandwidth.equals("40")) {
            removeSettings();
            mTxRxText.setVisibility(View.VISIBLE);
            mTxRxSpin.setVisibility(View.VISIBLE);
            mModeText.setVisibility(View.VISIBLE);
            mModeSpin.setVisibility(View.VISIBLE);
            mBandwidthText.setVisibility(View.VISIBLE);
            mBandwidthACSpin.setVisibility(View.VISIBLE);
            mBandwidthACSpin.setSelection(1);
            mChannelText.setVisibility(View.VISIBLE);
            mChannel5G40Spin.setVisibility(View.VISIBLE);
            mChannel5G40Spin.setSelection(0);
            mChannel = "36";
            mSideband = "l";
            mRateText.setVisibility(View.VISIBLE);
            mRateACSpin.setVisibility(View.VISIBLE);
            mRateACSpin.setSelection(0);
            mRate = "0";
            mPowerText.setVisibility(View.VISIBLE);
            mPower11ACSpin.setVisibility(View.VISIBLE);
            mPower11ACSpin.setSelection(0);
            mPower = "4";
            //mAntennaText.setVisibility(View.VISIBLE);
            //mAntennaSpin.setVisibility(View.VISIBLE);
            //mAntennaSpin.setSelection(0);
            mAntenna = "0";
            mBandwidth = "40";
            mGuardIntervalText.setVisibility(View.VISIBLE);
            mGuardIntervalSpin.setVisibility(View.VISIBLE);
            mGuardIntervalSpin.setSelection(0);
        } else if (TxRx == 0 && mode.equals("ac") && bandwidth.equals("80")) {
            removeSettings();
            mTxRxText.setVisibility(View.VISIBLE);
            mTxRxSpin.setVisibility(View.VISIBLE);
            mModeText.setVisibility(View.VISIBLE);
            mModeSpin.setVisibility(View.VISIBLE);
            mBandwidthText.setVisibility(View.VISIBLE);
            mBandwidthACSpin.setVisibility(View.VISIBLE);
            mBandwidthACSpin.setSelection(2);
            mChannelText.setVisibility(View.VISIBLE);
            mChannel5G80Spin.setVisibility(View.VISIBLE);
            mChannel5G80Spin.setSelection(0);
            mChannel = "36";
            mRateText.setVisibility(View.VISIBLE);
            mRateACSpin.setVisibility(View.VISIBLE);
            mRateACSpin.setSelection(0);
            mRate = "0";
            mPowerText.setVisibility(View.VISIBLE);
            mPower11ACSpin.setVisibility(View.VISIBLE);
            mPower11ACSpin.setSelection(0);
            mPower = "4";
            //mAntennaText.setVisibility(View.VISIBLE);
            //mAntennaSpin.setVisibility(View.VISIBLE);
            //mAntennaSpin.setSelection(0);
            mAntenna = "0";
            mBandwidth = "80";
            mGuardIntervalText.setVisibility(View.VISIBLE);
            mGuardIntervalSpin.setVisibility(View.VISIBLE);
            mGuardIntervalSpin.setSelection(0);
        } else if (TxRx == 1 && mode.equals("g")) {
            removeSettings();
            mTxRxText.setVisibility(View.VISIBLE);
            mTxRxSpin.setVisibility(View.VISIBLE);
            mModeText.setVisibility(View.VISIBLE);
            mModeSpin.setVisibility(View.VISIBLE);
            mChannelText.setVisibility(View.VISIBLE);
            mChannel2GSpin.setVisibility(View.VISIBLE);
            mChannel2GSpin.setSelection(0);
            mChannel = "1";
            //mAntennaText.setVisibility(View.VISIBLE);
            //mAntennaSpin.setVisibility(View.VISIBLE);
            //mAntennaSpin.setSelection(0);
            mAntenna = "0";
        } else if (TxRx == 1 && mode.equals("a")) {
            removeSettings();
            mTxRxText.setVisibility(View.VISIBLE);
            mTxRxSpin.setVisibility(View.VISIBLE);
            mModeText.setVisibility(View.VISIBLE);
            mModeSpin.setVisibility(View.VISIBLE);
            mChannelText.setVisibility(View.VISIBLE);
            mChannel5GSpin.setVisibility(View.VISIBLE);
            mChannel5GSpin.setSelection(0);
            mChannel = "36";
            //mAntennaText.setVisibility(View.VISIBLE);
            //mAntennaSpin.setVisibility(View.VISIBLE);
            //mAntennaSpin.setSelection(0);
            mAntenna = "0";
        } else if (TxRx == 1 && mode.equals("b")) {
            removeSettings();
            mTxRxText.setVisibility(View.VISIBLE);
            mTxRxSpin.setVisibility(View.VISIBLE);
            mModeText.setVisibility(View.VISIBLE);
            mModeSpin.setVisibility(View.VISIBLE);
            mChannelText.setVisibility(View.VISIBLE);
            mChannel2GSpin.setVisibility(View.VISIBLE);
            mChannel2GSpin.setSelection(0);
            mChannel = "1";
            //mAntennaText.setVisibility(View.VISIBLE);
            //mAntennaSpin.setVisibility(View.VISIBLE);
            //mAntennaSpin.setSelection(0);
            mAntenna = "0";
        } else if (TxRx == 1 && mode.equals("n") && bandwidth.equals("20")) {
            removeSettings();
            mTxRxText.setVisibility(View.VISIBLE);
            mTxRxSpin.setVisibility(View.VISIBLE);
            mModeText.setVisibility(View.VISIBLE);
            mModeSpin.setVisibility(View.VISIBLE);
            mBandwidthText.setVisibility(View.VISIBLE);
            mBandwidthNSpin.setVisibility(View.VISIBLE);
            mBandwidthNSpin.setSelection(0);
            mChannelText.setVisibility(View.VISIBLE);
            mChannelAllSpin.setVisibility(View.VISIBLE);
            mChannelAllSpin.setSelection(0);
            mChannel = "1";
            //mAntennaText.setVisibility(View.VISIBLE);
            //mAntennaSpin.setVisibility(View.VISIBLE);
            //mAntennaSpin.setSelection(0);
            mAntenna = "0";
            mBandwidth = "20";
        } else if (TxRx == 1 && mode.equals("n") && bandwidth.equals("40")) {
            removeSettings();
            mTxRxText.setVisibility(View.VISIBLE);
            mTxRxSpin.setVisibility(View.VISIBLE);
            mModeText.setVisibility(View.VISIBLE);
            mModeSpin.setVisibility(View.VISIBLE);
            mBandwidthText.setVisibility(View.VISIBLE);
            mBandwidthNSpin.setVisibility(View.VISIBLE);
            mBandwidthNSpin.setSelection(1);
            mChannelText.setVisibility(View.VISIBLE);
            mChannel5G40Spin.setVisibility(View.VISIBLE);
            mChannel5G40Spin.setSelection(0);
            mChannel = "36";
            mSideband = "l";
            //mAntennaText.setVisibility(View.VISIBLE);
            //mAntennaSpin.setVisibility(View.VISIBLE);
            //mAntennaSpin.setSelection(0);
            mAntenna = "0";
            mBandwidth = "40";
        } else if (TxRx == 1 && mode.equals("ac") && bandwidth.equals("20")) {
            removeSettings();
            mTxRxText.setVisibility(View.VISIBLE);
            mTxRxSpin.setVisibility(View.VISIBLE);
            mModeText.setVisibility(View.VISIBLE);
            mModeSpin.setVisibility(View.VISIBLE);
            mBandwidthText.setVisibility(View.VISIBLE);
            mBandwidthACSpin.setVisibility(View.VISIBLE);
            mBandwidthACSpin.setSelection(0);
            mChannelText.setVisibility(View.VISIBLE);
            mChannel5GSpin.setVisibility(View.VISIBLE);
            mChannel5GSpin.setSelection(0);
            mChannel = "36";
            //mAntennaText.setVisibility(View.VISIBLE);
            //mAntennaSpin.setVisibility(View.VISIBLE);
            //mAntennaSpin.setSelection(0);
            mAntenna = "0";
            mBandwidth = "20";
        } else if (TxRx == 1 && mode.equals("ac") && bandwidth.equals("40")) {
            removeSettings();
            mTxRxText.setVisibility(View.VISIBLE);
            mTxRxSpin.setVisibility(View.VISIBLE);
            mModeText.setVisibility(View.VISIBLE);
            mModeSpin.setVisibility(View.VISIBLE);
            mBandwidthText.setVisibility(View.VISIBLE);
            mBandwidthACSpin.setVisibility(View.VISIBLE);
            mBandwidthACSpin.setSelection(1);
            mChannelText.setVisibility(View.VISIBLE);
            mChannel5G40Spin.setVisibility(View.VISIBLE);
            mChannel5G40Spin.setSelection(0);
            mChannel = "36";
            mSideband = "l";
            //mAntennaText.setVisibility(View.VISIBLE);
            //mAntennaSpin.setVisibility(View.VISIBLE);
            //mAntennaSpin.setSelection(0);
            mAntenna = "0";
            mBandwidth = "40";
        } else if (TxRx == 1 && mode.equals("ac") && bandwidth.equals("80")) {
            removeSettings();
            mTxRxText.setVisibility(View.VISIBLE);
            mTxRxSpin.setVisibility(View.VISIBLE);
            mModeText.setVisibility(View.VISIBLE);
            mModeSpin.setVisibility(View.VISIBLE);
            mBandwidthText.setVisibility(View.VISIBLE);
            mBandwidthACSpin.setVisibility(View.VISIBLE);
            mBandwidthACSpin.setSelection(2);
            mChannelText.setVisibility(View.VISIBLE);
            mChannel5G80Spin.setVisibility(View.VISIBLE);
            mChannel5G80Spin.setSelection(0);
            mChannel = "36";
            //mAntennaText.setVisibility(View.VISIBLE);
            //mAntennaSpin.setVisibility(View.VISIBLE);
            //mAntennaSpin.setSelection(0);
            mAntenna = "0";
            mBandwidth = "80"; 
        } else if (TxRx == 2) {
            removeSettings();
            mTxRxText.setVisibility(View.VISIBLE);
            mTxRxSpin.setVisibility(View.VISIBLE);
            mChannelText.setVisibility(View.VISIBLE);
            mChannelAllSpin.setVisibility(View.VISIBLE);
            mChannelAllSpin.setSelection(0);
            mChannel = "1";
            //mAntennaText.setVisibility(View.VISIBLE);
            //mAntennaSpin.setVisibility(View.VISIBLE);
            //mAntennaSpin.setSelection(0);
            mAntenna = "0";
        } else {
            removeSettings();
            mTxRxText.setVisibility(View.VISIBLE);
            mTxRxSpin.setVisibility(View.VISIBLE);
            mModeText.setVisibility(View.VISIBLE);
            mModeSpin.setVisibility(View.VISIBLE);
            mChannelText.setVisibility(View.VISIBLE);
            mChannel2GSpin.setVisibility(View.VISIBLE);
            mChannel2GSpin.setSelection(0);
            mChannel = "1";
            mRateText.setVisibility(View.VISIBLE);
            mRateBSpin.setVisibility(View.VISIBLE);
            mRateBSpin.setSelection(0);
            mRate = "1";
            mPowerText.setVisibility(View.VISIBLE);
            mPower11BSpin.setVisibility(View.VISIBLE);
            mPower11BSpin.setSelection(0);
            mPower = "4";
            //mAntennaText.setVisibility(View.VISIBLE);
            //mAntennaSpin.setVisibility(View.VISIBLE);
            //mAntennaSpin.setSelection(0);
            mAntenna = "0";
        }
        mStartTxRx.setClickable(true);
    }

    void disableSpin() {
        mFTMStartStop.setClickable(false);
        mTxRxSpin.setClickable(false);
        mModeSpin.setClickable(false);
        mChannel2GSpin.setClickable(false);
        mChannel5GSpin.setClickable(false);
        mChannel5G40Spin.setClickable(false);
        mChannelAllSpin.setClickable(false);
        mChannel5G80Spin.setClickable(false);
        mRateBSpin.setClickable(false);
        mRateASpin.setClickable(false);
        mRateNSpin.setClickable(false);
        mRateACSpin.setClickable(false);
        mPowerSpin.setClickable(false);
        mPower11ASpin.setClickable(false);
        mPower11BSpin.setClickable(false);
        mPower11GSpin.setClickable(false);
        mPower11NSpin.setClickable(false);
        mPower11ACSpin.setClickable(false);
        mAntennaSpin.setClickable(false);
        mBandwidthNSpin.setClickable(false);
        mBandwidthACSpin.setClickable(false);
        mGuardIntervalSpin.setClickable(false);
    }

    void enableSpin() {
        mFTMStartStop.setClickable(true);
        mTxRxSpin.setClickable(true);
        mModeSpin.setClickable(true);
        mChannel2GSpin.setClickable(true);
        mChannel5GSpin.setClickable(true);
        mChannel5G40Spin.setClickable(true);
        mChannelAllSpin.setClickable(true);
        mChannel5G80Spin.setClickable(true);
        mRateBSpin.setClickable(true);
        mRateASpin.setClickable(true);
        mRateNSpin.setClickable(true);
        mRateACSpin.setClickable(true);
        mPowerSpin.setClickable(true);
        mPower11ASpin.setClickable(true);
        mPower11BSpin.setClickable(true);
        mPower11GSpin.setClickable(true);
        mPower11NSpin.setClickable(true);
        mPower11ACSpin.setClickable(true);
        mAntennaSpin.setClickable(true);
        mBandwidthNSpin.setClickable(true);
        mBandwidthACSpin.setClickable(true);
        mGuardIntervalSpin.setClickable(true);
    }

    void removeSettings() {
        mStartTxRx.setClickable(false);
        mTxRxText.setVisibility(View.GONE);
        mTxRxSpin.setVisibility(View.GONE);
        mModeText.setVisibility(View.GONE);
        mModeSpin.setVisibility(View.GONE);
        mChannelText.setVisibility(View.GONE);
        mChannel2GSpin.setVisibility(View.GONE);
        mChannel5GSpin.setVisibility(View.GONE);
        mChannel5G40Spin.setVisibility(View.GONE);
        mChannelAllSpin.setVisibility(View.GONE);
        mChannel5G80Spin.setVisibility(View.GONE);
        mRateText.setVisibility(View.GONE);
        mRateBSpin.setVisibility(View.GONE);
        mRateASpin.setVisibility(View.GONE);
        mRateNSpin.setVisibility(View.GONE);
        mRateACSpin.setVisibility(View.GONE);
        mPowerText.setVisibility(View.GONE);
        mPowerSpin.setVisibility(View.GONE);
        mPower11ASpin.setVisibility(View.GONE);
        mPower11BSpin.setVisibility(View.GONE);
        mPower11GSpin.setVisibility(View.GONE);
        mPower11NSpin.setVisibility(View.GONE);
        mPower11ACSpin.setVisibility(View.GONE);
        mAntennaText.setVisibility(View.GONE);
        mAntennaSpin.setVisibility(View.GONE);
        mBandwidthText.setVisibility(View.GONE);
        mBandwidthNSpin.setVisibility(View.GONE);
        mBandwidthACSpin.setVisibility(View.GONE);
        mGuardIntervalText.setVisibility(View.GONE);
        mGuardIntervalSpin.setVisibility(View.GONE);
        mCaptureRecvPktBtn.setVisibility(View.GONE);
    }

    void log(String str) {
        Log.d(TAG, str);
    }

}
