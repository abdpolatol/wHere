package com.example.gspot.Post;

public class comment {

	public boolean left;
	public String comment;
	public String date;
	public String name;
	public String ImageUrl;
	public int userID;
	
	public comment(boolean left, String comment,String date,String name,String ImageUrl,int userID) {
		super();
		this.left = left;
		this.comment = comment;
		this.date=date;
		this.name=name;	
		this.ImageUrl=ImageUrl;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
}
