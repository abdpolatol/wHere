package com.example.gspot.slidingmenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gspot.R;
import com.example.gspot.User;
import com.example.gspot.nearbyplaces.PlaceClass;

public class postHomeFragment extends Fragment {
	
	private User user;
	private PlaceClass place;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_photos, container, false);
         
        return rootView;
    }
	@Override
	public void onStart(){
		super.onStart();
		Intent i = getActivity().getIntent();
        user = (User) i.getParcelableExtra("user");
        place = (PlaceClass) i.getParcelableExtra("place");
		
	}
	
}