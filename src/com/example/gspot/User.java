package com.example.gspot;

import java.sql.Blob;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{
	
	private String name ;
	private String surname;
	private String user_name;
	private String itemDescription;
	private String price;
	private String city;
	private String imageUrl;
	private int imageNumber;
	private int userID;
	private int age;
	private int flag;
	
	public int getAge(){
		return age;
	}
	public void setAge(int age){
		this.age=age;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public int getImageNumber() {
		return imageNumber;
	}
	public void setImageNumber(int imageNumber) {
		this.imageNumber = imageNumber;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(surname);
		dest.writeString(user_name);
		dest.writeInt(userID);
		dest.writeInt(age);
		dest.writeString(city);
		dest.writeString(imageUrl);
		dest.writeInt(flag);
		
	}
	public void readFromParcel(Parcel in)
    {
        this.name = in.readString();
        this.surname = in.readString();
        this.user_name=in.readString();
        this.userID = in.readInt();
        this.age = in.readInt();
        this.city = in.readString();
        this.imageUrl = in.readString();
        this.flag = in.readInt();
    }
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User() {
		super();
	}
	private User(Parcel in) {
    	readFromParcel(in);
    }
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
}
