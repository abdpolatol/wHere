package com.example.gspot;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.gspot.Post.PostFragment;
import com.example.gspot.onlinePeople.onlinePeopleFragment;
import com.example.gspot.slidingmenu.NavDrawerItem;
import com.example.gspot.slidingmenu.NavDrawerListAdapter;
import com.example.gspot.slidingmenu.postHomeFragment;
public class newPostScreen extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private User user;
    private PlaceClass place;
 
    // nav drawer title
    private CharSequence mDrawerTitle;
 
    // used to store app title
    private CharSequence mTitle;
 
    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
 
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_page);
        Intent i = getIntent();
        user = (User) i.getParcelableExtra("user");
        place = (PlaceClass) i.getParcelableExtra("place");       
        
        mTitle = mDrawerTitle = getTitle();
        
        
        
        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items_post);
 
        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
 
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout2);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu2);
 
        navDrawerItems = new ArrayList<NavDrawerItem>();
        String numberOfUsers=number_of_online_users();
       
        // Current Place
        navDrawerItems.add(new NavDrawerItem(place.getName(), navMenuIcons.getResourceId(0, -1)));
        // Posts 
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Online People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1), true, numberOfUsers));
        // Checkout, Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
     // Home, Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(4, -1)));
 
        // Recycle the typed array
        navMenuIcons.recycle();
     
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
 
        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);
 
        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
 
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
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
            // on first time display view for first nav item
            displayView(0);
        }
    }
 
    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
        	final AlertDialog.Builder adb = new AlertDialog.Builder(newPostScreen.this);
            // display view for selected nav drawer item
        	if(position==3){
        		adb.setTitle("Confirm Check-out");
    			adb.setMessage("Do you want to check-out from "+ place.getName()+" ?");
    			adb.setIcon(R.drawable.checkinicon);			
    			adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
    				public void onClick(DialogInterface dialog, int which) {
    					checkOutFunction();
    					Intent i= new Intent(newPostScreen.this, newUserPage.class);    	               
    	                i.putExtra("user",user);
    	                startActivity(i);
    	                finish();
    					
    				}				
    			});
    			adb.setNegativeButton("No", null);
    			adb.show();
        		
        		
        	}
        	if(position==4){
        		Intent i= new Intent(newPostScreen.this, newUserPage.class);    	               
                i.putExtra("user",user);
                i.putExtra("place", place);
                startActivity(i);
                finish();
        		
        	}
            displayView(position);
        	
        }
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
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
 
    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        android.app.Fragment fragment = null;
        switch (position) {
        case 0:
            fragment = new postHomeFragment();
            break;
        case 1:
            fragment = new PostFragment();
            break;
        case 2:
            fragment = new onlinePeopleFragment();
            break;
        case 3:
            break;
        case 4:
            
            break;
        case 5:
          //  fragment = new WhatsHotFragment();
            break;
 
        default:
            break;
        }
 
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container2, fragment).commit();
 
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
    public void checkOutFunction(){
    	user.setCheckInFlag(0);
		HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://www.ceng.metu.edu.tr/~e1818871/checkout.php");
        
            
        List<NameValuePair> nameValuePairs_checkOut = new ArrayList<NameValuePair>(1);
        
 
        nameValuePairs_checkOut.add(new BasicNameValuePair("userID",Integer.toString(user.getUserID()).trim()) );  
       

        try{ 
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs_checkOut));
        httpclient.execute(httppost);
        
        }catch(Exception e){
            System.out.println("Exception : " + e.getMessage());
        }
    }
    @Override
    public void onBackPressed() {
    	Intent i= new Intent(newPostScreen.this, newUserPage.class);    	               
        i.putExtra("user",user);
        i.putExtra("place", place);
        startActivity(i);
        finish();
       
    }
    
    public String number_of_online_users() {
    	if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    	String response= "" ;
    	
    	
    	try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://www.ceng.metu.edu.tr/~e1818871/numberofusers.php?placeID="+place.getId());
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            response = httpclient.execute(httppost, responseHandler);
           
            
    }catch(Exception e){
            Log.e("log_tag", "Error in http connection "+e.toString());
    }
  
    return response;
    }
    
 
}
    