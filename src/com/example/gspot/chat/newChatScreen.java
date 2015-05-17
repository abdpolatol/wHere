package com.example.gspot.chat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpRequest;
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

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
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
import com.example.gspot.Post.PostFragment;
import com.example.gspot.Post.comment;
import com.example.gspot.Post.postAdapter;
import com.example.gspot.onlinePeople.AppController;

public class newChatScreen extends Fragment {
	private chatAdapter adapter;
	private static final String TAG = newChatScreen.class.getSimpleName();
	private List<comment> commentList = new ArrayList<comment>();
	private ListView lv;
	private EditText editText1;
	User user,friend;
	String name,surname,picture;
    int useridBundle;
	private ImageView profileImage;
	Bitmap bitmap;
	private ProgressDialog pDialog;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {  
	        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
	        return rootView;
	    }
	 public void onStart() {
	        super.onStart();
	        
			Intent i = getActivity().getIntent();
	        user = (User) i.getParcelableExtra("user");
	       // place = (PlaceClass) i.getParcelableExtra("place");
	        Bundle bundle= this.getArguments();
	       
	        name=bundle.getString("name");
	        surname=bundle.getString("surname");
	        useridBundle=bundle.getInt("id");
	        picture=bundle.getString("picture");
	        
			lv = (ListView) getView().findViewById(R.id.listViewchat);
			editText1 = (EditText) getView().findViewById(R.id.editTextchat);
			
			adapter = new chatAdapter(getActivity(), commentList);
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
				        HttpPost httppost = new HttpPost("http://www.ceng.metu.edu.tr/~e1818871/sendMessage.php?id="+user.getUserID()+"&id2="+useridBundle+"&msg="+editText1.getText().toString());
				        HttpPost httppost2 = new HttpPost("http://www.ceng.metu.edu.tr/~e1818871/processmessage.php?mode=3&name="+user.getName()+"&surname="+user.getSurname()+"&id2="+useridBundle); 
				        try {
							httpclient.execute(httppost);
							httpclient.execute(httppost2);
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
			
			String url = "http://www.ceng.metu.edu.tr/~e1818871/getMessage.php?id="+user.getUserID()+"&id2="+useridBundle;
	        // Creating volley request obj
			String response="";
			 ResponseHandler<String> responsehandler = new BasicResponseHandler();
			HttpClient httpclient=new DefaultHttpClient();
	        HttpPost httppost= new HttpPost("http://www.ceng.metu.edu.tr/~e1818871/getMessage.php?id="+user.getUserID()+"&id2="+useridBundle);
	        
	        try {
	        	 response=httpclient.execute(httppost,responsehandler);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        String[] response2 = response.split(">");
	        JSONArray jarray = null;
			try {
				jarray = new JSONArray(response2[1]);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	       
	        for (int j = 0; j <jarray.length(); j++) {
                try {

                    JSONObject obj =jarray.getJSONObject(j);
                    
                    String message=obj.getString("message");
                    String date=obj.getString("time");
                    int userid=Integer.parseInt(obj.getString("id"));
                    	
               		if(userid==user.getUserID())
               			commentList.add(new comment(false,message,date,userid));
               		else
               			commentList.add(new comment(true,message,date,userid));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
	        
			
	 }
	 @SuppressWarnings("unused")
	private void hidePDialog() {
	        if (pDialog != null) {
	            pDialog.dismiss();
	            pDialog = null;
	        }
	    }
		
	

}