package com.example.gspot.Image_Activity;

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gspot.R;
import com.example.gspot.User;

public class DownloadImage extends Activity {
  Button load_img;
  ImageView img;
  Bitmap bitmap;
  ProgressDialog pDialog;
  private User user;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent i = getIntent();
    user = (User) i.getParcelableExtra("user"); 
    setContentView(R.layout.activity_load);
    load_img = (Button)findViewById(R.id.load);
    img = (ImageView)findViewById(R.id.img);
    load_img.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View arg0) {
         new LoadImage().execute(user.getImageUrl());
      }
    });
  }
  private class LoadImage extends AsyncTask<String, String, Bitmap> {
    @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DownloadImage.this);
            pDialog.setMessage("Loading Image ....");
            pDialog.show();
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
           pDialog.dismiss();
         }else{
           pDialog.dismiss();
           Toast.makeText(DownloadImage.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();
         }
       }
   }
}