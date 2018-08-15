package com.example.soxluvr23.afinal;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RestaurantView extends AppCompatActivity {
    ArrayList<Restaurant> loc = new ArrayList<Restaurant>();
    Restaurant restaurant = new Restaurant();
    private AutoCompleteTextView textView;
    DecimalFormat df = new DecimalFormat("#.00");
    HashMap<String,Integer> MENU = new HashMap<String,Integer>();
    private String purchaseAddress = "";
    private boolean deliveryPickup = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_view);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        if (intent != null) {
            TextView res = (TextView) findViewById(R.id.Restaurant);
            TextView address = (TextView) findViewById(R.id.address);
            String currentLocation = intent.getCharSequenceExtra("Location").toString();
            loc = getIntent().getParcelableArrayListExtra("Locations");
            purchaseAddress = intent.getCharSequenceExtra("PurchaseLocation").toString();
            deliveryPickup = intent.getBooleanExtra("deliveryPickup",true);
            restaurant = findLoc(loc,currentLocation);
            String hours = setDate();
            res.setText(currentLocation);
            address.setText(restaurant.getAddressName());
            TextView dayview = (TextView) findViewById(R.id.hours);
            dayview.setText(hours);
            ImageView img = (ImageView) findViewById(R.id.image);
            img.setImageResource(restaurant.getImageId());

            String[] menu = Arrays.copyOf( restaurant.getMENU().keySet().toArray(),  restaurant.getMENU().keySet().toArray().length, String[].class);
            MENU = restaurant.getMENU();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_dropdown_item_1line,menu );
            textView = (AutoCompleteTextView)
                    findViewById(R.id.ItemInput);
            textView.setAdapter(adapter);

            final TextView UnitCostInput = (TextView) findViewById(R.id.UnitCostInput);
            final TextView QuantityInput = (TextView) findViewById(R.id.QuantityInput);
            final TextView OrderDisplay = (TextView) findViewById(R.id.OrderDisplay);
            final TextView TotalDisplay = (TextView) findViewById(R.id.TotalDisplay);
            final Button ButtonNewItem = (Button) findViewById(R.id.ButtonNewItem);
            final Button Total = (Button) findViewById(R.id.Total);

            ButtonNewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (textView.getText()+"" != ""){
                        OrderDisplay.append(""+textView.getText()+" x "+QuantityInput.getText()+"\n");
                    }
                    TotalDisplay.setText(df.format(((Double.parseDouble(TotalDisplay.getText()+""))+((Double.parseDouble(UnitCostInput.getText()+""))*(Double.parseDouble(QuantityInput.getText()+"")))))+"");

                    textView.setText("");
                    textView.requestFocus();
                    UnitCostInput.setText("0.00");
                    QuantityInput.setText(1+"");
                    textView.setText("");
                    textView.requestFocus();
                }
            });

            textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(b){
                        textView.showDropDown();
                    }
                    if(!b) {
                        // on focus off
                        String str = textView.getText().toString();

                        ListAdapter listAdapter = textView.getAdapter();
                        for(int i = 0; i < listAdapter.getCount(); i++) {
                            String temp = listAdapter.getItem(i).toString();
                            if(str.compareTo(temp) == 0) {
                                return;
                            }
                        }
                        for (Map.Entry<String,Integer> entry : MENU.entrySet()){
                            Log.d(entry.getKey(),textView.getText().toString());
                            if (entry.getKey().equals(textView.getText().toString())){
                                UnitCostInput.setText(""+df.format(entry.getValue()));
                                break;
                            }
                        }

                        textView.setText("");

                    }
                }
            });

            textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    for (Map.Entry<String,Integer> entry : MENU.entrySet()){
                        if (entry.getKey().equals(textView.getText().toString())){
                            UnitCostInput.setText(""+df.format(entry.getValue()));
                            break;
                        }
                    }
                }
            });
            Total.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (OrderDisplay.getText().toString().equals("")){
                        Log.d("SDS","DSDS");
                    }else{
                        Intent intent = new Intent(RestaurantView.this, CheckOut.class);
                        intent.putExtra("LineItems",OrderDisplay.getText().toString());
                        intent.putExtra("Restaurant",restaurant.getName());
                        intent.putExtra("purchaseAddress",purchaseAddress);
                        intent.putExtra("DeliveryCharge",restaurant.getDeliveryCharge());
                        intent.putExtra("deliveryPickup",deliveryPickup);
                        intent.putExtra("image",restaurant.getImageId());
                        intent.putExtra("Total",TotalDisplay.getText().toString());
                        intent.putExtra("RestaurantAddress",restaurant.getAddressName());
                        startActivity(intent);
                    }

                }
            });


        }
    }

    String setDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        String hours = restaurant.getHours(dayOfTheWeek);
        return hours;

    }

    public Restaurant findLoc(ArrayList<Restaurant> Locations, String Location){

        for (Restaurant r: Locations){
            if (Location.equals(r.getName())){
                return r;
            }
        }
        return null;
    }
}
