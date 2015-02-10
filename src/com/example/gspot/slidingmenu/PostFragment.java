package com.example.gspot.slidingmenu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.gspot.ItemListBaseAdapter;
import com.example.gspot.R;
import com.example.gspot.User;
import com.example.gspot.nearbyplaces.PlaceClass;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
 
public class PostFragment extends Fragment {
	  TextView tv2;
	  User user;
	  PlaceClass place;
	  Button b;
	  
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.deneme22, container, false);
        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();

        Intent i = getActivity().getIntent();
        user = (User) i.getParcelableExtra("user");
        place = (PlaceClass) i.getParcelableExtra("place");
        
        tv2 = (TextView)getView().findViewById(R.id.postText);
        b = (Button)getView().findViewById(R.id.postButton);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	send_Post();
            	tv2.setText("");
            }
        });
        checkinFunction();
        
        //Log.e("Name: ",user.getUser_name());
        first_do_that();
     /*   Thread t = new Thread() {

        	  @Override
        	  public void run() {
        	    try {
        	      while (!isInterrupted()) {
        	        Thread.sleep(1000);
        	        runOnUiThread(new Runnable() {
        	          @Override
        	          public void run() {
        	        	  first_do_that();
        	          }
        	        });
        	      }
        	    } catch (InterruptedException e) {
        	    }
        	  }
        	};

        	t.start();*/
        
        
    }
public String get_wall_posts()  {
    	
    	
    	String result = "";
    	//the year data to send
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    	nameValuePairs.add(new BasicNameValuePair("placeID",place.getId()));
        
    	InputStream is = null;
    	
    	try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://www.ceng.metu.edu.tr/~e1818871/wall.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
    }catch(Exception e){
            Log.e("log_tag", "Error in http connection "+e.toString());
    }
    //convert response to string
    try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-9"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
            }
            is.close();
     
            result=sb.toString();
    }catch(Exception e){
            Log.e("log_tag", "Error converting result "+e.toString());
    }
    return result;
    }

    public void first_do_that(){
    	
    	String result= get_wall_posts();
    	String asd=""; 
    	//Log.e("MyTag",result);
		ArrayList<User> results = new ArrayList<User>();

        try{
            JSONArray jArray = new JSONArray(result);
            
            int textViewCount = jArray.length();
            
            
            for(int i=0;i<jArray.length();i++){
            	

        			User user = new User();
                    JSONObject json_data = jArray.getJSONObject(i);
                    String name=json_data.getString("name");
                    String imageUrl=json_data.getString("profile_pic");
                    String post=json_data.getString("post");
                    String date=json_data.getString("post_date");
                    
                    asd= asd + name + " :" + post + "\n" ;
                   
                    user.setName(name);
               		user.setItemDescription(post);
               		user.setPrice(date);              		
               		user.setImageNumber(1);
               		user.setImageUrl(imageUrl);
               		results.add(user);
            }
    }
    catch(JSONException e){
            Log.e("log_tag", "Error parsing data "+e.toString());
    }

        final ListView lv1 = (ListView)getView().findViewById(R.id.listV_main);
        lv1.setAdapter(new ItemListBaseAdapter(getActivity(), results));
    }

    void send_Post(){
    	HttpClient httpclient=new DefaultHttpClient();
        HttpPost httppost= new HttpPost("http://www.ceng.metu.edu.tr/~e1818871/sendPost.php");
        List<NameValuePair> nameValuePairs;
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        
        nameValuePairs = new ArrayList<NameValuePair>(4);
        
        nameValuePairs.add(new BasicNameValuePair("userID",Integer.toString(user.getUserID()).trim()));
        nameValuePairs.add(new BasicNameValuePair("placeID",place.getId().trim()));
        nameValuePairs.add(new BasicNameValuePair("post_date",dateFormat.format(cal.getTime()).trim()));
        nameValuePairs.add(new BasicNameValuePair("post",tv2.getText().toString().trim())); 
               
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try {
        	httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			final String response = httpclient.execute(httppost, responseHandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    public void checkinFunction(){

		HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://www.ceng.metu.edu.tr/~e1818871/checkin.php");
        HttpPost httppost2 = new HttpPost("http://www.ceng.metu.edu.tr/~e1818871/add_place.php");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            List<NameValuePair> nameValuePairs_checkin = new ArrayList<NameValuePair>(3);
            List<NameValuePair> nameValuePairs_addplace = new ArrayList<NameValuePair>(4);
 
            nameValuePairs_checkin.add(new BasicNameValuePair("userID",Integer.toString(user.getUserID()).trim()) );  
        nameValuePairs_checkin.add(new BasicNameValuePair("placeID", place.getId().trim()) );
        nameValuePairs_checkin.add(new BasicNameValuePair("date",dateFormat.format(cal.getTime()).trim()) );
        nameValuePairs_addplace.add(new BasicNameValuePair("placeID",place.getId().trim()) );
        nameValuePairs_addplace.add(new BasicNameValuePair("placename",place.getName().trim()) );
        nameValuePairs_addplace.add(new BasicNameValuePair("placelat",place.getLat().trim()) );
        nameValuePairs_addplace.add(new BasicNameValuePair("placelon",place.getLon().trim()) );

        try{ 
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs_checkin));
        httppost2.setEntity(new UrlEncodedFormEntity(nameValuePairs_addplace));
        httpclient.execute(httppost);
        httpclient.execute(httppost2);
        }catch(Exception e){
            System.out.println("Exception : " + e.getMessage());
        }
    }
    
}
 