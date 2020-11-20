package com.example.googlemap;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.*;
import java.util.concurrent.Executor;

public class ListItem extends LinearLayout {
    private String username;
    private String address;
    private int location;

    private LayoutInflater inflater;
    private TextView userView;
    private TextView addressView;
    private ImageView userImageView;
    private OnClickListener clickListener;
    private OnTouchListener touchListener;
    private String urlString;
    private DownloadReceiver downloadReceiver;

    public static final String FILTER_ACTION_KEY = "999";

    public ListItem(Context context,String name,String address,int location) {
        super(context);
        initialize(context);
        this.username = name;
        this.address = address;
        this.location = location;
        setAlpha(0);
        animate().alpha(1);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public ListItem(Context context, AttributeSet attr) {
        super(context, attr);
        initialize(context);
    }

    private void setReceiver(){
        Intent s = new Intent();
        downloadReceiver= new DownloadReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FILTER_ACTION_KEY);

        s.putExtra("url",urlString);
        s.putExtra("filepath",urlString);
        LocalBroadcastManager.getInstance(this.getContext()).registerReceiver(downloadReceiver,intentFilter);
    }
    private void unregisterReceiver(){
        downloadReceiver = null;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initialize(final Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.list_item, this, true);
        userView = findViewById(R.id.username);
        addressView = findViewById(R.id.address);
        userImageView = findViewById(R.id.user_image);

        /*touchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){

                }else {
                    updateInfo();
                }
                return false;
            }
        };
        clickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInfo();
            }
        };

        //if (this.isInTouchMode()) {
            this.setOnTouchListener(touchListener);
        /*} else {
            this.setOnClickListener(clickListener);
        }*/
    }

    public int getLocation(){
        return location;
    }

    public void updateInfo(){
        ((MapsActivity)getContext()).setUsername(username);
        ((MapsActivity)getContext()).setAddress(address);
        ((MapsActivity)getContext()).setUserimage(urlString);
        ((MapsActivity)getContext()).setScreen(location,false);
    }

    public void setTitle(String m){
        userView.setText(m);
        username = m;
    }
    public void setAddressView(String a){ addressView.setText(a);
        address = a;
    }
    public void setLocation(int location){
        this.location = location;
    }

    @Override
    protected void onDetachedFromWindow() {
        unregisterReceiver();
        super.onDetachedFromWindow();
    }

    public void setAddress(String a){
        address = a;
    }

    public void setImage(String m,final String filesDir) {
        urlString = m;

        Executor ex = new Executor() {
            @Override
            public void execute(Runnable command) {
                command.run();
            }
        };
        ImageDownloadThread thread = new ImageDownloadThread(userImageView,urlString);
        thread.execute();
        /*Intent intent = new Intent(this.getContext(),DownloadImages.class);
        intent.putExtra("url",urlString);
        intent.putExtra("filename",urlString);
        intent.putExtra("directory",((MapsActivity)getContext()).getFilesDir().toString());
        intent.putExtra("imageView",(android.os.Parcelable)userImageView);
        this.getContext().startService(intent);*/
    }
    private class DownloadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            String bitmap = intent.getStringExtra("filepath");
            try {
                FileReader fileReader = new FileReader(new File(bitmap));
            }catch(IOException ex) {

            }
            byte[] bytes = Base64.decode(bitmap,Base64.DEFAULT);
            Bitmap bitmapconv = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            try{
                userImageView.setImageBitmap(bitmapconv);
            }catch(Exception e){
                Log.d("error",e.getMessage());
            }
        }
    }
}