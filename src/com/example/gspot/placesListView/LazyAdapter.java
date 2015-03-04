package com.example.gspot.placesListView;

import java.util.ArrayList;
import java.util.List;


import com.example.gspot.PlaceClass;
import com.example.gspot.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<PlaceClass> data;
    private static LayoutInflater inflater=null;
    private List<String> userCount;
    public ImageLoader imageLoader; 
    int count=0;
    public LazyAdapter(Activity a, ArrayList<PlaceClass> d,List<String> userCount) {
    	this.userCount=userCount;
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
        
        
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null){
        	
        	
        	vi = inflater.inflate(R.layout.list_row, null);
        }       
        
        TextView title = (TextView)vi.findViewById(R.id.placename); // title
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image  
        ImageView online_image=(ImageView)vi.findViewById(R.id.onlineIcon); // online image
        TextView number = (TextView)vi.findViewById(R.id.numberOfOnline); // numberOfOnline
        title.setText(data.get(position).getName());
        
        	number.setText(userCount.get(position));
        return vi;
    }
}