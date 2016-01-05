package com.example.wifirftest;

import android.os.Bundle;
import android.os.SystemProperties;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.lang.Runtime;
import java.lang.Process;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.BufferedReader;

import java.lang.String;

public class MainActivity extends FragmentActivity {
    private final String TAG = "WifiRfTest";
    private final int index = selectFragment();
    private String tmpbuf;

    private final int NOWIFI = 0;
    private final int BGNSISO = 1;
    private final int BGNMIMO = 2;
    private final int ABGNSISO = 3;
    private final int ABGNMIMO = 4;
    private final int ACSISO = 5;
    private final int ACMIMO = 6;
    private final int IDACSISO = 7;
    private final int MTKACSISO = 8;
    private final int MTKIDACSISO = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            switch (index) {
            case BGNSISO:
                // Create an instance of ExampleFragment
                BgnSisoFragment mBgnSisoFragment = new BgnSisoFragment();

                // In case this activity was started with special instructions from an Intent,
                // pass the Intent's extras to the fragment as arguments
                mBgnSisoFragment.setArguments(getIntent().getExtras());

                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, mBgnSisoFragment).commit();
            break;
            case ABGNSISO:
                // Create an instance of ExampleFragment
                AbgnSisoFragment mAbgnSisoFragment = new AbgnSisoFragment();

                // In case this activity was started with special instructions from an Intent,
                // pass the Intent's extras to the fragment as arguments
                mAbgnSisoFragment.setArguments(getIntent().getExtras());

                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, mAbgnSisoFragment).commit();
            break;
            case ACSISO:
                // Create an instance of ExampleFragment
                AcSisoFragment mAcSisoFragment = new AcSisoFragment();

                // In case this activity was started with special instructions from an Intent,
                // pass the Intent's extras to the fragment as arguments
                mAcSisoFragment.setArguments(getIntent().getExtras());

                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, mAcSisoFragment).commit();
            break;
            case ACMIMO:
                // Create an instance of ExampleFragment
                AcMimoFragment mAcMimoFragment = new AcMimoFragment();

                // In case this activity was started with special instructions from an Intent,
                // pass the Intent's extras to the fragment as arguments
                mAcMimoFragment.setArguments(getIntent().getExtras());

                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, mAcMimoFragment).commit();
            break;
            case ABGNMIMO:
                // Create an instance of ExampleFragment
                AbgnMimoFragment mAbgnMimoFragment = new AbgnMimoFragment();

                // In case this activity was started with special instructions from an Intent,
                // pass the Intent's extras to the fragment as arguments
                mAbgnMimoFragment.setArguments(getIntent().getExtras());

                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, mAbgnMimoFragment).commit();
            break;
            case IDACSISO:
                log("IDAcSisoFragment selected");
                // Create an instance of ExampleFragment
                IDAcSisoFragment mIDAcSisoFragment = new IDAcSisoFragment();

                // In case this activity was started with special instructions from an Intent,
                // pass the Intent's extras to the fragment as arguments
                mIDAcSisoFragment.setArguments(getIntent().getExtras());

                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, mIDAcSisoFragment).commit();
            break;
            case MTKACSISO:
                log("MTKAcSisoFragment selected");
                // Create an instance of ExampleFragment
                MTKAcSisoFragment mMTKAcSisoFragment = new MTKAcSisoFragment();

                // In case this activity was started with special instructions from an Intent,
                // pass the Intent's extras to the fragment as arguments
                mMTKAcSisoFragment.setArguments(getIntent().getExtras());

                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, mMTKAcSisoFragment).commit();
            break;
            case MTKIDACSISO:
                log("MTKIDAcSisoFragment selected");
                // Create an instance of ExampleFragment
                MTKIDAcSisoFragment mMTKIDAcSisoFragment = new MTKIDAcSisoFragment();

                // In case this activity was started with special instructions from an Intent,
                // pass the Intent's extras to the fragment as arguments
                mMTKIDAcSisoFragment.setArguments(getIntent().getExtras());

                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, mMTKIDAcSisoFragment).commit();
            break;
            default:
            break;
            }
        }
    }

    int selectFragment() {
        try {
            // Executes the command.
            Process process = Runtime.getRuntime().exec("/system/bin/WifiSetup");

            // Waits for the command to finish.
            process.waitFor();

            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            while ((tmpbuf = br.readLine()) != null) {
                log(tmpbuf);
		return Integer.parseInt(tmpbuf);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
    void log(String str) {
        Log.d(TAG, str);
    }
}
