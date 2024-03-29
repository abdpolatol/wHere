package com.example.gspot;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.gspot.chat.newChatScreen;
import com.example.gspot.friends.FriendsAdapter;
import com.example.gspot.friends.FriendsFragment;
import com.example.gspot.onlinePeople.AppController;
import com.example.gspot.settings.SettingsFragment;
import com.example.gspot.slidingmenu.MyLocationFragment;
import com.example.gspot.slidingmenu.MyProfileFragment;
import com.example.gspot.slidingmenu.NavDrawerItem;
import com.example.gspot.slidingmenu.NavDrawerListAdapter;
import com.example.gspot.slidingmenu.PhotosFragment;
import com.example.gspot.slidingmenu.friendDrawerAdapter;

public class newUserPage extends Activity{

	// slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ImageView imageView;
    private ArrayList<NavDrawerItem> navDrawerItems,navDrawerItemsfriend;
    private NavDrawerListAdapter adapter;
    private friendDrawerAdapter adpt;
    private List<User> userList = new ArrayList<User>();
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList,mDrawerListfriend;
 // used to store app title
    private CharSequence mTitle;
 // nav drawer title
    private CharSequence mDrawerTitle;
    private ActionBarDrawerToggle mDrawerToggle; 
	Button b,b2,b3,b4;
	Intent i;
	User user;
	PlaceClass place;
	int f=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);
         i= getIntent();
         final Activity activity=this;
        user = (User) i.getParcelableExtra("user");
        System.out.println("haldun");
        System.out.println(user.getRange());
        
        if(user.getCheckInFlag()==1)
        	place=(PlaceClass) i.getParcelableExtra("place");
        mTitle = mDrawerTitle = getTitle();
        
        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
 
        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
 
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //mDrawerLayout.setScrimColor(0);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        mDrawerListfriend= (ListView) findViewById(R.id.list_slidermenufriend);
        navDrawerItems = new ArrayList<NavDrawerItem>();
        navDrawerItemsfriend = new ArrayList<NavDrawerItem>();
        String url = "http://www.ceng.metu.edu.tr/~e1818871/friends/show_friends.php?userID="+Integer.toString(user.getUserID());
        // Creating volley request obj
        JsonArrayRequest movieReq1 = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        
                        
 
                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                            	
                                JSONObject obj = response.getJSONObject(i);
                                User temp_user = new User();
                               
                                temp_user.setName(obj.getString("name"));
                                temp_user.setSurname(obj.getString("surname"));
                                temp_user.setImageUrl(obj.getString("profile_pic"));
                                temp_user.setUserID(Integer.parseInt(obj.getString("userID")));
                                if(Integer.parseInt(obj.getString("status"))==0)
                                	f++;
                                userList.add(temp_user);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }                            
                        }
                        
                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adpt = new friendDrawerAdapter(activity, userList);
                        mDrawerListfriend.setAdapter(adpt);
                        adpt.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       
 
                    }
                });
 
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq1);
        
        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
     // Friends
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(5, -1)));
        
        //Checked in place
        if(user.getCheckInFlag()==1)
        navDrawerItems.add(new NavDrawerItem(place.getName(), navMenuIcons.getResourceId(6, -1)));
        
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(4, -1)));
        
 
        // Recycle the typed array
        navMenuIcons.recycle();
 
        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);
 
        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.logo, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ){
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }
 
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
 
       if (savedInstanceState == null) {
    	   	if(user.getFlag()==1){
    	   		user.setFlag(0);
    	   		displayView(1);
    	   	}
    	   	else if (user.getFlag()==2){
    	   		user.setFlag(0);
    	   		 displayView(2);
    	   	}
    	   	else
    	   		displayView(0);
        }
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        mDrawerListfriend.setOnItemClickListener(new SlideMenuClickListener2());

    }
   
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
 
    
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
        case R.id.action_settings:
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = (mDrawerLayout.isDrawerOpen(mDrawerList) || mDrawerLayout.isDrawerOpen(mDrawerListfriend));
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        if( mDrawerLayout.isDrawerOpen(mDrawerListfriend)){
        	getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(false);
        }
        if(!mDrawerLayout.isDrawerOpen(mDrawerListfriend)){
        	getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        	
        return super.onPrepareOptionsMenu(menu);
    }
    

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
    	final AlertDialog.Builder adb = new AlertDialog.Builder(newUserPage.this);
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            // display view for selected nav drawer item
        	if(position==4 && user.getCheckInFlag()==1){
        		if(imageView!=null)
        		imageView.setImageBitmap(null);
        		Intent i= new Intent(newUserPage.this, newPostScreen.class);    	               
                i.putExtra("user",user);
                i.putExtra("place", place);
                startActivity(i);
                finish();
        	}
        	else if(position==4 && user.getCheckInFlag()==0)
        	{	if(imageView!=null)
        		imageView.setImageBitmap(null);
        		adb.setTitle("Confirm Logout");
    			adb.setMessage("Do you want to logout ?");
    			adb.setIcon(R.drawable.ic_logout);			
    			adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
    				public void onClick(DialogInterface dialog, int which) {				
    					MainFragment.loginPrefsEditor.clear();
    	        		MainFragment.loginPrefsEditor.commit();
    	        		Intent i= new Intent(newUserPage.this, MainActivity.class); 
    	        		startActivity(i);
    	                finish();
    					
    				}				
    			});
    			adb.setNegativeButton("No", null);
    			adb.show();
        		
        		
        		
        	}
        	else if(position==5 && user.getCheckInFlag()==1)
        	{	if(imageView!=null)
        		imageView.setImageBitmap(null);
        		adb.setTitle("Confirm Logout");
    			adb.setMessage("Do you want to logout ?");
    			adb.setIcon(R.drawable.ic_logout);			
    			adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
    				public void onClick(DialogInterface dialog, int which) {				
    					MainFragment.loginPrefsEditor.clear();
    	        		MainFragment.loginPrefsEditor.commit();
    	        		Intent i= new Intent(newUserPage.this, MainActivity.class); 
    	        		startActivity(i);
    	                finish();
    	                
    					
    				}				
    			});
    			adb.setNegativeButton("No", null);
    			adb.show();
        		
        	}
        	else{
        		
        		displayView(position);
        	}
            
        }
    }
    private class SlideMenuClickListener2 implements
	    ListView.OnItemClickListener {
			final AlertDialog.Builder adb = new AlertDialog.Builder(newUserPage.this);
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
			        long id) {
			    // display view for selected nav drawer item
				Fragment fragment = null;
				userList.get(position);
				Bundle bundle= new Bundle();
				bundle.putString("name", userList.get(position).getName());
				bundle.putString("surname", userList.get(position).getSurname());
				bundle.putInt("id", userList.get(position).getUserID());
				bundle.putString("picture", userList.get(position).getImageUrl());
				fragment = new newChatScreen();
			    fragment.setArguments(bundle);
			    mTitle=userList.get(position).getName()+" "+userList.get(position).getSurname();
			    
			    ActionBar actionBar = getActionBar();
			    actionBar.setDisplayOptions(actionBar.getDisplayOptions()
			            | ActionBar.DISPLAY_SHOW_CUSTOM);
			    imageView = new ImageView(actionBar.getThemedContext());
			    Bitmap actionbar = getBitmapFromURL(userList.get(position).getImageUrl());
			    imageView.setScaleType(ImageView.ScaleType.CENTER);
			    imageView.setImageBitmap(Bitmap.createScaledBitmap(actionbar, 100, 100, false));
			    ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
			            ActionBar.LayoutParams.WRAP_CONTENT,
			            ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
			                    | Gravity.CENTER_VERTICAL);
			    //layoutParams.rightMargin = 40;
			    imageView.setLayoutParams(layoutParams);
			    actionBar.setCustomView(imageView);
			    
			    
			    
			    
			    
			    //getActionBar().setTitle(userList.get(position).getName()+" "+userList.get(position).getSurname());
			    android.app.FragmentManager fragmentManager = getFragmentManager();
	            fragmentManager.beginTransaction()
	                    .replace(R.id.frame_container, fragment).commit();
	            
	         // update selected item and title, then close the drawer
	            mDrawerListfriend.setItemChecked(position, true);
	            mDrawerListfriend.setSelection(position);
	            mDrawerLayout.closeDrawer(mDrawerListfriend); 
		    
			}
	    }
    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
        case 0:
        	if(imageView!=null)
        	imageView.setImageBitmap(null);
            fragment = new PhotosFragment();
            break;
        case 1:
        	if(imageView!=null)
        	imageView.setImageBitmap(null);
        	Bundle bundle= new Bundle();
        	bundle.putInt("flag", 0);
            fragment = new MyProfileFragment();
            fragment.setArguments(bundle);
            break;
        case 2:
        	if(imageView!=null)
        	imageView.setImageBitmap(null);
            fragment = new MyLocationFragment();
            break;
        case 3:
        	if(imageView!=null)
        	imageView.setImageBitmap(null);
        	fragment = new FriendsFragment();
            break;
        case 4:
            //fragment = new PagesFragment();
        	//add here
        	
        
            break;
        case 5:
        	//fragment = new PhotosFragment();
            //fragment = new WhatsHotFragment();
            break;
 
        default:
            break;
        }
 
        if (fragment != null) {
        	
            android.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();
 
            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);         
            
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
    
    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
 
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
 
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
    @Override
    public void onBackPressed() {
    	final AlertDialog.Builder adb = new AlertDialog.Builder(this);
    	adb.setTitle("Confirm Exit");
		adb.setMessage("Do you want to exit ?");
		//adb.setIcon(R.drawable.checkinicon);			
		adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {				
				finish();				
			}				
		});
		adb.setNegativeButton("No", null);
		adb.show();
    	
    }

}

