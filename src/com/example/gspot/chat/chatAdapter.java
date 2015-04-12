package com.example.gspot.chat;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.gspot.R;
import com.example.gspot.User;
import com.example.gspot.Post.comment;

public class chatAdapter extends BaseAdapter {
	private Activity activity;
    private LayoutInflater inflater;
	private TextView message;
	//private List<comment> messages = new ArrayList<comment>();
	private List<comment> messages;
	User user;
	private LinearLayout wrapper;
	

	public chatAdapter(Activity activity,List<comment> comentList) {
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
            convertView = inflater.inflate(R.layout.list_chat_message, null);
        
		wrapper = (LinearLayout) convertView.findViewById(R.id.wrapperchat);
		message = (TextView) convertView.findViewById(R.id.commentchat);
    
		
       
		message.setText(Html.fromHtml(coment.comment+"<br><small><font color='gray'>"+coment.date+"</font></small>"));
		 
		message.setBackgroundResource(coment.left ? R.drawable.bubble_yellow : R.drawable.bubble_green);
		wrapper.setGravity(coment.left ? Gravity.LEFT : Gravity.RIGHT);

		return convertView;
	}
	
	
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}
