package com.example.gspot.slidingmenu;

import java.util.List;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.NetworkImageView;
import com.example.gspot.R;
import com.example.gspot.User;
import com.example.gspot.onlinePeople.AppController;

public class friendDrawerAdapter extends BaseAdapter {

	private Activity activity;
    private LayoutInflater inflater;
    private List<User> userItems;
    User user;
    int flag;
    int cnt;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    
    public friendDrawerAdapter(Activity activity, List<User> userList) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		 
		Intent i = activity.getIntent();
        user = (User) i.getParcelableExtra("user");
        
        
		 	if (inflater == null)
	            inflater = (LayoutInflater) activity
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        if (convertView == null)
	            convertView = inflater.inflate(R.layout.friend_drawer, null);
	 
	        if (imageLoader == null)
	            imageLoader = AppController.getInstance().getImageLoader();
	        
	        NetworkImageView thumbNail = (NetworkImageView) convertView
	                .findViewById(R.id.thumbnail_friends_drawer);
	        
	        TextView title = (TextView) convertView.findViewById(R.id.title_friends_drawer);
	        
	        final User m = userItems.get(position);
	       
	        // thumbnail image
	        thumbNail.setImageUrl(m.getImageUrl(), imageLoader);
	        
	        // title
	        title.setText(m.getName()+" " +m.getSurname());
	        
	        // rating
	        
	        
	       
	        
	        /*
	        thumbNail.setOnClickListener(new OnClickListener(){
	        	 @Override
	             public void onClick(View v) {
	        		 
	             	Bundle bundle= new Bundle();
	             	bundle.putInt("flag", 4);
	             	
	             	
	             	bundle.putString("friendName",m.getName());
	             	bundle.putString("friendSurname",m.getSurname());
	             	bundle.putString("friendPhoto",m.getImageUrl());          	             	
	             	bundle.putInt("friendID",m.getUserID());
	             	
	             	MyProfileFragment fragment=new MyProfileFragment();
	             	fragment.setArguments(bundle);
	             	
	             	FragmentManager fragmentManager = activity.getFragmentManager();
	             	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	             	fragmentTransaction.replace(R.id.friendsFragment, fragment);
	             	fragmentTransaction.commit();
	                 
	             }
	        	
	        });*/
		return convertView;
	}

}