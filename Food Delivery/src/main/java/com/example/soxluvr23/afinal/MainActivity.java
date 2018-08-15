package com.example.soxluvr23.afinal;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.location.Geocoder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.location.Address;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String address = "";
    Context con = this;
    Geocoder coder;
    public ArrayList<Restaurant> Locations = new ArrayList<>();
    TextView addressView;
    private boolean deliveryPickup = true;
    Switch swtch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        coder = new Geocoder(con, Locale.getDefault())   ;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swtch = (Switch) findViewById(R.id.switch1);
        swtch.setChecked(true);
        Locations = populate();
        Button button= (Button) findViewById(R.id.addressButton);
        addressView =  (TextView) findViewById(R.id.addressInput);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address = addressView.getText().toString();
                try {
                    ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocationName(address, 50);
                    for(Address add : adresses){
                            HashMap<String,Double> local = findRestaurants(add);
                            dispRestaurants(local);
                            break;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        swtch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    deliveryPickup = true;
                } else {
                    deliveryPickup = false;
                }
            }
        });
    }

    private void dispRestaurants(HashMap<String,Double> local){
        DecimalFormat df = new DecimalFormat("#.##");
        // Find the ListView resource.
        final ListView restaurants = (ListView) findViewById(R.id.ListViewRestaurants);
        // Create ArrayAdapter using the planet list.
        List<String> list = new ArrayList<String>();
        for (Map.Entry<String, Double> entry : local.entrySet()){
            list.add(entry.getKey()+": "+df.format(entry.getValue())+" miles away");
        }
        //Log.d("A",local.size()+"");
        if (local.size() > 0){
            ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, list);

            // Set the ArrayAdapter as the ListView's adapter.
            restaurants.setAdapter( listAdapter );
            restaurants.setClickable(true);
            restaurants.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                    String s = restaurants.getItemAtPosition(position).toString();
                    int i = s.indexOf(":");
                    s = s.substring(0,i);

                    Intent intent = new Intent(MainActivity.this, RestaurantView.class);
                    intent.putExtra("Location", s);
                    intent.putExtra("Locations",Locations);
                    intent.putExtra("PurchaseLocation",addressView.getText().toString());
                    intent.putExtra("deliveryPickup",deliveryPickup);
                    startActivity(intent);
                }
            });
        }
        else{
            List<String> lst = new ArrayList<String>();
            restaurants.setClickable(false);
            lst.add("No Locations near you!");
            ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, lst);

            // Set the ArrayAdapter as the ListView's adapter.
            restaurants.setAdapter( listAdapter );
        }
    }

    private HashMap<String,Double> findRestaurants(Address loc) {
        HashMap<String,Double> local = new HashMap<>();
        Location locationA = new Location("point A");
        locationA.setLatitude(loc.getLatitude());
        locationA.setLongitude(loc.getLongitude());

        for (Restaurant res : Locations){
            //Log.d("A: "+locationA.getLatitude(),""+locationA.getLongitude());
            Location locationB = new Location("point B");
            locationB.setLatitude(res.getAddress().getLatitude());
            locationB.setLongitude(res.getAddress().getLongitude());
            //Log.d("B: "+locationB.getLatitude(),""+locationB.getLongitude());
           // Log.d("DIST: ",locationA.distanceTo(locationB)+"");
            if (locationA.distanceTo(locationB) < 24140.2){
                local.put(res.getName(), (locationA.distanceTo(locationB)*0.000621371));
            }
        }
        return local;
    }

    private ArrayList<Restaurant> populate(){
        ArrayList<Restaurant> Locations = new ArrayList<>();
        Restaurant temp = new Restaurant();
        temp.setName("Chicago's Pizza");
        temp.setAddressName("7225 W Harlem Ave.");
        temp.setDeliveryFee(4);
        Address loc = new Address(new Locale("English","Chicago"));
        loc.setLatitude(42.039788);
        loc.setLongitude(-87.807562);
        temp.setAddress(loc);
        temp.setImageid(R.drawable.chicagos);
        HashMap<String,String> hours = new HashMap<>();
        hours.put("Sunday","11:00 AM - 7:00 PM");
        hours.put("Monday","10:00 AM - 10:00 PM");
        hours.put("Tuesday","10:00 AM - 10:00 PM");
        hours.put("Wednesday","10:00 AM - 10:00 PM");
        hours.put("Thursday","10:00 AM - 10:00 PM");
        hours.put("Friday","10:00 AM - 11:00 PM");
        hours.put("Saturday","10:00 AM - 2:00 AM");
        temp.setHours(hours);
        HashMap<String,Integer> MENU = new HashMap<>();
        MENU.put("Spaghetti",16);
        MENU.put("Spaghettios",25);
        MENU.put("Spaghettios Pizza",30);
        MENU.put("Chocolate Pizza",5);
        MENU.put("Cheese Sticks",4);
        temp.setMENU(MENU);
        Locations.add(temp);

        temp = new Restaurant();
        temp.setName("Slim's Hotdogs");
        temp.setAddressName("469 W Huron St.");
        temp.setDeliveryFee(4);
        loc = new Address(new Locale("English","Chicago"));
        loc.setLatitude(41.894443);
        loc.setLongitude(-87.641243);
        temp.setAddress(loc);
        temp.setImageid(R.drawable.slims);
         hours = new HashMap<>();
        hours.put("Sunday","3:00 PM - 7:00 PM");
        hours.put("Monday","10:00 AM - 10:00 PM");
        hours.put("Tuesday","10:00 AM - 10:00 PM");
        hours.put("Wednesday","10:00 AM - 10:00 PM");
        hours.put("Thursday","10:00 AM - 10:00 PM");
        hours.put("Friday","10:00 AM - 11:00 PM");
        hours.put("Saturday","10:00 AM - 1:00 PM");
        temp.setHours(hours);
        MENU = new HashMap<>();
        MENU.put("Hot Dog",3);
        MENU.put("Cheese Dog",4);
        MENU.put("Midway Monster",10);
        MENU.put("Fries",1);
        MENU.put("Milkshake",3);
        temp.setMENU(MENU);
        Locations.add(temp);

        temp = new Restaurant();
        temp.setName("Johnathan's Jerk Chicken");
        temp.setDeliveryFee(8);
        loc = new Address(new Locale("English","Chicago"));
        loc.setLatitude(41.893215);
        loc.setLongitude(-87.616899);
        temp.setAddress(loc);
        temp.setImageid(R.drawable.johnathans);
        temp.setAddressName("401 E Ontario St.");
        hours = new HashMap<>();
        hours.put("Sunday","6:00 AM - 12:00 AM");
        hours.put("Monday","10:00 AM - 10:00 PM");
        hours.put("Tuesday","10:00 AM - 10:00 PM");
        hours.put("Wednesday","10:00 AM - 10:00 PM");
        hours.put("Thursday","10:00 AM - 10:00 PM");
        hours.put("Friday","10:00 AM - 11:00 PM");
        hours.put("Saturday","6:00 AM - 4:00 AM");
        temp.setHours(hours);
        MENU = new HashMap<>();
        MENU.put("Jerk Chicken",17);
        MENU.put("Side of Rice",5);
        MENU.put("Cornbread",7);
        MENU.put("Fountain Soda",3);
        MENU.put("Beer",8);
        temp.setMENU(MENU);
        Locations.add(temp);

        temp = new Restaurant();
        temp.setName("Oak Lawn Family Restaurant");
        temp.setDeliveryFee(5);
        loc = new Address(new Locale("English","Chicago"));
        temp.setAddressName("9400 SW Hwy");
        loc.setLatitude(41.719444);
        loc.setLongitude(-87.748611);
        temp.setAddress(loc);
        temp.setImageid(R.drawable.oaklawn);
        hours = new HashMap<>();
        hours.put("Sunday","9:00 AM - 3:00 PM");
        hours.put("Monday","9:00 AM - 3:00 PM");
        hours.put("Tuesday","9:00 AM - 3:00 PM");
        hours.put("Wednesday","9:00 AM - 3:00 PM");
        hours.put("Thursday","9:00 AM - 3:00 PM");
        hours.put("Friday","9:00 AM - 3:00 PM");
        hours.put("Saturday","9:00 AM - 3:00 PM");
        temp.setHours(hours);
        MENU = new HashMap<>();
        MENU.put("Omelet",9);
        MENU.put("Hash Browns",5);
        MENU.put("Bacon",7);
        MENU.put("Coffee",3);
        MENU.put("Pancakes",8);
        temp.setMENU(MENU);
        Locations.add(temp);

        temp = new Restaurant();
        temp.setName("SataitoDawgs");
        temp.setAddressName("4104 N Harlem Ave");
        temp.setDeliveryFee(30);
        loc = new Address(new Locale("English","Chicago"));
        loc.setLatitude(41.955527);
        loc.setLongitude(-87.808507);
        temp.setAddress(loc);
        temp.setImageid(R.drawable.sataitos);
        hours = new HashMap<>();
        hours.put("Sunday","ALL DAY");
        hours.put("Monday","ALL DAY");
        hours.put("Tuesday","ALL DAY");
        hours.put("Wednesday","ALL DAY");
        hours.put("Thursday","ALL DAY");
        hours.put("Friday","ALL DAY");
        hours.put("Saturday","ALL DAY");
        temp.setHours(hours);
        MENU = new HashMap<>();
        MENU.put("SataitoDawg",49);
        MENU.put("SataitoFries",27);
        MENU.put("Platinum Hot Dog",1);
        MENU.put("Gold Leaf Fries",1);
        MENU.put("Diamond Infused Soda",1);
        temp.setMENU(MENU);
        Locations.add(temp);

        return Locations;
    }

}
