/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/* ========================================================================== */
/*     Modifications on Features list / Changes Request / Problems Report     */
/* -------------------------------------------------------------------------- */
/*    date   |        author        |         Key          |     comment      */
/* ----------|----------------------|----------------------|----------------- */
/* 11/30/2012|Chen Ji               |bugID329061           |develop WLANTestM */
/*           |                      |                      |ode app java code */
/* ----------|----------------------|----------------------|----------------- */
/******************************************************************************/

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
