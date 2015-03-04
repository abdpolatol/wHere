package com.example.gspot.friends;

import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.gspot.R;
import com.example.gspot.User;
import com.example.gspot.onlinePeople.AppController;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FriendsAdapter extends BaseAdapter {

	private Activity activity;
    private LayoutInflater inflater;
    private List<User> userItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    
    public FriendsAdapter(Activity activity, List<User> userList) {
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
	            convertView = inflater.inflate(R.layout.row_friends, null);
	 
	        if (imageLoader == null)
	            imageLoader = AppController.getInstance().getImageLoader();
	        NetworkImageView thumbNail = (NetworkImageView) convertView
	                .findViewById(R.id.thumbnail_friends);
	        TextView title = (TextView) convertView.findViewById(R.id.title_friends);
	        TextView rating = (TextView) convertView.findViewById(R.id.rating_friends);
	        TextView genre = (TextView) convertView.findViewById(R.id.genre_friends);
	        TextView year = (TextView) convertView.findViewById(R.id.releaseYear_friends);
	 
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
