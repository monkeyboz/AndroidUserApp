package com.example.googlemap;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.List;

public class ListClass extends Fragment {
    private List listItems;
    private ListItem[] listItemsArray;
    private String title;
    private TextView titleView;
    private RecyclerView recyclerView;
    private static RecyclerView.LayoutManager layoutManager;
    private static RecyclerView.Adapter adapter;
    private View.OnClickListener onClick;
    private static String filesDir;
    private int screen;

    public ListClass(){

    }

    public static ListClass newInstance(String title, List listItems, int screen){
        Bundle b = new Bundle();
        ListClass l = new ListClass();
        l.setTitle(title);
        l.setListItems(listItems);
        Log.d("listItems",String.valueOf(listItems));
        l.setScreen(screen);
        Log.d("screen",l.getTitle());
        b.putSerializable("listItems", (Serializable) l.getListItems());
        b.putInt("screen",screen);
        b.putString("title",title);
        l.setArguments(b);


        return l;
    }

    public String getTitle(){ return this.title; }
    private void setListItems(List listItemsHolder) {
        listItems = listItemsHolder;
        listItemsArray = new ListItem[listItems.size()];
    }

    private void setScreen(int screen) {
        this.screen = screen;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    public List getListItems(){
        return listItems;
    }

    @Override
    public void onSaveInstanceState(Bundle savedStateInitialized){
        super.onSaveInstanceState(savedStateInitialized);
        savedStateInitialized.putString("title",title);
        savedStateInitialized.putInt("screen",screen);
        savedStateInitialized.putSerializable("listItems",(Serializable)listItems);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle saveStateInitialized){
        View v = inflater.inflate(R.layout.list_layout, parent, false);
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(((MapsActivity)v.getContext()));

        class profileViewHolder extends RecyclerView.ViewHolder{
            ImageView userImage;
            TextView username,address;
            ListItem view;
            public profileViewHolder(View v){
                super(v);
                view = (ListItem) v.findViewById(R.id.list_item);;
            }
        }

        adapter = new RecyclerView.Adapter() {
            @Override
            public profileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profiles_card_view,parent,false);
                //View m = v.findViewById(R.id.list_item);
                return new profileViewHolder(v);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                profileViewHolder m = (profileViewHolder)holder;
                final Dictionary<String,String> items = (Dictionary<String,String>)listItems.get(position);
                m.view.setLocation(2);
                m.view.setAddressView(items.get("address"));
                m.view.setImage(items.get("image"),filesDir);
                m.view.setTitle(items.get("name"));
                m.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ListItem)v).updateInfo();
                    }
                });
            }

            @Override
            public int getItemCount() {
                return listItems.size();
            }
        };

        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.addItemDecoration(new DividerItemDecoration(((MapsActivity)v.getContext()),LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        //updateList(v);
        //if(saveStateInitialized != null) {
            //listItems = (List) saveStateInitialized.getSerializable("listItems");
            Log.d("savedState",String.valueOf(listItems));
            //title = saveStateInitialized.getString("title");
            //screen = saveStateInitialized.getInt("screen");
            setRetainInstance(true);
        /*}else{
            Log.d("savedState","unavailable");
            titleView = v.findViewById(R.id.title_text);
            setRetainInstance(true);
            updateList(v);
        }*/
        return v;
    }
    private void updateList(View v){
        titleView = (TextView)v.findViewById(R.id.title_text);
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        //updateList((CardView)v.findViewById(R.id.scrollView));
    }
    private void updateList(CardView scroll){
        titleView.setText(title);

        for(int i = 0; i < listItems.size();++i) {
            Dictionary<String,String> item = (Dictionary<String,String>)listItems.get(i);
            ListItem listItem = new ListItem(this.getContext(), item.get("name"),item.get("address"),screen);
            listItem.setTitle(item.get("name"));
            listItem.setAddressView(item.get("address"));
            listItem.setImage(item.get("image"),filesDir);
            listItem.setAddress(item.get("address"));
            scroll.addView(listItem);
        }
    }
}
