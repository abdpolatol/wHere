package com.example.gspot.onlinePeople;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.gspot.R;
import com.example.gspot.User;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class onlinePeopleAdapter  extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<User> userItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
 
    public onlinePeopleAdapter(Activity activity, List<User> movieItems) {
        this.activity = activity;
        this.userItems = movieItems;
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
         
        // genre
        //genre.setText(m.getCity());
         
        // release year
       // year.setText(String.valueOf(m.get));
 
        return convertView;
    }
 
}