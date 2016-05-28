
package com.android.WLANTestMode;


import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import com.android.internal.telephony.TelephonyIntents;
import android.util.Config;
import android.util.TctLog;

public class TestModeReceiver extends BroadcastReceiver {
    private static final String TAG = "TestModeReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Config.LOGD) TctLog.d(TAG, "onReceive " + intent);

        // fetch up useful stuff
        String action = intent.getAction();
        String host = intent.getData() != null ? intent.getData().getHost() : null;

        // WLAN Test Mode, *#*#364463#*#*
        if (TelephonyIntents.SECRET_CODE_ACTION.equals(action) && "364463".equals(host)) {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setClassName("com.android.WLANTestMode","com.android.WLANTestMode.WLANTestMode");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
