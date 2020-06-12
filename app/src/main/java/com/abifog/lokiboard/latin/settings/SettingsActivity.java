/*
 * Copyright (C) 2012 The Android Open Source Project
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

package com.abifog.lokiboard.latin.settings;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import androidx.core.content.ContextCompat;
import androidx.core.view.KeyEventDispatcher;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.abifog.lokiboard.InGService;
import com.abifog.lokiboard.JobGService;
import com.abifog.lokiboard.MailWorker;
import com.abifog.lokiboard.R;
import com.abifog.lokiboard.latin.RichInputMethodManager;
import com.abifog.lokiboard.latin.utils.FragmentUtils;


import java.util.concurrent.TimeUnit;

public class SettingsActivity extends PreferenceActivity {
    private static final String DEFAULT_FRAGMENT = SettingsFragment.class.getName();
    private static final String TAG = SettingsActivity.class.getSimpleName();

    @Override
    protected void onStart() {
        super.onStart();


        boolean enabled = false;
        try {
            InputMethodManager immService = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            RichInputMethodManager.InputMethodInfoCache inputMethodInfoCache = new RichInputMethodManager.InputMethodInfoCache(immService, getPackageName());
            enabled = inputMethodInfoCache.isInputMethodOfThisImeEnabled();
        } catch (Exception e) {
            Log.e(TAG, "Exception in check if input method is enabled", e);
        }

        if (!enabled) {
            final Context context = this;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.setup_message);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
            builder.setCancelable(false);

            builder.create().show();
        }
    }

    @Override
    protected void onCreate(final Bundle savedState) {
        super.onCreate(savedState);
        final ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }



        Log.e(TAG, "Starting the Service");

        /*
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                MailWorker.class,20, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();


        WorkManager.getInstance(getApplicationContext()).enqueue(periodicWorkRequest);
        */
/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            ComponentName componentName = new ComponentName(getApplication(), JobGService.class);
            JobInfo jobInfo = new JobInfo.Builder(78788,componentName)
                    .setPersisted(true)
                    .setPeriodic(20*60*1000)
                    .build();

            JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

            int code = jobScheduler.schedule(jobInfo);

            if (code == JobScheduler.RESULT_SUCCESS){
                Log.i("jhhgd","sechuled!");
            }else {

                Intent serviceIntent = new Intent(getApplicationContext(), InGService.class);
                // serviceIntent.putExtra("inputExtra", input);
                ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);

            }

        }else {
            Intent serviceIntent = new Intent(getApplicationContext(), InGService.class);
            // serviceIntent.putExtra("inputExtra", input);
            ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);

        }


 */

        Intent serviceIntent = new Intent(SettingsActivity.this, InGService.class);
       // serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ContextCompat.startForegroundService(SettingsActivity.this, serviceIntent);


    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Intent getIntent() {
        final Intent intent = super.getIntent();
        final String fragment = intent.getStringExtra(EXTRA_SHOW_FRAGMENT);
        if (fragment == null) {
            intent.putExtra(EXTRA_SHOW_FRAGMENT, DEFAULT_FRAGMENT);
        }
        intent.putExtra(EXTRA_NO_HEADERS, true);
        return intent;
    }

    @Override
    public boolean isValidFragment(final String fragmentName) {
        return FragmentUtils.isValidFragment(fragmentName);
    }
}
