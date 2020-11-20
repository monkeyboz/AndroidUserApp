package com.example.googlemap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.*;

public class DownloadImageTask extends AsyncTask<String, Void, Dictionary<String,Dictionary<Bitmap,ImageView>>> {
    private List<ImageView> imageViews;
    private List<String> images;
    public DownloadImageTask(List<ImageView> imageViews, List<String> images){
        this.imageViews = imageViews;
        this.images = images;
    }
    @Override
    protected void onPostExecute(Dictionary<String,Dictionary<Bitmap,ImageView>> result){
        Enumeration<String> keys = result.keys();
        for (Enumeration<String> it = keys; it.hasMoreElements();) {
            String m = it.nextElement();
            Enumeration<Bitmap> v = result.get(m).keys();
            for(Enumeration<Bitmap> mb = v; mb.hasMoreElements();) {
                Bitmap bitmap = mb.nextElement();
                result.get(m).get(bitmap).setImageBitmap(bitmap);
            }
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        Log.d("progress","testing"+values.toString());
        super.onProgressUpdate(values);
    }

    @Override
    protected Dictionary<String, Dictionary<Bitmap,ImageView>> doInBackground(String... strings){
        Dictionary<String,Dictionary<Bitmap,ImageView>> d = new Hashtable();
        for(int i = 0; i < images.size();++i) {
            try {
                InputStream in = new java.net.URL(images.get(i)).openStream();
                Dictionary<Bitmap,ImageView> m = null;
                m.put(BitmapFactory.decodeStream(in),imageViews.get(i));
                d.put(images.get(i),m);
            }catch(Exception e){
                d.put(images.get(i),null);
            }
        }
        return d;
    }
}
