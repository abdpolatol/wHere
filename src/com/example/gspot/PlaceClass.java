package com.example.gspot;


import java.sql.Blob;



import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcel;
import android.os.Parcelable;


public class PlaceClass implements Parcelable{
	private String name;
	private String id;
	private String lat,lon;
	private int imageNumber;
	
	public int getImageNumber() {
		return imageNumber;
	}


	public void setImageNumber(int imageNumber) {
		this.imageNumber = imageNumber;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(id);
		dest.writeString(lat);
		dest.writeString(lon);

		
	}
	public void readFromParcel(Parcel in)
    {
        this.name = in.readString();
        this.id = in.readString();
        this.lat=in.readString();
        this.lon = in.readString();
;
    }
	public static final Parcelable.Creator<PlaceClass> CREATOR = new Parcelable.Creator<PlaceClass>() {
        public PlaceClass createFromParcel(Parcel in) {
            return new PlaceClass(in);
        }

        public PlaceClass[] newArray(int size) {
            return new PlaceClass[size];
        }
    };

    public PlaceClass() {
		super();
	}
	private PlaceClass(Parcel in) {
    	readFromParcel(in);
    }
	

	
}
