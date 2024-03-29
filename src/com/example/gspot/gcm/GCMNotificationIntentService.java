package com.example.gspot.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;

import com.example.gspot.MainActivity;
import com.example.gspot.R;
import com.example.gspot.newUserPage;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMNotificationIntentService extends IntentService {
	// Sets an ID for the notification, so it can be updated
	public static final int notifyID = 9001;
	NotificationCompat.Builder builder;

	public GCMNotificationIntentService() {
		super("GcmIntentService");
	}

	public static final String TAG = "GCMNotificationIntentService";

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				sendNotification("Deleted messages on server: "
						+ extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {

				sendNotification(""	+ extras.get(ApplicationConstants.MSG_KEY)); //When Message is received normally from GCM Cloud Server
			}
		}
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(String greetMsg) {
		if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
		
	        Intent resultIntent = new Intent(this, MainActivity.class);
	        resultIntent.putExtra("greetjson", greetMsg);
	        resultIntent.setAction(Intent.ACTION_MAIN);
	        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
	        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
	                resultIntent, PendingIntent.FLAG_ONE_SHOT);
	        
	        NotificationCompat.Builder mNotifyBuilder;
	        NotificationManager mNotificationManager;
	        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	        
	        String[] notificationMsg = greetMsg.split(" ");
	        mNotifyBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.logo);
	        
	        if(notificationMsg[0].equals("1")){
	        	mNotifyBuilder.setContentTitle("Friend Request")
	                .setContentText(notificationMsg[1]+" "+notificationMsg[2]+ " sent you a friend request.");
	        }
	        else if(notificationMsg[0].equals("2")){
	        	mNotifyBuilder.setContentTitle("Friend Activity")
	                .setContentText(notificationMsg[1]+" "+notificationMsg[2]+ " was at "+notificationMsg[3]+" 1 minute ago");
	        }
	        else if (notificationMsg[0].equals("3")){
	        	mNotifyBuilder.setContentTitle("New Message")
                .setContentText(notificationMsg[1]+" "+notificationMsg[2]+ " sent you a message.");
	        }
	        
	        // Set pending intent
	        mNotifyBuilder.setContentIntent(resultPendingIntent);
	        
	        // Set Vibrate, Sound and Light	        
	        int defaults = 0;
	        defaults = defaults | Notification.DEFAULT_LIGHTS;
	        defaults = defaults | Notification.DEFAULT_VIBRATE;
	        defaults = defaults | Notification.DEFAULT_SOUND;
	        
	        mNotifyBuilder.setDefaults(defaults);

	        // Set autocancel
	        mNotifyBuilder.setAutoCancel(true);
	        // Post a notification
	        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
	}
}
