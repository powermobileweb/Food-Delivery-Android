package com.example.soxluvr23.afinal;

import android.location.Address;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.util.HashMap;
import java.util.Locale;

public class Restaurant implements Parcelable{
    String name = "";
    HashMap<String,Integer> MENU = new HashMap<>();
    HashMap<String,String> hours = new HashMap<>();
    Address address = new Address(new Locale("English","Chicago"));
    double distance = 0;
    int deliveryFee = 0;
    String add = "";
    int imageId = 0;

    Restaurant(){

    }

    protected Restaurant(Parcel in) {
        name = in.readString();
        imageId = in.readInt();
        address = in.readParcelable(Address.class.getClassLoader());
        distance = in.readDouble();
        deliveryFee = in.readInt();
        add = in.readString();
        Bundle bun = in.readBundle();
        hours = (HashMap<String, String>) bun.getSerializable("HOURS");
        MENU = (HashMap<String, Integer>) bun.getSerializable("MENU");

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(imageId);
        dest.writeParcelable(address, flags);
        dest.writeDouble(distance);
        dest.writeInt(deliveryFee);
        dest.writeString(add);
        Bundle bun = new Bundle();
        bun.putSerializable("HOURS",hours);
        bun.putSerializable("MENU",MENU);
        dest.writeBundle(bun);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public void setMENU(HashMap<String,Integer> MENU){
        this.MENU = MENU;
    }

    public void setHours(HashMap<String,String> hours){
        Log.d(""+hours.size(),"sethours");
        this.hours = hours;
    }

    public void setAddress(Address address){
        this.address = address;
    }

    public void setDeliveryFee(int deliveryFee){
        this.deliveryFee = deliveryFee;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getHours(String day){
        Log.d(""+hours.size(),"gethours");

        switch(day){
            case "Sunday":
                return hours.get("Sunday");
            case "Monday":
                return hours.get("Monday");

            case "Tuesday":
                return hours.get("Tuesday");
            case "Wednesday":
                return hours.get("Wednesday");

            case "Thursday":
                return hours.get("Thursday");

            case "Friday":
                return hours.get("Friday");
            case "Saturday":
                return hours.get("Saturday");
        }
        return null;
    }

    public Address getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public void setAddressName(String addressName) {
        this.add = addressName;
    }
    public String getAddressName() {
        return add;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageid(int imageid) {
        this.imageId = imageid;
    }

    public HashMap<String,Integer> getMENU() {
        return MENU;
    }

    public int getDeliveryCharge() {
        return deliveryFee;
    }
}
