package com.example.gspot.slidingmenu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gspot.PlaceClass;
import com.example.gspot.R;
import com.example.gspot.User;
import com.example.gspot.Image_Activity.UploadPicture;
import com.example.gspot.myprofile.PullScrollView;
import com.example.gspot.myprofile.previousPosts;


public class MyProfileFragment extends Fragment implements PullScrollView.OnTurnListener {
	private Animator mCurrentAnimator;
	private int mShortAnimationDuration;
    private PullScrollView mScrollView;
    private ImageView mHeadImg;
    private ImageView profileImage,expandedImageView;
    private User user;
    private TableLayout mMainLayout;
    TextView username,age,city;
    PlaceClass place;
    Bitmap bitmap;
    ProgressDialog pDialog;
    private TextView editProfile;
    int friendID;
    String friendName,friendSurname,friendPhoto;
	int 	flag;
	int flag1;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.act_pull_down, container, false);         
        return rootView;
    }
	
	@Override
    public void onStart() {
        super.onStart();
        Bundle bundle=this.getArguments();
        flag=bundle.getInt("flag");
        Intent i = getActivity().getIntent();
        user = (User) i.getParcelableExtra("user");        
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	StrictMode.setThreadPolicy(policy);
    	final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
    	
		username = (TextView)getView().findViewById(R.id.user_name);
		age = (TextView)getView().findViewById(R.id.user_age);
		city = (TextView)getView().findViewById(R.id.user_city);
		initView();
		editProfile = (TextView)getView().findViewById(R.id.attention_user);
		
		editProfile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	if(flag == 0){
            		
            	}
            	if(flag == 1){
            		adb.setTitle("Confirm Unfriend");
        			adb.setMessage("Do you want to unfriend "+friendName+" "+friendSurname+" ?");
        			adb.setIcon(R.drawable.delete);			
        			adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
        				public void onClick(DialogInterface dialog, int which) {
        					deletefriend();
        					Bundle bundle= new Bundle();
        					flag=3;
        	            	bundle.putInt("flag", flag);
        	            	bundle.putString("friendName",friendName);
        	            	bundle.putString("friendSurname",friendSurname);
        	            	bundle.putString("friendPhoto",friendPhoto);          
        	            	bundle.putInt("friendID",friendID);
        	            	
        	            	MyProfileFragment fragment=new MyProfileFragment();
        	            	fragment.setArguments(bundle);
        	            	
        	            	FragmentManager fragmentManager = getActivity().getFragmentManager();
        	            	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        	            	fragmentTransaction.replace(R.id.myprofilefragment, fragment);

        	            	fragmentTransaction.commit();
        				}				
        			});
        			adb.setNegativeButton("No", null);
        			adb.show();
            	}
            	if(flag == 2){
            		adb.setTitle("Cancel Friend Request");
        			adb.setMessage("Do you want to cancel friend request?");
        			adb.setIcon(R.drawable.delete);			
        			adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
        				public void onClick(DialogInterface dialog, int which) {
        					deletefriend();
        					Bundle bundle= new Bundle();
        					flag=3;
        	            	bundle.putInt("flag", flag);
        	            	bundle.putString("friendName",friendName);
        	            	bundle.putString("friendSurname",friendSurname);
        	            	bundle.putString("friendPhoto",friendPhoto);          
        	            	bundle.putInt("friendID",friendID);
        	            	
        	            	MyProfileFragment fragment=new MyProfileFragment();
        	            	fragment.setArguments(bundle);
        	            	
        	            	FragmentManager fragmentManager = getActivity().getFragmentManager();
        	            	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        	            	fragmentTransaction.replace(R.id.myprofilefragment, fragment);

        	            	fragmentTransaction.commit();
        				}				
        			});
        			adb.setNegativeButton("No", null);
        			adb.show();
            	}
            	if(flag == 3){
            		adb.setTitle("Confirm Add Friend");
        			adb.setMessage("Do you want to add "+friendName+" "+friendSurname+" as friend?");
        			adb.setIcon(R.drawable.add);			
        			adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
        				public void onClick(DialogInterface dialog, int which) {
        					addfriend();
        					Bundle bundle= new Bundle();
        					flag=2;
        	            	bundle.putInt("flag", flag);
        	            	bundle.putString("friendName",friendName);
        	            	bundle.putString("friendSurname",friendSurname);
        	            	bundle.putString("friendPhoto",friendPhoto);          
        	            	bundle.putInt("friendID",friendID);
        	            	
        	            	MyProfileFragment fragment=new MyProfileFragment();
        	            	fragment.setArguments(bundle);
        	            	
        	            	FragmentManager fragmentManager = getActivity().getFragmentManager();
        	            	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        	            	fragmentTransaction.replace(R.id.myprofilefragment, fragment);

        	            	fragmentTransaction.commit();
        				}				
        			});
        			adb.setNegativeButton("No", null);
        			adb.show();
            	}
            	if(flag == 4){
            		adb.setTitle("Confirm Friend Request");
        			adb.setMessage("Do you want to be friend with "+friendName+" "+friendSurname+" ?");
        			adb.setIcon(R.drawable.add);			
        			adb.setPositiveButton("Confirm", new AlertDialog.OnClickListener() {
        				public void onClick(DialogInterface dialog, int which) {
        					confirmfriend();
        					Bundle bundle= new Bundle();
        					flag=1;
        	            	bundle.putInt("flag", flag);
        	            	bundle.putString("friendName",friendName);
        	            	bundle.putString("friendSurname",friendSurname);
        	            	bundle.putString("friendPhoto",friendPhoto);          
        	            	bundle.putInt("friendID",friendID);
        	            	
        	            	MyProfileFragment fragment=new MyProfileFragment();
        	            	fragment.setArguments(bundle);
        	            	
        	            	FragmentManager fragmentManager = getActivity().getFragmentManager();
        	            	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        	            	fragmentTransaction.replace(R.id.myprofilefragment, fragment);

        	            	fragmentTransaction.commit();
        				}				
        			});
        			adb.setNegativeButton("Decline", new AlertDialog.OnClickListener() {
        				public void onClick(DialogInterface dialog, int which) {
        					deletefriend();
        					Bundle bundle= new Bundle();
        					flag=3;
        	            	bundle.putInt("flag", flag);
        	            	bundle.putString("friendName",friendName);
        	            	bundle.putString("friendSurname",friendSurname);
        	            	bundle.putString("friendPhoto",friendPhoto);          
        	            	bundle.putInt("friendID",friendID);
        	            	
        	            	MyProfileFragment fragment=new MyProfileFragment();
        	            	fragment.setArguments(bundle);
        	            	
        	            	FragmentManager fragmentManager = getActivity().getFragmentManager();
        	            	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        	            	fragmentTransaction.replace(R.id.myprofilefragment, fragment);

        	            	fragmentTransaction.commit();
        				}				
        			});
        			adb.show();
            	}
            	
            }
		});
    	if(flag==0){
    		username.setText(user.getName().concat(" ").concat(user.getSurname()) );
    		age.setText(Integer.toString(user.getAge()));
    		city.setText(user.getCity());     	
    		new LoadImage().execute(user.getImageUrl());   
    		editProfile.setOnClickListener(new OnClickListener() {
    		@Override
            	public void onClick(View v) {
            	 
            	}
    		});
    		flag1=1;
    		registerForContextMenu(profileImage);
    		showTable();
    	}
    	else{
    		friendID=(bundle.getInt("friendID"));
        	friendName=(bundle.getString("friendName"));
        	friendSurname=(bundle.getString("friendSurname"));
        	friendPhoto=(bundle.getString("friendPhoto"));
        	if(flag==1){
        		username.setText(friendName+" "+friendSurname);
        		new LoadImage().execute(friendPhoto);
        		editProfile.setText("Unfriend");
        		editProfile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.delete, 0, 0, 0);
        		flag1=0;
        		registerForContextMenu(profileImage);
        	}
        	if(flag==2){
        		username.setText(friendName+" "+friendSurname);
        		new LoadImage().execute(friendPhoto);
        		editProfile.setText("In Progress");
        		editProfile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.waiting, 0, 0, 0);
        		flag1=0;
        		registerForContextMenu(profileImage);
        	}
        	if(flag==3){
        		username.setText(friendName+" "+friendSurname);
        		new LoadImage().execute(friendPhoto);
        		editProfile.setText("Add Friend");
        		flag1=0;
        		registerForContextMenu(profileImage);
        		//editProfile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.waiting, 0, 0, 0);
        	}
        	if(flag==4){
        		username.setText(friendName+" "+friendSurname);
        		new LoadImage().execute(friendPhoto);
        		editProfile.setText("Accept/Decline Request");
        		editProfile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.confirm, 0, 0, 0);
        		flag1=0;
        		registerForContextMenu(profileImage);
        	}
    	}      
    }
	protected void initView() {
        mScrollView = (PullScrollView) getView().findViewById(R.id.scroll_view);
        mHeadImg = (ImageView) getView().findViewById(R.id.background_img);        
        profileImage = (ImageView) getView().findViewById(R.id.user_avatar);
        expandedImageView = (ImageView) getView().findViewById(R.id.expanded_image);
        
        
        mMainLayout = (TableLayout) getView().findViewById(R.id.table_layout);
        mScrollView.setHeader(mHeadImg);
        mScrollView.setOnTurnListener(this);
    }

    public void showTable() {
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
     //   layoutParams.gravity = Gravity.CENTER;
        layoutParams.leftMargin = 30;
        layoutParams.bottomMargin = 10;
        layoutParams.topMargin = 10;
        ArrayList <previousPosts> temporaryPost = fetchPosts();
        

        for (int i = 0; i < temporaryPost.size(); i++) {
            TableRow tableRow = new TableRow(getActivity());
            TextView textView = new TextView(getActivity());
            textView.setText(temporaryPost.get(i).getPost()+" at "+temporaryPost.get(i).getPlaceName()+" "+temporaryPost.get(i).getPostDate());
            textView.setTextSize(12);
            textView.setMinHeight(2);
            
           // textView.setPadding(15, 15, 15, 15);

            tableRow.addView(textView, layoutParams);
            if (i % 2 != 0) {
                tableRow.setBackgroundColor(Color.LTGRAY);
            } else {
                tableRow.setBackgroundColor(Color.WHITE);
            }

            final int n = i;
            tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Click item " + n, Toast.LENGTH_SHORT).show();
                }
            });

            mMainLayout.addView(tableRow);
        }
    }

    public void onTurn() {

    }
 public String getPosts()  {
    	
    	
    	String result = "";
    	//the year data to send
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    	nameValuePairs.add(new BasicNameValuePair("userID",Integer.toString(user.getUserID()).trim()));
    	InputStream is = null;
    	
    	try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://www.ceng.metu.edu.tr/~e1818871/myposts.php");
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

    public ArrayList<previousPosts> fetchPosts(){
    	
    	String result= getPosts();
    	
		ArrayList<previousPosts> results = new ArrayList<previousPosts>();
		
        try{
            JSONArray jArray = new JSONArray(result);
      
            for(int i=0;i<jArray.length();i++){
            	

            	previousPosts prevPost = new previousPosts();
                    JSONObject json_data = jArray.getJSONObject(i);
                    String placeName=json_data.getString("place_name");
                    String post=json_data.getString("post");
                    String date=json_data.getString("post_date");

                    prevPost.setPlaceName(placeName);
                    prevPost.setPost(post);
                    prevPost.setPostDate(date);            		
               		results.add(prevPost);
            }
    }
    catch(JSONException e){
            Log.e("log_tag", "Error parsing data "+e.toString());
    }

        return results;
    }
    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Loading Image ....");
                pDialog.show();
        	}
           	protected Bitmap doInBackground(String... args) {
           		try {
                   bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());
           		} catch (Exception e) {
           			e.printStackTrace();
           		}
           		bitmap=getRoundedCroppedBitmap(bitmap,300);
           		return bitmap;
           }
           protected void onPostExecute(Bitmap image) {
             if(image != null){
               profileImage.setImageBitmap(image);
               expandedImageView.setImageBitmap(image);
               pDialog.dismiss();
             }else{
               pDialog.dismiss();
               Toast.makeText(getActivity(), "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();
             }
           }
       }
   
    @Override   
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)  
    {  
            super.onCreateContextMenu(menu, v, menuInfo);  
            if(flag1==1){
            menu.setHeaderTitle("Profile Picture Settings");    
            menu.add(0, v.getId(), 0, "Show picture");//groupId, itemId, order, title   
            menu.add(0, v.getId(), 0, "Change picture");  
            }
            else{
            	zoomImageFromThumb(profileImage, 1);
            }
    }
    @Override    
    public boolean onContextItemSelected(MenuItem item){    
            if(item.getTitle()=="Show picture"){  
            	zoomImageFromThumb(profileImage, 1);
            }    
            else if(item.getTitle()=="Change picture"){  
            	Intent i= new Intent(getActivity(), UploadPicture.class);
                i.putExtra("user",user);
                startActivity(i);  

            }else{  
               return false;  
            }    
          return true;    
      }  
    private void zoomImageFromThumb(final View thumbView, int imageResId) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        getView().findViewById(R.id.myprofilefragment)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                            .ofFloat(expandedImageView, View.X, startBounds.left))
                            .with(ObjectAnimator
                                    .ofFloat(expandedImageView, 
                                            View.Y,startBounds.top))
                            .with(ObjectAnimator
                                    .ofFloat(expandedImageView, 
                                            View.SCALE_X, startScaleFinal))
                            .with(ObjectAnimator
                                    .ofFloat(expandedImageView, 
                                            View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
       
    } 
    public void deletefriend(){
    	HttpPost httppost = new HttpPost("http://www.ceng.metu.edu.tr/~e1818871/friends/delete_friend.php?userID="+user.getUserID()+"&friendID="+Integer.toString(friendID));
    	HttpClient httpclient = new DefaultHttpClient();
        try {
			httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
    public void addfriend(){
    	HttpPost httppost = new HttpPost("http://www.ceng.metu.edu.tr/~e1818871/friends/add_friend.php?userID="+user.getUserID()+"&friendID="+Integer.toString(friendID));
    	HttpClient httpclient = new DefaultHttpClient();
    	HttpPost httppost2 = new HttpPost("http://www.ceng.metu.edu.tr/~e1818871/processmessage.php?mode=1&userID="+Integer.toString(friendID)+"&name="+user.getName()+"&surname="+user.getSurname());
        try {
			httpclient.execute(httppost);
			httpclient.execute(httppost2);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
    public void confirmfriend(){
    	HttpPost httppost = new HttpPost("http://www.ceng.metu.edu.tr/~e1818871/friends/accept_friend.php?userID="+user.getUserID()+"&friendID="+Integer.toString(friendID));
    	HttpClient httpclient = new DefaultHttpClient();
        try {
			httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 50;
        int targetHeight = 50;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, 
                            targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
            ((float) targetHeight - 1) / 2,
            (Math.min(((float) targetWidth), 
            ((float) targetHeight)) / 2),
            Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap, 
            new Rect(0, 0, sourceBitmap.getWidth(),
            sourceBitmap.getHeight()), 
            new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }
    public static Bitmap getRoundedCroppedBitmap(Bitmap bitmap, int radius) {
        Bitmap finalBitmap;
        if(bitmap.getWidth() != radius || bitmap.getHeight() != radius)
            finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        else
            finalBitmap = bitmap;
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(),
                finalBitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(), finalBitmap.getHeight());

        paint.setAntiAlias(true);
     
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(finalBitmap.getWidth() / 2+0.7f, finalBitmap.getHeight() / 2+0.7f,
                finalBitmap.getWidth() / 2+0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, rect, paint);


                return output;
    }
}
