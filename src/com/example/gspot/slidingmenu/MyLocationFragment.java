package com.example.gspot.slidingmenu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.example.gspot.PlaceClass;
import com.example.gspot.R;
import com.example.gspot.User;
import com.example.gspot.newPostScreen;
import com.example.gspot.newUserPage;
import com.example.gspot.placesListView.LazyAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MyLocationFragment extends Fragment implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener {
	private MapView mapView;
	private GoogleMap map;
	private ListView list;
	private User user;
	private LazyAdapter adapter;
    public static final String NAME = "name";
	private LocationClient mLocationClient;
    private Location mCurrentLocation;
    private int counter=0;
    private PlaceClass place;
    public static ArrayList<PlaceClass> nearbyplaces = new ArrayList<PlaceClass>();
    public  ArrayList<String> placeIDs = new ArrayList<String>();
	public static List<String> userCount;
	
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
        View rootView = inflater.inflate(R.layout.userpage, container, false);
        setHasOptionsMenu(true); 
        
        mapView = (MapView) rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        
    	map = mapView.getMap();
    	map.getUiSettings().setMyLocationButtonEnabled(true);
    	map.setMyLocationEnabled(true);
    	MapsInitializer.initialize(this.getActivity());
		
		// Updates the location and zoom of the MapView
		
		
        return rootView;
    }
	
	public void onStart() {
        super.onStart();

     
        Intent i = getActivity().getIntent();
        user = (User) i.getParcelableExtra("user");
        list=(ListView)getView().findViewById(R.id.list);
        final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        //setUpMapIfNeeded();
        
        mLocationClient = new LocationClient(getActivity(), this, this);
        mLocationClient.connect();
        
        
		
        list.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
			if(user.getCheckInFlag()==0){
			adb.setTitle("Confirm Check-in");
			adb.setMessage("Do you want to check-in at "+nearbyplaces.get(position).getName()+" ?");
			adb.setIcon(R.drawable.checkinicon);			
			adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {				
					Intent i= new Intent(getActivity(), newPostScreen.class); 
					user.setCheckInFlag(1);
					i.putExtra("place", nearbyplaces.get(position));
	                i.putExtra("user",user);
	                place=nearbyplaces.get(position);
	                	                
	                checkinFunction();
	                startActivity(i);
	                getActivity().finish();
	
					
				}				
			});
			adb.setNegativeButton("No", null);
			adb.show();
		}
			if(user.getCheckInFlag()==1){
				adb.setTitle("You must first check out");
				adb.setMessage("Do you want to check out and check-in at "+nearbyplaces.get(position).getName()+" ?");
				adb.setIcon(R.drawable.checkinicon);			
				adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {				
						Intent i= new Intent(getActivity(), newPostScreen.class); 
						checkOutFunction();
						user.setCheckInFlag(1);
						i.putExtra("place", nearbyplaces.get(position));
		                i.putExtra("user",user);
		                place=nearbyplaces.get(position);
		                
		                checkinFunction();
		                startActivity(i);
		                getActivity().finish();
		
						
					}				
				});
				adb.setNegativeButton("No", null);
				adb.show();
			}
	
		
		
		
		
	}
	});
    
	
	}
	public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
		
    }


    @Override
	public void onStop() {
        mLocationClient.disconnect();
        super.onStop();
    }
  

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub
    }
    @Override
	public void onResume() {
		mapView.onResume();
		super.onResume();
	}
    @Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}
    @Override
   	public void onDestroy() {
   		super.onDestroy();
   		mapView.onDestroy();
   	}

    @Override
    public void onDisconnected() {
        Toast.makeText(getActivity(), "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(Bundle arg0) {
        Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT).show();
        LocationManager manager = (LocationManager) getActivity()
                .getSystemService(Context.LOCATION_SERVICE);
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                new LocationListener() {
                    @Override
                    public void onStatusChanged(String provider, int status,
                            Bundle extras) {
                    }

                    public void onProviderEnabled(String provider) {
                    }

                    public void onProviderDisabled(String provider) {
                    }

                    public void onLocationChanged(final Location location) {
                    	mCurrentLocation = location;
                    	counter++;
                    	if(counter==1){
                    		if(nearbyplaces.size()!=0){
                    			nearbyplaces.clear();
                    		}// onur's api AIzaSyBg0q_Qi-IbgaVVBCX3MadAt2rFMkwvZWU
                    		String query = "https://maps.googleapis.com/maps/api/place/search/json?radius=500&key=AIzaSyBPz4pH4Hqbodd5vmOvGb2BYpLLN_Ir1uM&location=";
                    		query=query.concat(String.valueOf(mCurrentLocation.getLatitude())+","+String.valueOf(mCurrentLocation.getLongitude()));                    		
                    		new HttpTask().execute(query); 
                    	}
                    	
                    }
                });
        
        mCurrentLocation = mLocationClient.getLastLocation();
        
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 15);
		map.animateCamera(cameraUpdate);
        
    }
    
    public String getWebPage(String adresse) {

	    HttpClient httpClient = new DefaultHttpClient();
	    HttpGet httpGet = new HttpGet();

	    InputStream inputStream = null;

	    String response = null;

	    try {

	        URI uri = new URI(adresse);
	        httpGet.setURI(uri);

	        HttpResponse httpResponse = httpClient.execute(httpGet);
	        int statutCode = httpResponse.getStatusLine().getStatusCode();
	        int length = (int) httpResponse.getEntity().getContentLength();


	        inputStream = httpResponse.getEntity().getContent();
	        Reader reader = new InputStreamReader(inputStream, "UTF-8");

	        int inChar;
	        StringBuffer stringBuffer = new StringBuffer();

	        while ((inChar = reader.read()) != -1) {
	            stringBuffer.append((char) inChar);
	        }

	        response = stringBuffer.toString();

	    } catch (ClientProtocolException e) {
	        Log.e("", "HttpActivity.getPage() ClientProtocolException error", e);
	    } catch (IOException e) {
	        Log.e("", "HttpActivity.getPage() IOException error", e);
	    } catch (URISyntaxException e) {
	        Log.e("", "HttpActivity.getPage() URISyntaxException error", e);
	    } finally {
	        try {
	            if (inputStream != null)
	                inputStream.close();

	        } catch (IOException e) {
	            Log.e("", "HttpActivity.getPage() IOException error lors de la fermeture des flux", e);
	        }
	    }
	    
	    return response;
	}

	private class HttpTask extends AsyncTask<String, Integer, String> {

	    @Override
	    protected String doInBackground(String... urls) {
	        // TODO Auto-generated method stub
	        String response = getWebPage(urls[0]);
	        return response;
	    }

	    @Override
	    protected void onPostExecute(String response) {
	    	parse(response);
	    	
	    }

	    @Override
	    protected void onPreExecute() {
	        // TODO Auto-generated method stub
	        super.onPreExecute();
	    }

	    @Override
	    protected void onProgressUpdate(Integer... values) {
	        // TODO Auto-generated method stub
	        super.onProgressUpdate(values);
	    }
	}
    public void parse(String input){
		String[] lines = input.split(System.getProperty("line.separator"));
		
		int counter = 0;
		nearbyplaces.clear();
		for( int i = 0; i <= lines.length - 1; i++)
		{	
			if(lines[i].toLowerCase().contains("\"location\"")){
				PlaceClass temp = new PlaceClass();
				nearbyplaces.add(temp);					
				}

			if(lines[i].toLowerCase().contains("\"lat\"")){					
				nearbyplaces.get(counter).setLat(lines[i].substring(lines[i].indexOf(": ")+2,lines[i].indexOf(",")));
				}
			if(lines[i].toLowerCase().contains("\"lng\"")){
				nearbyplaces.get(counter).setLon(lines[i].substring(lines[i].indexOf(": ")+2));
				}
			if(lines[i].toLowerCase().contains("\"name\"")){
				nearbyplaces.get(counter).setName(lines[i].substring(lines[i].indexOf(": \"")+3,lines[i].indexOf("\",")));			
				}
			if(lines[i].toLowerCase().contains("\"place_id\"")){
				placeIDs.add(lines[i].substring(lines[i].indexOf(": \"")+3,lines[i].indexOf("\",")));
				nearbyplaces.get(counter).setId(lines[i].substring(lines[i].indexOf(": \"")+3,lines[i].indexOf("\",")));
				counter++;
				}  			
		}
		
		
		 String onlineUsers = userCountFunction(placeIDs,placeIDs.size());
		 String subString =onlineUsers.substring(1, onlineUsers.length()-2);
		 
		 System.out.println("sizeee");
		 System.out.println(nearbyplaces.size());
		 System.out.println(placeIDs.size());
		  userCount= Arrays.asList(subString.split(","));
		
		 
		
		 
        adapter=new LazyAdapter(getActivity(), MyLocationFragment.nearbyplaces,userCount);           
        list.setAdapter(adapter);
        
	}
    public String userCountFunction(ArrayList<String> ids, int size){
    	if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    	String result = "";
    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (int i = 0; i < size; i++) {

        	nameValuePairs.add(new BasicNameValuePair("ids[]",ids.get(i)));

       }
        nameValuePairs.add(new BasicNameValuePair("size",Integer.toString(size)));
        InputStream is = null;
        
        try{
        	HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://www.ceng.metu.edu.tr/~e1818871/usersinplaces.php");
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
   
    @SuppressLint("SimpleDateFormat")
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
        
        httpclient.execute(httppost2);
        
       
        httpclient.execute(httppost);
        
        }catch(Exception e){
        	
            System.out.println("Exception : " + e.getMessage());
        }
    }

    
}