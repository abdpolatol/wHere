package com.example.gspot.onlinePeople;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.gspot.MainActivity;
import com.example.gspot.R;
import com.example.gspot.User;
import com.example.gspot.newUserPage;
import com.example.gspot.myprofile.PullScrollView;
import com.example.gspot.slidingmenu.MyProfileFragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class onlinePeopleAdapter  extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<User> userItems;
    Fragment fragment=null;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
 
    public onlinePeopleAdapter(Activity activity, List<User> userList) {
        this.activity = activity;
        this.userItems = userList;
    }
 
    @Override
    public int getCount() {
        return userItems.size();
    }
 
    @Override
    public Object getItem(int location) {
        return userItems.get(location);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
 
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.row_online_people, null);
 
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail2);
        
        thumbNail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
                
            }
        });
        
        
        TextView title = (TextView) convertView.findViewById(R.id.title2);
        TextView rating = (TextView) convertView.findViewById(R.id.rating2);
        TextView genre = (TextView) convertView.findViewById(R.id.genre2);
        TextView year = (TextView) convertView.findViewById(R.id.releaseYear2);
 
        // getting movie data for the row
        User m = userItems.get(position);
 
        // thumbnail image
        thumbNail.setImageUrl(m.getImageUrl(), imageLoader);
        
        // title
        title.setText(m.getName()+" " +m.getSurname());
         
        // rating
        rating.setText("Online ");
         
        
 
        return convertView;
    }
 
}