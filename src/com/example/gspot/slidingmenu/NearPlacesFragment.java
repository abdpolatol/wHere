package com.example.gspot.slidingmenu;

import com.example.gspot.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NearPlacesFragment extends Fragment {
	
	public NearPlacesFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_near_places, container, false);
         
        return rootView;
    }
}