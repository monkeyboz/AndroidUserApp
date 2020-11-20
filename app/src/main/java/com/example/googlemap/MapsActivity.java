package com.example.googlemap;

import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.Executor;

public class MapsActivity extends FragmentActivity {
    private GoogleMaps mMap;
    private MainScreen mainScreen;
    private ListClass list;
    private String[] screens;
    private int currScreen = -1;
    private static String filesDirectory;
    private String tag;
    private Fragment fragment;
    private String username;
    private String city;
    private String state;
    private String userimage;
    private String address;

    private Runnable googleMapsFragmentRunnable;
    private Runnable userProfilesRunnable;
    private Runnable mainScreenRunnable;

    private Executor executor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        executor = new Executor() {
            @Override
            public void execute(Runnable command) {
                command.run();
            }
        };
        mainScreenRunnable = new Runnable() {
            @Override
            public void run() {
                fragment = new MainScreen();
                fragment.setArguments(getIntent().getExtras());
                tag = "FragmentMain";
            }
        };
        fragment = new MainScreen();
        fragment.setArguments(getIntent().getExtras());
        tag = "FragmentMain";
        userProfilesRunnable = new Runnable() {
            @Override
            public void run() {
                List l = getUsers();
                fragment = ListClass.newInstance("Users", l, 2);
                fragment.setArguments(getIntent().getExtras());
                tag = "FragmentProfile";
            }
        };
        googleMapsFragmentRunnable = new Runnable() {
            @Override
            public void run() {
                fragment = GoogleMaps.newInstance(username, userimage, address);
                fragment.setArguments(getIntent().getExtras());
                tag = "FragmentMap";
            }
        };

        address = "Santa Monica, CA 90401";
        username = "testing";
        userimage = "";
        if(savedInstanceState == null) {
            filesDirectory = getFilesDir().toString()+'/';
            setContentView(R.layout.main_activity);

            if (currScreen == -1) {
                currScreen = 0;
                setScreen(currScreen,true);
            }else{
                setScreen(currScreen,false);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currScreen",currScreen);
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        currScreen = savedInstanceState.getInt("currScreen");
        setScreen(currScreen,false);
    }

    @Override
    public void onStateNotSaved() {
        super.onStateNotSaved();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if(currScreen != 0){
            currScreen--;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
    }
    public String getDirectory(){
        return filesDirectory+"/";
    }
    public void setScreen(int screen,boolean init) {
        Log.d("Screen",String.valueOf(screen));
        Log.d("current screen",String.valueOf(this.currScreen));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (screen != this.currScreen) {
            this.currScreen = screen;
            transaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
            switchScreen(this.currScreen);
            transaction.replace(R.id.fragment_container, fragment, tag);
            transaction.addToBackStack(null);
            transaction.commit();
        }else{
            if(init) {
                this.switchScreen(this.currScreen);
                Log.d("testing",String.valueOf(fragment));
                transaction.add(R.id.fragment_container, fragment, tag).commit();
            } else{
                Log.d("testing","updated");
                this.switchScreen(this.currScreen);
                transaction.replace(R.id.fragment_container, fragment, tag);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }
    }
    public void setAddress(String address){
        this.address = address;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getUserName(){
        return username;
    }
    public String getAddress(){
        return address;
    }
    public void setUserimage(String userImage){
        this.userimage = userImage;
    }

    private void switchScreen(int screen) {
        switch (screen) {
            case 0:
                executor.execute(mainScreenRunnable);
                break;
            case 1:
                executor.execute(userProfilesRunnable);
                break;
            case 2:
                executor.execute(googleMapsFragmentRunnable);
                break;
            case 3:
                executor.execute(userProfilesRunnable);
                /*fragment = UserProfileEdit.newInstance(username,address,city,state);
                fragment.setArguments(getIntent().getExtras());
                tag = "FragmentEditProfile";*/
                break;
            default:
                break;
        }
    }

    public void sendUserInformation(String username,String address,String city,String state){
        this.username = username;
        this.address = address;
        this.city = city;
        this.state = state;
    }

    private List getUsers(){
        List l = new ArrayList();
        try {
            String users = "{\"users\":[" +
                    "{\"name\":\"Taurean Wooley\",\"image\":\"http://www.magpictures.com/resources/presskits/diary/2.jpg\",\"address\":\"503 Olympic Blvd., Santa Monica, CA 90401\"}," +
                    "{\"name\":\"Monkey Boz\",\"image\":\"https://icdn5.digitaltrends.com/image/screen-shot-2019-02-15-at-19-16-58-720x720.jpg\",\"address\":\"382 San Diego dr., San Diego, CA\"},"+
                    "{\"name\":\"Monkey Boz\",\"image\":\"https://images.app.goo.gl/NnGA3L5hWL2x28xY6\",\"address\":\"382 San Diego dr., San Diego, CA\"},"+
                    "{\"name\":\"Monkey Boz\",\"image\":\"https://mymodernmet.com/wp/wp-content/uploads/2018/10/Mou-Aysha-portrait-photography-3.jpg\",\"address\":\"382 San Diego dr., San Diego, CA\"},"+
                    "{\"name\":\"Monkey Boz\",\"image\":\"https://icdn5.digitaltrends.com/image/screen-shot-2019-02-15-at-19-16-58-720x720.jpg\",\"address\":\"382 San Diego dr., San Diego, CA\"},"+
                    "{\"name\":\"Monkey Boz\",\"image\":\"https://icdn5.digitaltrends.com/image/screen-shot-2019-02-15-at-19-16-58-720x720.jpg\",\"address\":\"382 San Diego dr., San Diego, CA\"},"+
                    "{\"name\":\"Monkey Boz\",\"image\":\"https://icdn5.digitaltrends.com/image/screen-shot-2019-02-15-at-19-16-58-720x720.jpg\",\"address\":\"382 San Diego dr., San Diego, CA\"},"+
                    "{\"name\":\"Monkey Boz\",\"image\":\"https://icdn5.digitaltrends.com/image/screen-shot-2019-02-15-at-19-16-58-720x720.jpg\",\"address\":\"382 San Diego dr., San Diego, CA\"},"+
                    "{\"name\":\"Monkey Boz\",\"image\":\"https://icdn5.digitaltrends.com/image/screen-shot-2019-02-15-at-19-16-58-720x720.jpg\",\"address\":\"382 San Diego dr., San Diego, CA\"},"+
                    "{\"name\":\"Monkey Boz\",\"image\":\"https://icdn5.digitaltrends.com/image/screen-shot-2019-02-15-at-19-16-58-720x720.jpg\",\"address\":\"382 San Diego dr., San Diego, CA\"},"+
                    "]}";
            JSONObject n = new JSONObject(users);
            JSONArray a = n.getJSONArray("users");
            for(int i = 0; i < a.length();++i){
                Dictionary<String,String> m = new Hashtable<>();
                JSONObject json = a.getJSONObject(i);
                m.put("name",json.getString("name"));
                m.put("image",json.getString("image"));
                m.put("address",json.getString("address"));
                m.get("name");
                l.add(m);
            }
            return l;
        }catch(Exception ex){
            Log.d("exception",ex.getMessage());
        }
        return l;
    }
}