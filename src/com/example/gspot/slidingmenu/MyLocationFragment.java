package com.example.gspot.slidingmenu;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.gspot.R;
import com.example.gspot.User;
import com.example.gspot.newPostScreen;
import com.example.gspot.nearbyplaces.PlaceClass;
import com.example.gspot.placesListView.LazyAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
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
	
	private GoogleMap map;
	ListView list;
	User user;
    LazyAdapter adapter;
    public static final String NAME = "name";
	private LocationClient mLocationClient;
    private Location mCurrentLocation;
    private int counter=0;
    public static ArrayList<PlaceClass> nearbyplaces = new ArrayList<PlaceClass>();
	
	
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		setHasOptionsMenu(true); 
        View rootView = inflater.inflate(R.layout.userpage, container, false);
         
        return rootView;
    }
	
	public void onStart() {
        super.onStart();

        
        Intent i = getActivity().getIntent();
        user = (User) i.getParcelableExtra("user");
        list=(ListView)getView().findViewById(R.id.list);
        final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        setUpMapIfNeeded();
        map.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(getActivity(), this, this);
        mLocationClient.connect();
        list.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
			adb.setTitle("Confirm Check-in");
			adb.setMessage("Do you want to check-in at "+nearbyplaces.get(position).getName()+" ?");
			adb.setIcon(R.drawable.checkinicon);			
			adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Intent i= new Intent(getActivity(), newPostScreen.class); 
	                i.putExtra("place", nearbyplaces.get(position));
	                i.putExtra("user",user);
	                startActivity(i);
					
				}				
			});
			adb.setNegativeButton("No", null);
			adb.show();
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
   
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
    	
        if (map == null) {
            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                                .getMap();
            // Check if we were successful in obtaining the map.
            if (map != null) {
                // The Map is verified. It is now safe to manipulate the map.
 
           }
        }
        
        
    }/*
    @Override
    protected void onStart() {
        super.onStart();
        mLocationClient.connect();
    }*/

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
                    		}
                    		String query = "https://maps.googleapis.com/maps/api/place/search/json?radius=500&key=AIzaSyBg0q_Qi-IbgaVVBCX3MadAt2rFMkwvZWU&location=";
                    		query=query.concat(String.valueOf(mCurrentLocation.getLatitude())+","+String.valueOf(mCurrentLocation.getLongitude()));                    		
                    		new HttpTask().execute(query); 
                    	}
                    	
                    }
                });
        mCurrentLocation = mLocationClient.getLastLocation();
        
    }

    @Override
    public void onDisconnected() {
        Toast.makeText(getActivity(), "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
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
		for( int i = 0; i <= lines.length - 1; i++)
		{	

			if(lines[i].toLowerCase().contains("\"lat\"")){
				PlaceClass temp = new PlaceClass();
				nearbyplaces.add(temp);	
				nearbyplaces.get(counter).setLat(lines[i].substring(lines[i].indexOf(": ")+2,lines[i].indexOf(",")));
				}
			if(lines[i].toLowerCase().contains("\"lng\"")){
				nearbyplaces.get(counter).setLon(lines[i].substring(lines[i].indexOf(": ")+2));
				}
			if(lines[i].toLowerCase().contains("\"name\"")){
				nearbyplaces.get(counter).setName(lines[i].substring(lines[i].indexOf(": \"")+3,lines[i].indexOf("\",")));			
				}
			if(lines[i].toLowerCase().contains("\"place_id\"")){
				nearbyplaces.get(counter).setId(lines[i].substring(lines[i].indexOf(": \"")+3,lines[i].indexOf("\",")));
				counter++;
				}  			
		}
		
        adapter=new LazyAdapter(getActivity(), MyLocationFragment.nearbyplaces);        
        list.setAdapter(adapter);
        
	}
}