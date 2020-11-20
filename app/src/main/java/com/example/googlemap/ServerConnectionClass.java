package com.example.googlemap;

import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.tasks.Task;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ServerConnectionClass extends AsyncTask<Void,Void,Void> {
    private URL url;
    private String attributes;

    public ServerConnectionClass(String url,String attributes) throws MalformedURLException{
        this.url = new URL(url);
        this.attributes = attributes;
    }

    private void cache(File cacheFile){

    }
    private void cache(String cacheFile,byte[] content,boolean forceUpdate)throws Exception{
        File file = new File(cacheFile);
        if(!forceUpdate){
            if(!file.exists()) {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bos.write(content);
            }else{

            }
        }
    }
    private String oStreamWriter(OutputStream output,InputStream input) throws Exception{
        oStreamWriter(output);
        return iStreamReader(input);
    }
    private void oStreamWriter(OutputStream output) throws Exception{
        OutputStreamWriter oWriter =  new OutputStreamWriter(output);
        oWriter.write(attributes,0,attributes.length());
        output.close();
    }
    private String iStreamReader(InputStream input) throws Exception{
        BufferedInputStream iReader = new BufferedInputStream(input);
        byte[] b = new byte[1000];
        int count = 0;
        String inputString = "";
        while(iReader.read(b,count,b.length) != -1){
            inputString += String.valueOf(b);
            ++count;
            if(count+b.length > b.length && b.length-count > 0){
                iReader.read(b,count,b.length-count);
                inputString += String.valueOf(b);
                break;
            }
        }
        input.close();
        return inputString;
    }
    private Object downloadType(Object o){
        return o;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URLConnection connection = url.openConnection();
            OutputStream output = connection.getOutputStream();
            InputStream input = connection.getInputStream();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            oStreamWriter(output,input);
        }catch(Exception ex){
            Log.d("Exception",ex.toString());
        }
        return null;
    }
}
