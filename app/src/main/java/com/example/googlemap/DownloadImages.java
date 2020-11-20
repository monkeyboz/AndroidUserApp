package com.example.googlemap;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadImages extends IntentService {
    private Bitmap BITMAP;
    private int result = Activity.RESULT_CANCELED;
    public static final String NOTIFICATION = "";
    public static final String URL = "url";
    public static final String RESULT = "result";
    public static final String FILENAME = "filename";
    public static final String FILEPATH = "filepath";
    public static final String DIRECTORY = "directory";
    private Bitmap bitmap;
    public DownloadImages(){
        super("DownloadImages");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String url = intent.getStringExtra(URL);
        String filename = intent.getStringExtra(FILENAME);
        String directory = intent.getStringExtra(DIRECTORY);
        URL t = null;
        try {
            t = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        File output = new File(directory,t.getFile());
        String outputPath = directory+"/"+t.getFile();
        if(output.exists()) {
            result = Activity.RESULT_OK;
            broadcastIntent(outputPath,result,intent);
        }else {

            int result = 0;
            try {
                File f = new File(output.getPath());
                if (!f.exists()) {
                    URL urlObj = new URL(url);
                    InputStream stream = urlObj.openConnection().getInputStream();
                    InputStreamReader reader = new InputStreamReader(stream);
                    FileOutputStream fos = new FileOutputStream(output.getPath());
                    int next = -1;

                    while ((next = reader.read()) != -1) {
                        fos.write(next);
                    }
                    result = Activity.RESULT_OK;
                }
            } catch (IOException ex) {
                Log.d("result", String.valueOf(result));
            }
            broadcastIntent(outputPath, result, intent);
        }
    }

    private void broadcastIntent(String output,int result,Intent intent){
        intent.putExtra(FILEPATH,output);
        intent.putExtra(RESULT,result);
        intent.setAction("900");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }
}
