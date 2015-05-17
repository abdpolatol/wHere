package com.example.gspot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
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
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
 
public class RegisterPage extends Activity {
	EditText runame,rpass,rname,rsurname,remail,rpass2,rcity,birthday;
	Button b3;
	HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;
    Spinner s;    
    Calendar myCalendar = Calendar.getInstance();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
    	
    	rname = (EditText)findViewById(R.id.regname);
        rsurname = (EditText)findViewById(R.id.regsurname);
        rcity = (EditText)findViewById(R.id.regcity);
        rpass = (EditText)findViewById(R.id.regpassword);
        rpass2 = (EditText)findViewById(R.id.regpassword2);
        remail = (EditText)findViewById(R.id.regmail);
        runame = (EditText)findViewById(R.id.reguname);
		b3 = (Button)findViewById(R.id.completeregister);
		s = (Spinner) findViewById(R.id.gender);		
		birthday = (EditText)findViewById(R.id.regbirthday);
		birthday.setKeyListener(null);
        
        b3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	dialog = ProgressDialog.show(RegisterPage.this, "", 
                        "Registering user...", true);
                 new Thread(new Runnable() {
                	 
                        @Override
						public void run() {
                            register();                          
                        }
                      }).start();               
            }
        });
              
    	final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

    	    @Override
    	    public void onDateSet(DatePicker view, int year, int monthOfYear,
    	            int dayOfMonth) {
    	        // TODO Auto-generated method stub
    	        myCalendar.set(Calendar.YEAR, year);
    	        myCalendar.set(Calendar.MONTH, monthOfYear);
    	        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    	        updateLabel();
    	    }

    	};     
        
 	   birthday.setOnClickListener(new OnClickListener() {

	        @Override
	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	            new DatePickerDialog(RegisterPage.this, date, myCalendar
	                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
	                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
	        }
	    });
    }
            
    public void register(){
		try{            
           
			
            httpclient=new DefaultHttpClient();
            httppost= new HttpPost("http://www.ceng.metu.edu.tr/~e1818871/register.php");
            String tempgender="Female";
            if(String.valueOf(s.getSelectedItem()).equals(tempgender)){
            	tempgender = "F";
            }
            else{
            	tempgender = "M";
            }                        
	            nameValuePairs = new ArrayList<NameValuePair>(9);
	            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
	            nameValuePairs.add(new BasicNameValuePair("username",runame.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
	            nameValuePairs.add(new BasicNameValuePair("password",rpass.getText().toString().trim()));
	            nameValuePairs.add(new BasicNameValuePair("name",rname.getText().toString().trim()));
	            nameValuePairs.add(new BasicNameValuePair("surname",rsurname.getText().toString().trim()));
	            nameValuePairs.add(new BasicNameValuePair("email",remail.getText().toString().trim()));
	            nameValuePairs.add(new BasicNameValuePair("gender",tempgender.trim()));
	            nameValuePairs.add(new BasicNameValuePair("city",rcity.getText().toString().trim()));
	            nameValuePairs.add(new BasicNameValuePair("birthday",birthday.getText().toString().trim()));
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            
	            
	            ResponseHandler<String> responseHandler = new BasicResponseHandler();
	            String response;
	            String passNotMatch="Password does not match";
	            if(rpass2.getText().toString().equals(rpass.getText().toString())){
	            	response = httpclient.execute(httppost, responseHandler);
	            }
	            else{
	            	response=passNotMatch;
	            }
	                      
	            if(response.equalsIgnoreCase("Record Added")){
	            	dialog.dismiss();
	                runOnUiThread(new Runnable() {
	                    public void run() {
	                        Toast.makeText(RegisterPage.this,"Register Success", Toast.LENGTH_SHORT).show();
	                    }
	                });
	                startActivity(new Intent(RegisterPage.this, MainActivity.class));
	                
	            }else if (response.equalsIgnoreCase("Username taken")){
	            	dialog.dismiss();
	                showUsernameAlert();                
	            }else if (response.equalsIgnoreCase("Record not added")){
	            	dialog.dismiss();
	            	showDBAlert();
	            }else if(response.equalsIgnoreCase("Password does not match")){
	            	dialog.dismiss();
	            	showPasswordAlert();
	            }
	            else {
	            	dialog.dismiss();
	            	showNullAlert(); // boþ gönderiyo stringi null olarak göndermiyo phpden ayarla 
	            }
            
        }catch(Exception e){
            dialog.dismiss();
            System.out.println("Exception : " + e.getMessage());
        }		
	}

	private void showUsernameAlert() {
		RegisterPage.this.runOnUiThread(new Runnable() {
            @Override
			public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterPage.this);
                builder.setTitle("Registration Error.");
                builder.setMessage("Username already exists.")  
                       .setCancelable(false)
                       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                           @Override
						public void onClick(DialogInterface dialog, int id) {
                           }
                       });                     
                AlertDialog alert = builder.create();
                alert.show();               
            }
        });		
	}
	private void showNullAlert() {
		RegisterPage.this.runOnUiThread(new Runnable() {
            @Override
			public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterPage.this);
                builder.setTitle("Registration Error.");
                builder.setMessage("Please fill all of the fields.")  
                       .setCancelable(false)
                       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                           @Override
						public void onClick(DialogInterface dialog, int id) {
                           }
                       });                     
                AlertDialog alert = builder.create();
                alert.show();               
            }
        });		
	}
	private void showDBAlert() {
		RegisterPage.this.runOnUiThread(new Runnable() {
            @Override
			public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterPage.this);
                builder.setTitle("Registration Error.");
                builder.setMessage("Database is not responding.")  
                       .setCancelable(false)
                       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                           @Override
						public void onClick(DialogInterface dialog, int id) {
                           }
                       });                     
                AlertDialog alert = builder.create();
                alert.show();               
            }
        });
		
	}
	/////////////////////////////////////////////////////////////////////
	
	private void updateLabel() {

	    String myFormat = "yyyy-MM-dd"; //In which you need put here
	    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

	    birthday.setText(sdf.format(myCalendar.getTime()));
	    }	
	
	///////////////////////////////////////////////////////////////////////
	private void showPasswordAlert() {
		RegisterPage.this.runOnUiThread(new Runnable() {
            @Override
			public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterPage.this);
                builder.setTitle("Registration Error.");
                builder.setMessage("Password does not match.")  
                       .setCancelable(false)
                       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                           @Override
						public void onClick(DialogInterface dialog, int id) {
                           }
                       });                     
                AlertDialog alert = builder.create();
                alert.show();               
            }
        });
		
	}
}