package com.example.gspot.Image_Activity;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gspot.R;
import com.example.gspot.User;
import com.example.gspot.newUserPage;
import com.example.gspot.slidingmenu.MyProfileFragment;

public class UploadPicture extends Activity {
    private Uri mImageCaptureUri;
    private ImageView mImageView;
    private User user;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;

    private String selectedImagePath = "";
    boolean GallaryPhotoSelected = false;

    public static String Finalmedia = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Intent i = getIntent();
        user = (User) i.getParcelableExtra("user");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cropmain);

        final String[] items = new String[] { "Take from camera","Select from gallery" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Image");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) { // pick from
                                                                    // camera
            	if (item == 0) {
            	     Intent intent   = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            	     
            	     mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
            	            "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

            	     intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

            	     try {
            	      intent.putExtra("return-data", true);
            	      
            	      startActivityForResult(intent, PICK_FROM_CAMERA);
            	     } catch (ActivityNotFoundException e) {
            	      e.printStackTrace();
            	     }

                } else { // pick from file
                    Intent intent = new Intent();

                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(intent,
                            "Complete action using"), PICK_FROM_FILE);
                }
            }
        });

        final AlertDialog dialog = builder.create();

        Button button = (Button) findViewById(R.id.btn_crop);
        Button uploadbutton = (Button) findViewById(R.id.uploadbutton);
        mImageView = (ImageView) findViewById(R.id.iv_photo);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        uploadbutton.setOnClickListener(new OnClickListener() {            
            @Override            
            public void onClick(View v) {  
                 new Thread(new Runnable() {
                        public void run() {
                             runOnUiThread(new Runnable() {
                                    public void run() {

                                    }
                                });                     
                         uploadFile(selectedImagePath);
                        }
                      }).start();
                }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
        case PICK_FROM_CAMERA:
            Log.i("TAG", "Inside PICK_FROM_CAMERA");
            doCrop();
            break;

        case PICK_FROM_FILE:
            mImageCaptureUri = data.getData();
            Log.i("TAG",
                    "After Crop mImageCaptureUri " + mImageCaptureUri.getPath());
            GallaryPhotoSelected = true;
            doCrop();

            break;

        case CROP_FROM_CAMERA:
            Bundle extras = data.getExtras();

            selectedImagePath = mImageCaptureUri.getPath();

            Log.i("TAG", "After Crop selectedImagePath " + selectedImagePath);

            if (GallaryPhotoSelected) {
                selectedImagePath = getRealPathFromURI(mImageCaptureUri);
                Log.i("TAG", "Absolute Path " + selectedImagePath);
                GallaryPhotoSelected = true;
            }

            Finalmedia = selectedImagePath;

            if (extras != null) {
                // Bitmap photo = extras.getParcelable("data");
                Log.i("TAG", "Inside Extra " + selectedImagePath);
                Bitmap photo = (Bitmap) extras.get("data");
                photo = MyProfileFragment.getRoundedCroppedBitmap(photo, 300);
                selectedImagePath = String.valueOf(System.currentTimeMillis())
                        + ".jpg";

                Log.i("TAG", "new selectedImagePath before file "
                        + selectedImagePath);

                File file = new File(Environment.getExternalStorageDirectory(),
                        selectedImagePath);

                try {
                    file.createNewFile();
                    FileOutputStream fos = new FileOutputStream(file);
                    photo.compress(Bitmap.CompressFormat.PNG, 95, fos);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(this,
                            "Sorry, Camera Crashed-Please Report as Crash A.",
                            Toast.LENGTH_LONG).show();
                }

                selectedImagePath = Environment.getExternalStorageDirectory()
                        + "/" + selectedImagePath;
                Log.i("TAG", "After File Created  " + selectedImagePath);

                Bitmap bm = decodeFile(selectedImagePath);
                
                mImageView.setImageBitmap(bm);
            }

              File f = new File(mImageCaptureUri.getPath());
              if (f.exists()) f.delete();
             

            break;

        }
    }

    public static Bitmap decodeFile(String path) {
        int orientation;
        try {
            if (path == null) {
                return null;
            }
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
         
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap bm = BitmapFactory.decodeFile(path, o2);
            Bitmap bitmap = bm;

            ExifInterface exif = new ExifInterface(path);

            orientation = exif
                    .getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

            Log.e("ExifInteface .........", "rotation =" + orientation);

            // exif.setAttribute(ExifInterface.ORIENTATION_ROTATE_90, 90);

            Log.e("orientation", "" + orientation);
            Matrix m = new Matrix();

            if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
                m.postRotate(180);
                // m.postScale((float) bm.getWidth(), (float) bm.getHeight());
                // if(m.preRotate(90)){
                Log.e("in orientation", "" + orientation);
                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                        bm.getHeight(), m, true);
                return bitmap;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                m.postRotate(90);
                Log.e("in orientation", "" + orientation);
                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                        bm.getHeight(), m, true);
                return bitmap;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                m.postRotate(270);
                Log.e("in orientation", "" + orientation);
                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                        bm.getHeight(), m, true);
                
                return bitmap;
            }
            
            return bitmap;
        } catch (Exception e) {
            return null;
        }

    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void doCrop() {
    	  final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();
    	     
    	     Intent intent = new Intent("com.android.camera.action.CROP");
    	        intent.setType("image/*");
    	        
    	        List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );
    	        
    	        int size = list.size();
    	        
    	        if (size == 0) {         
    	         Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();
    	         
    	            return;
    	        } else {
    	         intent.setData(mImageCaptureUri);
    	            
    	            intent.putExtra("outputX", 200);
    	            intent.putExtra("outputY", 200);
    	            intent.putExtra("aspectX", 1);
    	            intent.putExtra("aspectY", 1);
    	            intent.putExtra("scale", true);
    	            intent.putExtra("return-data", true);
    	            
    	         if (size == 1) {
    	          Intent i   = new Intent(intent);
    	          ResolveInfo res = list.get(0);
    	          
    	          i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
    	          
    	          startActivityForResult(i, CROP_FROM_CAMERA);
    	         } else {
    	          for (ResolveInfo res : list) {
    	           final CropOption co = new CropOption();
    	           
    	           co.title  = getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
    	           co.icon  = getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
    	           co.appIntent= new Intent(intent);
    	           
    	           co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
    	           
    	              cropOptions.add(co);
    	          }
    	         
    	          CropOptionAdapter adapter = new CropOptionAdapter(getApplicationContext(), cropOptions);
    	          
    	          AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	          builder.setTitle("Choose Crop App");
    	          builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
    	              public void onClick( DialogInterface dialog, int item ) {
    	                  startActivityForResult( cropOptions.get(item).appIntent, CROP_FROM_CAMERA);
    	              }
    	          });
    	         
    	          builder.setOnCancelListener( new DialogInterface.OnCancelListener() {
    	              @Override
    	              public void onCancel( DialogInterface dialog ) {
    	                 
    	                  if (mImageCaptureUri != null ) {
    	                      getContentResolver().delete(mImageCaptureUri, null, null );
    	                      mImageCaptureUri = null;
    	                  }
    	              }
    	          } );
    	          
    	          AlertDialog alert = builder.create();
    	          
    	          alert.show();
            }
        }
    }

       public int uploadFile(String sourceFileUri) {
           
     	   String upLoadServerUri ="http://www.ceng.metu.edu.tr/~e1818871/uploads/upload_media.php?userID=".concat(Integer.toString(user.getUserID()));
           String fileName = sourceFileUri;
           int serverResponseCode = 0;
           ProgressDialog dialog = null;
           HttpURLConnection conn = null;
           DataOutputStream dos = null;  
           String lineEnd = "\r\n";
           String twoHyphens = "--";
           String boundary = "*****";
           int bytesRead, bytesAvailable, bufferSize;
           byte[] buffer;
           int maxBufferSize = 1 * 1024 * 1024; 
           File sourceFile = new File(sourceFileUri); 
           if (!sourceFile.isFile()) {
         	  Log.e("uploadFile", "Source File Does not exist");
         	  return 0;
           }
               try { 
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);
                conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName); 
                conn.setRequestProperty("userID", Integer.toString(user.getUserID()).trim()); 
                
                dos = new DataOutputStream(conn.getOutputStream());
      
                dos.writeBytes(twoHyphens + boundary + lineEnd); 
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+ fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
      
                bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size
      
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
      
                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
                  
                while (bytesRead > 0) {
                  dos.write(buffer, 0, bufferSize);
                  bytesAvailable = fileInputStream.available();
                  bufferSize = Math.min(bytesAvailable, maxBufferSize);
                  bytesRead = fileInputStream.read(buffer, 0, bufferSize);               
                 }
      
                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
      
                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();
                 
                Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
                if(serverResponseCode == 200){
                    runOnUiThread(new Runnable() {
                         public void run() {
                             Toast.makeText(UploadPicture.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                         }
                     });                
                }    
                
                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();
                
                
            	HttpClient httpclient=new DefaultHttpClient();
             HttpPost httppost= new HttpPost("http://www.ceng.metu.edu.tr/~e1818871/uploads/update_profile_pic.php");
             List<NameValuePair> nameValuePairs;
             
             nameValuePairs = new ArrayList<NameValuePair>(2);
             
             nameValuePairs.add(new BasicNameValuePair("userID",Integer.toString(user.getUserID()).trim()));
             nameValuePairs.add(new BasicNameValuePair("filename",fileName.trim()));
             
             ResponseHandler<String> responseHandler = new BasicResponseHandler();
             try {
             	httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
     			httpclient.execute(httppost, responseHandler);
     		} catch (ClientProtocolException e) {
     			e.printStackTrace();
     		} catch (IOException e) {
     			e.printStackTrace();
     		}
                 
           } catch (MalformedURLException ex) {  
               dialog.dismiss();  
               ex.printStackTrace();
               Toast.makeText(UploadPicture.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
               Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
           } catch (Exception e) {
               dialog.dismiss();  
               e.printStackTrace();
               Toast.makeText(UploadPicture.this, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
               Log.e("Upload file to server Exception", "Exception : " + e.getMessage(), e);  
           }
           String[] lines = selectedImagePath.split("/");
           user.setImageUrl("http://www.ceng.metu.edu.tr/~e1818871/uploads/"+Integer.toString(user.getUserID())+"/"+lines[lines.length-1]);
           user.setFlag(1);
           Intent i= new Intent(UploadPicture.this, newUserPage.class);
           i.putExtra("user",user);
           startActivity(i);
           finish();
           return serverResponseCode;  
          } 
       

}
