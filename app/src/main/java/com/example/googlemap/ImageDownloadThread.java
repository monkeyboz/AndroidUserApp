package com.example.googlemap;

import android.graphics.*;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;

public class ImageDownloadThread extends AsyncTask<Void,Void,Void> {
    private ImageView imageView;
    private String url;
    /* ImageDownloadThread */
    public ImageDownloadThread(ImageView parent,String url){
        this.url = url;
        this.imageView = parent;
        Log.d("imageurl",this.url);
    }
    /* saveBitmap */
    public void saveBitmap(String file,Bitmap bitmap){
        int size = bitmap.getRowBytes() * bitmap.getHeight();
        ByteBuffer b = ByteBuffer.allocate(size);
        bitmap.copyPixelsToBuffer(b);

        byte[] bytes = new byte[size];

        try {
            FileOutputStream fileO = new FileOutputStream(file);
            fileO.write(bytes);
            fileO.flush();
            fileO.close();
        }catch(Exception ex){
            Log.d("File Exception",ex.toString());
        }
    }
    /* downloadImage */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void downloadImage(){
        Bitmap bitmap = null;
        Log.d("downloading","download enabled");
        String dir = imageView.getContext().getFilesDir()+"/";
        Log.d("cache directory",dir);
        try {
            URL test = new URL(url);

            String[] directory = test.getFile().split("/");
            File file = new File(dir+directory[directory.length-1]);
            if(!file.exists()) {
                file.createNewFile();

                InputStream inputStream = test.openConnection().getInputStream();
                FileOutputStream output = new FileOutputStream(file);

                bitmap = BitmapFactory.decodeStream(inputStream);
                bitmap = Bitmap.createScaledBitmap(bitmap,200,200,true);

                Bitmap obitmap = Bitmap.createBitmap(200,200,bitmap.getConfig());
                Canvas canvas = new Canvas(obitmap);
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setAntiAlias(true);
                canvas.drawRoundRect(new RectF(0,0,200,200),10,10,paint);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(bitmap,new Rect(0,0,bitmap.getHeight(),bitmap.getWidth()),new Rect(0,0,200,200),paint);

                obitmap.compress(Bitmap.CompressFormat.PNG,87,output);

                output.flush();
                output.close();
            }else{
                bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            }
        }catch(Exception ex) {
            Log.d("error",ex.toString());
            /*try {
                /*File file = new File(Uri.parse("android.resource://" + imageView.getContext().getCacheDir() + "/" + R.drawable.common_full_open_on_phone).getPath());
                bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            } catch (Exception vx) {
                bitmap = null;
            }*/
        }

        /*if(bitmap != null) {
            Log.d("scaling image","image scaling");
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),bitmap.getConfig());
            Canvas canvas = new Canvas(output);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            Log.d("rounding bitmap","bitmap");
            paint.setShader(new BitmapShader(output, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            canvas.drawBitmap(bitmap,0,0,paint);
            canvas.drawRoundRect((new RectF(0, 0, output.getWidth(), output.getHeight())), 100, 100, paint);
            bitmap = output;
        }*/

        if(bitmap != null) {
            Log.d("bitmap","settingup");
            imageView.setBackgroundTintBlendMode(BlendMode.CLEAR);
            imageView.post(new Runnable() {
                private Bitmap bitmap;
                @Override
                public void run() {
                    imageView.setImageBitmap(bitmap);
                }

                public Runnable init(Bitmap bitmap) {
                    this.bitmap = bitmap;
                    return (this);
                }
            }.init(bitmap));
        }else{
            Log.d("bitmap null","null");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected Void doInBackground(Void... voids) {
        downloadImage();
        return null;
    }
}
