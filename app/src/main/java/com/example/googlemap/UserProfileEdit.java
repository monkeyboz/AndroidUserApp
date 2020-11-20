package com.example.googlemap;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;

public class UserProfileEdit extends Fragment {
    EditText usernameText;
    EditText addressText;
    EditText cityText;
    EditText stateText;
    String username, address, city, state;
    Button submitEdit;
    public UserProfileEdit(String username,String address,String city,String state){
        setVariables(username,address,city,state);
    }
    public static UserProfileEdit newInstance(String username,String address, String city, String state){
        UserProfileEdit profileEdit = new UserProfileEdit(username,address,city,state);
        return profileEdit;
    }
    private void setVariables(String username,String address, String city, String state){
        this.username = username;
        this.address = address;
        this.city = city;
        this.state = state;
    }

    private void initiate(View v){
        usernameText = v.findViewById(R.id.username);
        addressText = v.findViewById(R.id.address);
        cityText = v.findViewById(R.id.city);
        stateText = v.findViewById(R.id.state);
        submitEdit = v.findViewById(R.id.submit);

        usernameText.setText(username);
        addressText.setText(address);
        cityText.setText(city);
        stateText.setText(state);
    }

    private void getInfo(){
        username = usernameText.getText().toString();
        address = addressText.getText().toString();
        city = cityText.getText().toString();
        state = stateText.getText().toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedStateInitialized){
        View v = inflater.inflate(R.layout.useredit,parent,false);

        initiate(v);

        submitEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfo();
                int userId = 0;
                String sessionId = "somethingdifferent";
                LoginClass login = new LoginClass(username,userId,sessionId);
                ((MapsActivity)getContext()).sendUserInformation(username,address,city,state);
                ((MapsActivity)getContext()).setScreen(2,false);
            }
        });
        return v;
    }
}
