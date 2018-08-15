package com.example.soxluvr23.afinal;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderPlaced extends AppCompatActivity implements OnMapReadyCallback {
    Context con = this;
    Geocoder coder;
    private double total = 0;
    private String restaurant = "";
    private String Address = "";
    private String restaurantAddress = "";
    private int imageId = 0;
    DecimalFormat df = new DecimalFormat("#.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        coder = new Geocoder(con, Locale.getDefault())   ;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        TextView res = (TextView) findViewById(R.id.Restaurant);
        TextView address = (TextView) findViewById(R.id.address);
        TextView Total = (TextView) findViewById(R.id.Total);


        Intent intent = getIntent();
        if (intent != null) {
            total = intent.getDoubleExtra("Total",0);
            restaurant = intent.getCharSequenceExtra("Restaurant").toString();
            Address = intent.getCharSequenceExtra("Address").toString();
            restaurantAddress = intent.getCharSequenceExtra("RestaurantAddress").toString();
            imageId = intent.getIntExtra("image", 0);

            ImageView img = (ImageView) findViewById(R.id.image);
            img.setImageResource(imageId);

        }
        res.setText(restaurant);
        address.setText(restaurantAddress);
        Total.setText("TOTAL: $"+df.format(total));

    }

    Location makeLocation(String Address) {
        ArrayList<Address> adresses = new ArrayList<>();
        try {
            adresses = (ArrayList<Address>) coder.getFromLocationName(Address, 50);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Location locationA = new Location(Address);
        locationA.setLatitude(adresses.get(0).getLatitude());
        locationA.setLongitude(adresses.get(0).getLongitude());
        return locationA;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Location Start = makeLocation(restaurantAddress);
        Location End = makeLocation(Address);
        map.addMarker(new MarkerOptions().position(new LatLng(Start.getLatitude(), Start.getLongitude())).title("Restaurant"));
        map.addMarker(new MarkerOptions().position(new LatLng(End.getLatitude(), End.getLongitude())).title("Destination"));
        float zoomLevel = (float) 11.0; //This goes up to 21
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(End.getLatitude(), End.getLongitude()), zoomLevel));
    }
}
