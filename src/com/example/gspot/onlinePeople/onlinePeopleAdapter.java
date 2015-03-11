package com.example.gspot.onlinePeople;
import java.io.IOException;
import java.util.List;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.gspot.R;
import com.example.gspot.User;
import com.example.gspot.slidingmenu.MyProfileFragment;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class onlinePeopleAdapter  extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<User> userItems;
    Fragment fragment=null;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    User user;
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
    	//View vi =convertView;
    	final User m = userItems.get(position);
    	Intent i = activity.getIntent();
        user = (User) i.getParcelableExtra("user");
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
            	String response="";
            	HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://www.ceng.metu.edu.tr/~e1818871/friends/isFriend.php?userID="+user.getUserID()+"&friendID="+m.getUserID());
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                try {
					response = httpclient.execute(httppost, responseHandler);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                int flag=Integer.parseInt(response);
               
            	Bundle bundle= new Bundle();
            	bundle.putInt("flag", flag);
            	bundle.putString("friendName",m.getName());
            	bundle.putString("friendSurname",m.getSurname());
            	bundle.putString("friendPhoto",m.getImageUrl());          
            	bundle.putInt("friendID",m.getUserID());
            	
            	MyProfileFragment fragment=new MyProfileFragment();
            	fragment.setArguments(bundle);
            	
            	FragmentManager fragmentManager = activity.getFragmentManager();
            	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            	fragmentTransaction.replace(R.id.onlinePeopleFragment, fragment);
            	fragmentTransaction.commit();
                
            }
        });
        
        
        TextView title = (TextView) convertView.findViewById(R.id.title2);
        TextView rating = (TextView) convertView.findViewById(R.id.rating2);
        //TextView genre = (TextView) convertView.findViewById(R.id.genre2);
        //TextView year = (TextView) convertView.findViewById(R.id.releaseYear2);
 
        // getting movie data for the row
        
 
        // thumbnail image
        thumbNail.setImageUrl(m.getImageUrl(), imageLoader);
        
        // title
        title.setText(m.getName()+" " +m.getSurname());
         
        // rating
        rating.setText("Online ");
         
        
 
        return convertView;
    }
 
}