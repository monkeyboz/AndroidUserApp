package com.example.googlemap;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class LoginClass extends Thread{
    private String username;
    private String password;
    private int userId;
    private String sessionId;
    private String loginUrl;
    private boolean loggedin;
    public LoginClass(String username,String password,String loginUrl) {
        this.username = username;
        this.password = password;
        this.loginUrl = loginUrl;
        try {
            this.testLogin();
        } catch (Exception ex) {
            Log.d("login exception", "no long in for you");
        }
    }
    public LoginClass(String username, int userId,String sessionId){
        this.username = username;
        this.userId = userId;
        this.sessionId = sessionId;
        this.loginUrl = "otherlogin";
        try{
            this.testLogin();
        }catch(Exception ex){
            Log.d("Exception testing","something");
        }
    }
    public boolean login(String username,String password){
        this.username = username;
        this.password = password;
        this.run();
        return false;
    }
    public boolean testLogin() throws Exception {
        URLConnection url = new URL(this.loginUrl).openConnection();
        url.connect();
        InputStream input = new BufferedInputStream(url.getInputStream());
        byte[] m = new byte[100];
        String string = "";
        while(input.read(m,0,m.length) != -1) {
            string += String.valueOf(m);
        }
        Log.d("InputStream",string);
        JSONObject jsonObject = new JSONObject(string);
        JSONArray a = jsonObject.getJSONArray("login");
        for(int i = 0; i < a.length(); ++i){

        }
        this.loggedin = true;
        return false;
    }
    @Override
    public void run(){
        try {
            if (testLogin()) {
                Log.d("Login is available", "Logged in");
            } else {
                Log.d("Login not available", "Still Logged out");
            }
        }catch(Exception ex){
            Log.d("Something went wrong","not sure what but it should work now");
        }
    }
}
