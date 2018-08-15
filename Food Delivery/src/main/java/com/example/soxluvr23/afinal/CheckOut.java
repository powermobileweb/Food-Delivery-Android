package com.example.soxluvr23.afinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

public class CheckOut extends AppCompatActivity {
    String purchaseAddress = "";
    String restaurantAddress = "";
    boolean deliveryPickup = true;
    private String lineItems = "";
    private String restaurant = "";
    private int deliveryCharge = 0;
    private int imageId = 0;
    private double total = 0.0;
    DecimalFormat df = new DecimalFormat("#.00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
    }

    @Override
    protected void onStart() {
        super.onStart();
        TextView res = (TextView) findViewById(R.id.Restaurant);
        TextView deliverycharge = (TextView) findViewById(R.id.deliveryCharge);
        TextView address = (TextView) findViewById(R.id.address);
        TextView orderdetails = (TextView) findViewById(R.id.OrderDisplay);
        TextView totalview = (TextView) findViewById(R.id.total);
        Button Total = (Button) findViewById(R.id.button2);
        final EditText name = (EditText) findViewById(R.id.editText);
        final EditText phone = (EditText) findViewById(R.id.editText5);
        final EditText card = (EditText) findViewById(R.id.editText4);
        card.addTextChangedListener(new CreditCardNumberFormattingTextWatcher());


        final Intent intent = getIntent();
        if (intent != null) {
            lineItems = intent.getCharSequenceExtra("LineItems").toString();
            restaurant = intent.getCharSequenceExtra("Restaurant").toString();
            purchaseAddress = intent.getCharSequenceExtra("purchaseAddress").toString();
            deliveryCharge = intent.getIntExtra("DeliveryCharge",3);
            deliveryPickup = intent.getBooleanExtra("deliveryPickup", true);
            restaurantAddress = intent.getCharSequenceExtra("RestaurantAddress").toString();
            imageId = intent.getIntExtra("image",0);
            total = Double.parseDouble(intent.getCharSequenceExtra("Total").toString());

            res.setText(restaurant);
            deliverycharge.setText("Delivery Charge: $"+df.format(deliveryCharge));
            ImageView img = (ImageView) findViewById(R.id.image);
            img.setImageResource(imageId);
            if (deliveryPickup){
                orderdetails.setText(lineItems+"\nSUBTOTAL: "+"$"+df.format(total)+"\n\nDelivery Charge $"+df.format(deliveryCharge)+"\nTip $3.00");
                total = total+deliveryCharge+3.00;
                totalview.setText("Total: $"+df.format(total));
            }else{
                orderdetails.setText(lineItems+"SUBTOTAL: "+"$"+df.format(total));
                totalview.setText("$"+df.format(total));
            }
            address.setText(purchaseAddress);
        }



        Total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().equals("") && !phone.getText().toString().equals("") && !card.getText().toString().equals("")){
                    Intent intent = new Intent(CheckOut.this, OrderPlaced.class);
                    intent.putExtra("Total",total);
                    intent.putExtra("Restaurant",restaurant);
                    intent.putExtra("Address",purchaseAddress);
                    intent.putExtra("RestaurantAddress",restaurantAddress);
                    intent.putExtra("image",imageId);
                    startActivity(intent);
                }
            }
        });
    }
    public static class CreditCardNumberFormattingTextWatcher implements TextWatcher {

        private boolean lock;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            for (int i = 4; i < s.length(); i += 5) {
                if (s.toString().charAt(i) != ' ') {
                    s.insert(i, " ");
                }
            }
        }
    }
}
