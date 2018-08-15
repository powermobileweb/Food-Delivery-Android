# Food-Delivery-Android-Application

On the First Screen it takes a plain text address from the user and converts it to a Location with a Latitude and Longitude value so that it can compare to other locations. There is also the option to choose between Pickup and Delivery. Even though not super clear, you wouldn’t pay a tip or a delivery fee if you were to choose to pick it up yourself. 

After hitting submit, the App takes your Latitude and Longitude and finds the distance between those restaurants and you. The distance method built into the Android Location class returns in meters so there is a conversion to miles needed. Although not super obvious, that is actually a ListView and if it had more restaurants, it would scroll through. That Location gets all restaurants except one in the suburbs in this example. To change the address, just type in the edittext field again and submit. To progress, click on the restaurant you want to eat at.

The next menu is the brains of the application; it actually takes the orders. You type in the Items field and there is a drop down that yields options. Clicking on that will go to quantity and after adjusting that, clicking add to order will add it to the bottom item log, which is a little bit different than assignment 3. To proceed, click Checkout. To go to the previous menu, just hit the back button.

After adding food, it gives you the option to check out and place the order. It reuses the address provided before, but needs the customer’s Name, Phone and Credit Card Number. You can’t actually continue without filling out these fields. Clicking Place order will place the order and bring you to the final menu. 

The final screen shows a map of the restaurant and the customer’s location and displays the order.
