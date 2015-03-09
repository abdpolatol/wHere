package com.example.gspot.Post;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.gspot.PlaceClass;
import com.example.gspot.R;
import com.example.gspot.User;
import com.example.gspot.onlinePeople.AppController;

public class PostFragment extends Fragment {
	private com.example.gspot.Post.postAdapter adapter;
	private static final String TAG = PostFragment.class.getSimpleName();
	private List<comment> commentList = new ArrayList<comment>();
	private ListView lv;
	private EditText editText1;
	User user;
	PlaceClass place;
	private ImageView profileImage;
	Bitmap bitmap;
	private ProgressDialog pDialog;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {  
	        View rootView = inflater.inflate(R.layout.activity_discuss, container, false);
	        return rootView;
	    }
	 public void onStart() {
	        super.onStart();

			Intent i = getActivity().getIntent();
	        user = (User) i.getParcelableExtra("user");
	        place = (PlaceClass) i.getParcelableExtra("place");
	      
			lv = (ListView) getView().findViewById(R.id.listView1);
			editText1 = (EditText) getView().findViewById(R.id.editText1);
			
			adapter = new postAdapter(getActivity(), commentList);
			lv.setAdapter(adapter);
			commentList.clear();
			

			
			editText1.setOnKeyListener(new OnKeyListener() {
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					// If the event is a key-down event on the "enter" button
					if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
						// Perform action on key press
						
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				        Calendar cal = Calendar.getInstance();
						
						HttpClient httpclient=new DefaultHttpClient();
				        HttpPost httppost= new HttpPost("http://www.ceng.metu.edu.tr/~e1818871/sendPost.php");
				        
				        List<NameValuePair> nameValuePairs;					        
				        nameValuePairs = new ArrayList<NameValuePair>(4);				        
				        nameValuePairs.add(new BasicNameValuePair("userID",Integer.toString(user.getUserID())));
				        nameValuePairs.add(new BasicNameValuePair("placeID",place.getId().trim()));
				        nameValuePairs.add(new BasicNameValuePair("post_date",dateFormat.format(cal.getTime()).trim()));
				        nameValuePairs.add(new BasicNameValuePair("post",editText1.getText().toString().trim())); 
				               
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
				        commentList.add(new comment(false, editText1.getText().toString(),dateFormat.format(cal.getTime()).trim(),user.getName(),user.getImageUrl(),user.getUserID()));
						editText1.setText("");
						return true;
					}
					return false;
				}
			});
			
			String url = "http://www.ceng.metu.edu.tr/~e1818871/wall.php?placeID="+place.getId();
	        // Creating volley request obj
	        JsonArrayRequest PostReq = new JsonArrayRequest(url,
	                new Response.Listener<JSONArray>() {
	                    @Override
	                    public void onResponse(JSONArray response) {
	                        Log.d(TAG, response.toString());
	                        hidePDialog();
	                        System.out.println("haldun2");
	 
	                        // Parsing json
	                        for (int i = 0; i < response.length(); i++) {
	                            try {
	 
	                                JSONObject obj = response.getJSONObject(i);
	                                String name=obj.getString("name");
	        	                    String imageUrl=obj.getString("profile_pic");
	        	                    String post=obj.getString("post");
	        	                    String date=obj.getString("post_date");
	        	                    int userid=Integer.parseInt(obj.getString("userID"));

	        	               		if(userid==user.getUserID())
	        	               			commentList.add(new comment(false,post,date,name,imageUrl,userid));
	        	               		else
	        	               			commentList.add(new comment(true,post,date,name,imageUrl,userid));
	 
	                            } catch (JSONException e) {
	                                e.printStackTrace();
	                            }
	 
	                        }
	 
	                        // notifying list adapter about data changes
	                        // so that it renders the list view with updated data
	                        
	                        adapter.notifyDataSetChanged();
	                    }
	                }, new Response.ErrorListener() {
	                    @Override
	                    public void onErrorResponse(VolleyError error) {
	                        VolleyLog.d(TAG, "Error: " + error.getMessage());
	                        hidePDialog();
	 
	                    }
	                });
	 
	        // Adding request to request queue
	        AppController.getInstance().addToRequestQueue(PostReq);
			//addItems();
			//adapter.notifyDataSetChanged();
			
	 }/*
	 private void addItems() {
		 	String result = "";
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
	    	try{
	            JSONArray jArray = new JSONArray(result);
	            
	            int textViewCount = jArray.length();
	            
	            
	            for(int i=0;i<jArray.length();i++){
	            	

	        			
	                    JSONObject json_data = jArray.getJSONObject(i);
	                    String name=json_data.getString("name");
	                    String imageUrl=json_data.getString("profile_pic");
	                    String post=json_data.getString("post");
	                    String date=json_data.getString("post_date");
	                    int userid=Integer.parseInt(json_data.getString("userID"));
	                   
	                    
	                   
	               		if(userid==user.getUserID())
	               			commentList.add(new comment(false,post,date,name,imageUrl));
	               		else
	               			commentList.add(new comment(true,post,date,name,imageUrl));
	            }
	    	}
	    	catch(JSONException e){
	            Log.e("log_tag", "Error parsing data "+e.toString());
	    	}
			
		}*/
	 private void hidePDialog() {
	        if (pDialog != null) {
	            pDialog.dismiss();
	            pDialog = null;
	        }
	    }
		
	

}
