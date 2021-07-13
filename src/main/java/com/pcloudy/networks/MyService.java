package com.pcloudy.networks;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.FileWriter;

public class MyService extends Service {
    File doc;
    int uid;
    Handler handler;
    boolean dataWrite=false;
    public MyService() {
    }

    public void Ver() {
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "pCloudy";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "BackgroundService",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Service")
                    .setContentText("Handler").build();
            startForeground(1, notification);
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "service called", Toast.LENGTH_SHORT).show();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Ver();
        String pack = intent.getStringExtra("packnam");
        Toast.makeText(this, "service started"+pack, Toast.LENGTH_SHORT).show();
        Log.e("jjjjj", "onStartCommand: "+pack );


            handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run()
                {
                    Log.e("runrunrunrun", "run: ");
                    writeData2(pack);
                handler.postDelayed(this::run,5000);
                }
        },5000);
        return START_STICKY;
    }


    private void fillNetworkStatsAll(NetworkStatsHelper networkStatsHelper,String fileName) {
        long mobileWifiRx = networkStatsHelper.getAllRxBytesMobile(this) + networkStatsHelper.getAllRxBytesWifi();
        long mobileWifiTx = networkStatsHelper.getAllTxBytesMobile(this) + networkStatsHelper.getAllTxBytesWifi();
        Log.d("appsdatalog",""+fileName+"  = "+mobileWifiRx);
        String textToSaveString = String.valueOf(mobileWifiRx);
        String Txstring = String.valueOf(mobileWifiTx);
        writeToFile(textToSaveString,Txstring);
    }


    private void writeToFile(String textToSaveString,String Txstr) {
        Log.e("Repeatthisfun", "writeToFile: ");
        try {

            File file=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if(!file.exists()) {
                file.mkdir();
            }
            File file1=new File(file,"net.txt");
            if(!dataWrite)
            {
               file1.delete();
               dataWrite=true;
            }
            if(!file1.exists()) {
                file1.createNewFile();
                FileWriter writer = new FileWriter(file1,true);
                writer.append("Rx"+" "+"Tx\n");
                writer.flush();
                writer.close();
                Log.e("Directory",file.getAbsolutePath());
            }


            Log.e("Directory",file.getAbsolutePath());
            FileWriter writer = new FileWriter(file1,true);
            Log.e("aaaa", "writeToFile: "+textToSaveString);
            writer.append(textToSaveString + " " + Txstr + " \n");
            writer.flush();
            writer.close();
        } catch (Exception e) {
            Log.e("TAG", "File write failed: " + e.toString());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dataWrite=false;
    }

    private void writeData2(String packageName)
    {
        Log.e("ttttttttttt", "writeData2: " );
        uid = PackageManagerHelper.getPackageUid(this, packageName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkStatsManager networkStatsManager = (NetworkStatsManager) getSystemService(NETWORK_STATS_SERVICE);
            NetworkStatsHelper networkStatsHelper = new NetworkStatsHelper(networkStatsManager, uid);
            NetworkStats networkStats = null;
            try {
                networkStats = networkStatsManager.queryDetailsForUid(
                        ConnectivityManager.TYPE_WIFI,
                        "",
                        0,
                        System.currentTimeMillis(),
                        uid);
            } catch (Exception e) {

            }

            long rxBytes = 0L;
            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
            while (networkStats.hasNextBucket()) {
                networkStats.getNextBucket(bucket);
                rxBytes += bucket.getRxBytes();
            }
            networkStats.close();
            PackageManager packageManager= getApplicationContext().getPackageManager();
            String appName="";
            try {
                appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA));
                }
            catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            fillNetworkStatsAll(networkStatsHelper, appName);
        }
    }
}