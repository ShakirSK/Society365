package main.society365.maneger;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import main.society365.BuildConfig;
import main.society365.R;

import java.io.File;


public class Main2Activity extends AppCompatActivity {
    private long enqueue;
    private DownloadManager dm;
    BroadcastReceiver receiver;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

         receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(
                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(enqueue);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c
                                .getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c
                                .getInt(columnIndex)) {

                            ImageView view = (ImageView) findViewById(R.id.imageView1);
                            String uriString = c
                                    .getString(c
                                            .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));

                            Uri a = Uri.parse(uriString);
                            File d = new File(a.getPath());
                            // copy file from external to internal will esaily avalible on net use google.
                       //     view.setImageURI(a);


/*
                            Intent share = new Intent(getApplicationContext(),showpdftest_file.class);
                            // set flag to give temporary permission to external app to use your FileProvider
                            share.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            Uri apkURI = FileProvider.getUriForFile(
                                    context,
                                    BuildConfig.APPLICATION_ID  + ".myfileprovider", d);
                            share.setDataAndType(apkURI, "application/pdf");
                            share.putExtra(Intent.EXTRA_STREAM, apkURI);
                            share.setAction(Intent.ACTION_SEND);

                            startActivity(share);*/
                            Intent share = new Intent(context,showpdftest_file.class);
                            // set flag to give temporary permission to external app to use your FileProvider
                            share.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            Uri apkURI = FileProvider.getUriForFile(
                                    context,
                                    BuildConfig.APPLICATION_ID  + ".myfileprovider", d);
                            Log.d("pdfmain2", apkURI.toString());
                           share.putExtra("URL", apkURI.toString());

                            startActivity(share);

                        }
                    }
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void onClick(View view) {
      int SHARE_PERMISSION_CODE = 223;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (MarshMallowPermission.checkMashMallowPermissions(this, new String[]
                        {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                SHARE_PERMISSION_CODE)) {
                dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                String DownloadUrl = "http://150.242.14.196:8012//society//system//storage//download//partyreport//Nitin%20D.%20Shigwan_11-02-2019.pdf";
String sdsds = "http:\\/\\/society365.in\\/sms\\/images\\/Profile.png";
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(DownloadUrl)).setDestinationInExternalPublicDir("/Sohail_Temp", "entry.pdf");

                request.setDescription("http://150.242.14.196:8012//society//system//storage//download//partyreport//Nitin%20D.%20Shigwan_11-02-2019.pdf");   //appears the same in Notification bar while downloading

                enqueue = dm.enqueue(request);
            }

        }
           /* if (MarshMallowPermission.checkMashMallowPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, SHARE_PERMISSION_CODE);
        }*/

    }

    public void showDownload(View view) {
        Intent i = new Intent();
        i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
        startActivity(i);
    }
    protected void onPause() {
        super.onPause();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }

    }

}