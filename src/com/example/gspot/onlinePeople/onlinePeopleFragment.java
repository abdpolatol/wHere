package com.example.gspot.onlinePeople;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.gspot.PlaceClass;
import com.example.gspot.R;
import com.example.gspot.User;

public class onlinePeopleFragment extends Fragment {
	private User user;
	// Log tag
    private static final String TAG = onlinePeopleFragment.class.getSimpleName();
 
    // Movies json url
    private static  String url;
    private ProgressDialog pDialog;
    private List<User> userList = new ArrayList<User>();
    private ListView listView;
    private onlinePeopleAdapter adapter;
    private PlaceClass place;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_online_people, container, false);
        return rootView;
    }
	public void onStart(){
		super.onStart();
		Intent i = getActivity().getIntent();
        user = (User) i.getParcelableExtra("user");
		place = (PlaceClass) i.getParcelableExtra("place");
		listView = (ListView) getView().findViewById(R.id.list2);
        adapter = new onlinePeopleAdapter(getActivity(), userList);
        listView.setAdapter(adapter);
        userList.clear();
        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
 
        // changing action bar color
        getActivity().getActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#1b1b1b")));
        

 
        url = "http://www.ceng.metu.edu.tr/~e1818871/online_users.php?userID="+Integer.toString(user.getUserID())+ "&placeID="+place.getId();
        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();
 
                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
 
                                JSONObject obj = response.getJSONObject(i);
                                User temp_user = new User();
                                temp_user.setName(obj.getString("name"));
                                temp_user.setSurname(obj.getString("surname"));
                                temp_user.setImageUrl(obj.getString("profile_pic"));
                                temp_user.setUserID(Integer.parseInt(obj.getString("userID")));
                                
                                userList.add(temp_user);
 
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
        AppController.getInstance().addToRequestQueue(movieReq);
		
	}
	@Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }
 
    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
    	inflater.inflate(R.menu.main, menu);
    	super.onCreateOptionsMenu(menu,inflater);
    }
 
}
	

