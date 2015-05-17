package com.example.gspot;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.example.gspot.gcm.ApplicationConstants;

public class MainFragment extends Fragment{
	private static final String TAG = "MainFragment";
	private UiLifecycleHelper uiHelper;
	Button b;
	TextView signUp;
	Intent i;
	EditText et,pass;
	HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;
    public static SharedPreferences loginPreferences;
    public static SharedPreferences.Editor loginPrefsEditor;
    private String uname,upass;
    private boolean saveLogin;
    String regID="";
    GoogleCloudMessaging gcmObj;
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, 
	        Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.activity_main, container, false);
	    // async task yapýlcak
	    StrictMode.ThreadPolicy policy = new
	    		StrictMode.ThreadPolicy.Builder()
	    		.permitAll().build();
	    		StrictMode.setThreadPolicy(policy);
	    LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
	    authButton.setFragment(this);
	    b= (Button) view.findViewById(R.id.loginbutton);
	    et = (EditText)view.findViewById(R.id.usernameMain);
	    et.setHint("Username");
	    et.requestFocus();
	   
        pass= (EditText)view.findViewById(R.id.passwordMain);
        pass.setHint("Password");
        
        loginPreferences = getActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        uname = loginPreferences.getString("username", "");
        upass = loginPreferences.getString("password", "");
        
        if (saveLogin == true) {
        	login();   
        }
        
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(getActivity(), "", 
                        "Validating user...", true);
                 new Thread(new Runnable() {
                        @Override
						public void run() {
                            login();                          
                        }
                      }).start();               
            }
        });
        signUp = (TextView)view.findViewById(R.id.signUp);
	    signUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	 
            	 i = new Intent(getActivity().getApplicationContext(),RegisterPage.class);
            	 startActivity(i);

            }
        });
	    return view;
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	        Log.i(TAG, "Logged in...");
	    } else if (state.isClosed()) {
	        Log.i(TAG, "Logged out...");
	    }
	}
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	    uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);
	}
	@Override
	public void onResume() {
	    super.onResume();
	    // For scenarios where the main activity is launched and user
	    // session is not null, the session state change notification
	    // may not be triggered. Trigger it if it's open/closed.
	    Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }
	    uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	private void registerInBackground(final String emailID) {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcmObj == null) {
						gcmObj = GoogleCloudMessaging.getInstance(getActivity().getApplicationContext());
					}
					regID = gcmObj
							.register(ApplicationConstants.GOOGLE_PROJ_ID);
					msg = "Registration ID :" + regID;
					
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				httpclient=new DefaultHttpClient();
                httppost= new HttpPost("http://www.ceng.metu.edu.tr/~e1818871/gcm_register.php?regID="+regID+"&userID="+emailID);
                try {
					httpclient.execute(httppost);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.execute(null, null, null);
	}
	void login(){
        try{            
        	if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            } 
            httpclient=new DefaultHttpClient();
            httppost= new HttpPost("http://www.ceng.metu.edu.tr/~e1818871/check.php"); 
            nameValuePairs = new ArrayList<NameValuePair>(2);
            if(saveLogin==false){
            	nameValuePairs.add(new BasicNameValuePair("username",et.getText().toString().trim())); 
            	nameValuePairs.add(new BasicNameValuePair("password",pass.getText().toString().trim()));
            	uname=et.getText().toString().trim();
            }
            else{
            	nameValuePairs.add(new BasicNameValuePair("username",uname.trim())); 
                nameValuePairs.add(new BasicNameValuePair("password",upass.trim()));
            }
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpclient.execute(httppost, responseHandler);
           
             
            if(response.equalsIgnoreCase("User Found")){
            	registerInBackground(uname.trim());            	
            	InputStream is = null;
            	httpclient=new DefaultHttpClient();
                httppost= new HttpPost("http://www.ceng.metu.edu.tr/~e1818871/session.php");
                nameValuePairs = new ArrayList<NameValuePair>(1);
                if(saveLogin == false){
                	nameValuePairs.add(new BasicNameValuePair("username",et.getText().toString().trim()));
                }
                else{
                	nameValuePairs.add(new BasicNameValuePair("username",uname.trim()));
                }
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                
                HttpResponse sessionResponse = httpclient.execute(httppost);
               
                HttpEntity entity = sessionResponse.getEntity();
                is = entity.getContent();
                
                String name="";
                String surname="";
                String username="";
                String bdate="";
                String city="";
                String imageUrl="";
                int range=100;
                int age=0;
                int userID;
                String result = null; 
                try{
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
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
                
                JSONArray jArray = new JSONArray(result);
                JSONObject json_data = jArray.getJSONObject(0);
                name=json_data.getString("name");
                surname =json_data.getString("surname");
                username =json_data.getString("username");
                userID = json_data.getInt("userID");
                bdate = json_data.getString("birthday");
                city = json_data.getString("city");
                String[] dateinfo = bdate.split("-");
                imageUrl = json_data.getString("profile_pic");
                range = json_data.getInt("range_attr");
                age = getAge( Integer.parseInt(dateinfo[0]), Integer.parseInt(dateinfo[1]), Integer.parseInt(dateinfo[2]));
                User user = new User();
                user.setName(name);
                user.setSurname(surname);
                user.setUser_name(username);
                user.setUserID(userID);
                user.setAge(age);
                user.setCity(city);
                user.setImageUrl(imageUrl);
                user.setCheckInFlag(0);
                user.setRange(range);
              
                getActivity().runOnUiThread(new Runnable() {
                    @Override
					public void run() {
                        Toast.makeText(getActivity(),"Login Success", Toast.LENGTH_SHORT).show();
                    }
                });
               
                //dialog.dismiss();
                // keep logged in*****
                if(saveLogin==false){
                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.putString("username", username);
                loginPrefsEditor.putString("password", pass.getText().toString().trim());
                loginPrefsEditor.commit();
                }
                //***************
               
                Intent i= new Intent(getActivity(), newUserPage.class);
                System.out.println("denizzz");
                System.out.println(user.getRange());
                i.putExtra("user", user);
                startActivity(i);
                getActivity().finish();
                
            }else{
            	dialog.dismiss();
                showAlert();                
            }
             
        }catch(Exception e){
            dialog.dismiss();
            System.out.println("Exception : " + e.getMessage());
        }
    }
	public int getAge (int _year, int _month, int _day) {

        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, a;         

        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(_year, _month, _day);
        a = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH))
                        || ((m == cal.get(Calendar.MONTH)) && (d < cal
                                        .get(Calendar.DAY_OF_MONTH)))) {
                --a;
        }
        if(a < 0)
                throw new IllegalArgumentException("Age < 0");
        return a;
    }
	public void showAlert(){
		getActivity().runOnUiThread(new Runnable() {
            @Override
			public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Login Error.");
                builder.setMessage("User not Found.")  
                       .setCancelable(false)
                       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                           @Override
						public void onClick(DialogInterface dialog, int id) {
                           }
                       });                     
                AlertDialog alert = builder.create();
                alert.show();               
            }
        });
    }
}
