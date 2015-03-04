package com.example.gspot.Post;

import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.gspot.R;
import com.example.gspot.User;



import com.example.gspot.onlinePeople.AppController;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class postAdapter extends BaseAdapter {
	private Activity activity;
    private LayoutInflater inflater;
	private TextView message;
	//private List<comment> messages = new ArrayList<comment>();
	private List<comment> messages;
	User user;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	private LinearLayout wrapper;
	

	public postAdapter(Activity activity,List<comment> comentList) {
		this.activity = activity;
		this.messages = comentList;
	}

	public int getCount() {
		return this.messages.size();
	}

	public comment getItem(int index) {
		return this.messages.get(index);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		comment coment = messages.get(position);
		Intent i = activity.getIntent();
        user = (User) i.getParcelableExtra("user");
       
		if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.listitem_discuss, null);
        
		 if (imageLoader == null)
	            imageLoader = AppController.getInstance().getImageLoader();
		 NetworkImageView thumbNail = (NetworkImageView) convertView
	                .findViewById(R.id.list_image2);
		 /*
		 System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaa-----------"+user.getUserID());
		if(!coment.left){
			 thumbNail = (NetworkImageView) convertView
	                .findViewById(R.id.list_image3);
			 System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
		}
		*/
		wrapper = (LinearLayout) convertView.findViewById(R.id.wrapper);
		message = (TextView) convertView.findViewById(R.id.comment);

		
		System.out.println("haldun");
		Log.e("haldun", "haldun");
		Log.e("haldun", Integer.toString(messages.size()));
		System.out.println(messages.size());
		

		message.setText(Html.fromHtml("<i><b>"+coment.name+"</b></i><br>"+coment.comment+"<br><small><font color='gray'>"+coment.date+"</font></small>"));
		
		 thumbNail.setImageUrl(coment.ImageUrl, imageLoader);
		 
		message.setBackgroundResource(coment.left ? R.drawable.bubble_yellow : R.drawable.bubble_green);
		wrapper.setGravity(coment.left ? Gravity.LEFT : Gravity.RIGHT);

		return convertView;
	}
	/*
	private class LoadImage extends AsyncTask<String, String, Bitmap> {
	    @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            
	    	}
	       	protected Bitmap doInBackground(String... args) {
	       		try {
	               bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());
	       		} catch (Exception e) {
	       			e.printStackTrace();
	       		}
	       		return bitmap;
	       }
	       protected void onPostExecute(Bitmap image) {
	         if(image != null){
	           img.setImageBitmap(image);
	          
	         }else{
	           
	         }
	       }
	   }
*/
	public Bitmap decodeToBitmap(byte[] decodedByte) {
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}
