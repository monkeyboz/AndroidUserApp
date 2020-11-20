package com.example.googlemap;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;

public class MainScreen extends Fragment {
    private View.OnClickListener clickListener;
    private View.OnTouchListener touchListener;
    private ImageView list;
    public MainScreen(){
        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MapsActivity)getActivity()).setScreen(1,false);
            }
        };
        touchListener = new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("Activity",String.valueOf((MapsActivity)getActivity()));
                ((MapsActivity)getActivity()).setScreen(1,false);
                return false;
            }
        };
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedStateInitialized){
        View view = inflater.inflate(R.layout.main_screen,parent,false);
        list = (ImageView)view.findViewById(R.id.list);
        list.setFocusableInTouchMode(true);
        if(!list.isInTouchMode()) {
            list.setOnClickListener(clickListener);
        } else {
            list.setOnTouchListener(touchListener);
        }
        return view;
    }
}
