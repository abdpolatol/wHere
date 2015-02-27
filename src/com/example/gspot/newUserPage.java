package com.example.gspot;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.gspot.nearbyplaces.PlaceClass;
import com.example.gspot.slidingmenu.MyLocationFragment;
import com.example.gspot.slidingmenu.MyProfileFragment;
import com.example.gspot.slidingmenu.NavDrawerItem;
import com.example.gspot.slidingmenu.NavDrawerListAdapter;
import com.example.gspot.slidingmenu.PhotosFragment;

public class newUserPage extends Activity{

	// slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
 // used to store app title
    private CharSequence mTitle;
 // nav drawer title
    private CharSequence mDrawerTitle;
    private ActionBarDrawerToggle mDrawerToggle; 
	Button b,b2,b3,b4;
	Intent i;
	User user;
	PlaceClass place;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);
         i= getIntent();
        user = (User) i.getParcelableExtra("user");
        
        if(user.getCheckInFlag()==1)
        	place=(PlaceClass) i.getParcelableExtra("place");
        mTitle = mDrawerTitle = getTitle();
        
        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
 
        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
 
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setScrimColor(0);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
 
        navDrawerItems = new ArrayList<NavDrawerItem>();
 
        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        
        
        //Checked in place
        if(user.getCheckInFlag()==1)
        navDrawerItems.add(new NavDrawerItem(place.getName(), navMenuIcons.getResourceId(5, -1)));
        
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(4, -1)));
        
        //Logout
        
        // Pages
       // navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // What's hot, We  will add a counter here
       // navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
         
 
        // Recycle the typed array
        navMenuIcons.recycle();
 
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
     * Swaps fragments in the main content view
     */
    

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
        	if(position==3 && user.getCheckInFlag()==1){
        		Intent i= new Intent(newUserPage.this, newPostScreen.class);    	               
                i.putExtra("user",user);
                i.putExtra("place", place);
                startActivity(i);
                finish();
        	}
        	else if(position==3 && user.getCheckInFlag()==0)
        	{	
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
        	else if(position==4 && user.getCheckInFlag()==1)
        	{	
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
    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
        case 0:
            fragment = new PhotosFragment();
            break;
        case 1:
            fragment = new MyProfileFragment();
            break;
        case 2:
            fragment = new MyLocationFragment();
            break;
        case 3:
            
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
  

	

}
