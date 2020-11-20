package com.example.googlemap;

import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.*;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import static android.view.View.*;

public class GoogleMaps extends Fragment implements OnMapReadyCallback {
    private GoogleMap GoogleMap;
    private List<ImageView> imageViews;
    private View view;
    private List<String> urls;
    private DownloadImageTask downloadImageTask;
    private SupportMapFragment mapFragment;
    private MapView mapView;
    private String address;
    private String username;

    public GoogleMaps(){

    }
    public static GoogleMaps newInstance(String username, String userImage, String address) {

        GoogleMaps m = new GoogleMaps();
        m.username = username;
        m.setImageViews();
        m.setUrls(userImage);
        m.setLocation(address);

        return m;
    }

    private void setLocation(String latlon) {
        this.address = latlon;
    }

    private void setImageViews(){
        imageViews = new ArrayList<ImageView>();
    }
    private void setUrls(String userImage){
        urls = new ArrayList<String>();
        urls.add(userImage);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedStateInitialized) {
        view = inflater.inflate(R.layout.userprofile,parent,false);
        setupInformation();
        //savedStateInitialized.putString("testing","testing");
        mapView = (MapView)view.findViewById(R.id.map);
        mapView.onCreate(savedStateInitialized);
        mapView.getMapAsync(this);

        view.setAlpha(0);

        try{
            MapsInitializer.initialize(this.getActivity());
        }catch(Exception e){
            e.printStackTrace();
        }
        return view;
    }
    public void setUsername(String username){
        this.username = username;
    }
    @Override
    public void onResume(){
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        mapView.onDestroy();
    }

    public String getUserName(){
        return username;
    }

    private void setupInformation(){
        final ImageView imageView = (ImageView) view.findViewById(R.id.user_image);
        TextView userText = (TextView)view.findViewById(R.id.username);
        TextView description = (TextView)view.findViewById(R.id.description);
        Button editProfile = view.findViewById(R.id.edit_profile);
        editProfile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MapsActivity)getContext()).setScreen(3,false);
            }
        });
        description.setText(((MapsActivity) getContext()).getAddress());
        userText.setText(((MapsActivity) getContext()).getUserName());

        Button uploadButton = view.findViewById(R.id.uploadImage);
        final Intent photo = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        try {
            HttpURLConnection http = new HttpURLConnection(new URL("http://testing.com")) {
                @Override
                public void disconnect() {

                }

                @Override
                public boolean usingProxy() {
                    return false;
                }

                @Override
                public void connect() throws IOException {

                }
            };
        }catch(Exception ex){
            Log.d("exception",ex.toString());
        }

        final Bundle result = new Bundle();

        OnClickListener clickListener = new OnClickListener(){
            @Override
            public void onClick(View v){
                startActivityForResult(photo,10,result);
            }
        };
        uploadButton.setOnClickListener(clickListener);
        imageViews.add(imageView);
        ImageDownloadThread thread = new ImageDownloadThread(imageView,urls.get(0));
        thread.execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int method,int result,Intent intent) {
        ImageView imageView = (ImageView) view.findViewById(R.id.user_image);
        try {
            Uri selectedFile = intent.getData();
            String[] fileString = selectedFile.toString().split("/");
            InputStream input = getContext().getContentResolver().openInputStream(selectedFile);

            Bitmap bitmap = BitmapFactory.decodeStream(input);
            bitmap = Bitmap.createScaledBitmap(bitmap,imageView.getWidth(),imageView.getHeight(),true);
            File file = new File(imageView.getContext().getFilesDir()+"/"+fileString[fileString.length-1]);
            OutputStream output = new FileOutputStream(file);
            Bitmap obitmap = Bitmap.createBitmap(imageView.getWidth(),imageView.getHeight(),bitmap.getConfig());
            Canvas canvas = new Canvas(obitmap);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            canvas.drawRoundRect(0,0,imageView.getWidth(),imageView.getHeight(),20,20,paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap,0,0,paint);
            obitmap.compress(Bitmap.CompressFormat.PNG,87,output);

            imageView.setImageBitmap(obitmap);
            imageView.setBackgroundTintBlendMode(BlendMode.CLEAR);
        } catch (Exception ex) {
            Log.d("exception",ex.toString());
        }
    }

    @Override
    public void onMapReady(GoogleMap gmap) {
        GoogleMap = gmap;
        GoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        GoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        GoogleMap.getUiSettings().setAllGesturesEnabled(true);
        String[] local = address.split(",");
        Double lat;
        Boolean successful = false;
        try {
            local[0] = String.valueOf(Double.valueOf(local[0]));
            local[1] = String.valueOf(Double.valueOf(local[1]));
        } catch (NumberFormatException ex) {
            Geocoder g = new Geocoder(((MapsActivity) getContext()));
            try {
                List<Address> latlon = g.getFromLocationName(address, 1);
                local[0] = String.valueOf(latlon.get(0).getLatitude());
                local[1] = String.valueOf(latlon.get(0).getLongitude());
                successful = true;
            } catch(Exception e){
                successful = false;
            }
        }
        if (local[0].length() > 2 && successful) {
            LatLng location = new LatLng(Double.valueOf(local[0]), Double.valueOf(local[1]));
            GoogleMap.addMarker(new MarkerOptions().position(location).title("locationMarker"));
            GoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,24f));
        }
        view.setAlpha(1);
    }
}