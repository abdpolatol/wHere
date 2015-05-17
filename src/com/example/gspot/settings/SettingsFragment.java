package com.example.gspot.settings;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.gspot.R;
import com.example.gspot.RegisterPage;
import com.example.gspot.User;
import com.example.gspot.newUserPage;

import android.os.Bundle;
import android.app.Fragment;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsFragment extends Fragment{
	 private SeekBar seekBar;
	 private TextView seekText;
	 private Button saveButton;
	 private User user;
	 public int progress;


	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {  
	        View rootView = inflater.inflate(R.layout.settings, container, false);
	        return rootView;
	    }
	 
	 public void onStart() {
	        super.onStart();
	        Intent i = getActivity().getIntent();
	        user = (User) i.getParcelableExtra("user");
	        
	        seekBar = (SeekBar) getView().findViewById(R.id.seekBar);
	        seekText = (TextView) getView().findViewById(R.id.seekText);
	        saveButton =(Button) getView().findViewById(R.id.saveButton);
	        progress = user.getRange();
	        seekBar.setMax(400);
	        final int max=seekBar.getMax()+100;
	        seekBar.setProgress(progress-100);
	        
	        
	        seekText.setText(Integer.toString(progress)+"m");
	        
	        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
	        	
	  		
	  		  
	  		  
	  		  @Override
	  		  public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
	  			 
	  			
	  		    	progress = progresValue;
	  		    	progress+=100;
	  		    	seekText.setText(Integer.toString(progress)+"m");

	  			 // Toast.makeText(getActivity().getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
	  		  }
	  		
	  		  @Override
	  		  public void onStartTrackingTouch(SeekBar seekBar) {
	  			 // Toast.makeText(getActivity().getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
	  		  }
	  		
	  		  @Override
	  		  public void onStopTrackingTouch(SeekBar seekBar) {
	  			 
	  			//  Toast.makeText(getActivity().getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
	  		  }
	  	   });
	       
	        saveButton.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	 
	            	HttpClient httpclient=new DefaultHttpClient();
			        HttpPost httppost = new HttpPost("http://www.ceng.metu.edu.tr/~e1818871/change_range.php?userID="+user.getUserID()+"&range="+progress);
			        
			        try {
			        	
						httpclient.execute(httppost);
						Toast.makeText(getActivity().getApplicationContext(), "Changes saved", Toast.LENGTH_SHORT).show();
						
						Intent i= new Intent(getActivity(), newUserPage.class);
						user.setRange(progress);
						i.putExtra("user", user);
		                startActivity(i);
		                getActivity().finish();
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	            
	        });
	        

	        
	 }

}
