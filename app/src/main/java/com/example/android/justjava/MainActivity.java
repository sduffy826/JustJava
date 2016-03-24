package com.example.android.justjava;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    final private int MaxQuantity = 10;
    private int quantity;
    private int pricePerCup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            pricePerCup = 5;
            quantity = 0;
        }
        setButtonState();
    }

    /**
     * Called when the user touches the button
     */
    public void submitOrder(View view) {
        EditText edTxtName = (EditText) findViewById(R.id.name);
        String theName = edTxtName.getText().toString();

        CheckBox cbCream = (CheckBox) findViewById(R.id.checkBoxCream);
        boolean cbCream_value = cbCream.isChecked();

        CheckBox cbChocolate = (CheckBox) findViewById(R.id.checkBoxChocolate);
        boolean cbChocolate_value = cbChocolate.isChecked();

        String body = createOrderSummary(theName, quantity,
                calculatePricePerCup(pricePerCup, cbCream_value, cbChocolate_value),  // Price Cup
                calculatePrice(quantity,
                        calculatePricePerCup(pricePerCup, cbCream_value, cbChocolate_value)),
                cbCream_value, cbChocolate_value);


        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        mapIntent.setData(Uri.parse("geo:41.05, -74.14"));
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
/*
        Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
        mailIntent.setData(Uri.parse("mailto:"));
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, "JustJava order for " + theName);
        mailIntent.putExtra(Intent.EXTRA_TEXT, body);
        if (mailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mailIntent);
        }
 */       displayMessage(body);

        Log.i("checkBoxCream", (cbCream_value ? "true" : "false"));
        Log.i("checkBoxChocolate", (cbChocolate_value ? "true" : "false"));

        //String priceMessage = "Amount Due: $" + calculatePrice(quantity, pricePerCup) +
        //        "\nThank you!";
        //displayMessage(priceMessage);
        putUpMessage(getString(R.string.loveyou));
    }

    private void putUpMessage(String theMsg) {
        Context ct = getApplicationContext();
        Toast ts = Toast.makeText(ct, theMsg, 5);
        ts.show();
    }


    /**
     * @param name             is the name of the person
     * @param qty              is number of cups of coffe
     * @param price            is the price per cup
     * @param total            is the total order price (qty*price)
     * @param wantWhippedCream is a boolean if they want whipped cream
     * @param wantChocolate    is a boolean to say they want chocolate or not
     * @return a string that is a summary of the order
     */
    private String createOrderSummary(String name, int qty, int pricePerCup, int total,
                                      boolean wantWhippedCream, boolean wantChocolate) {
        return "Name: " + name + "\n" +
                "Want Whipped Cream: " + (wantWhippedCream ? "Yes" : "No") + "\n" +
                "Want Chocolate: " + (wantChocolate ? "Yes" : "No") + "\n" +
                "Quantity: " + qty + "\n" +
                "Price Per Cup: $" + pricePerCup + "\n" +
                "Total: $" + total + "\n" +
                "Thank you!!";
    }

    /**
     * @param qty   is the number of cups of coffee
     * @param price is the price per cup
     * @return total price (qty * price)
     */
    private int calculatePrice(int qty, int price) {
        return (qty * price);
    }

    private int calculatePricePerCup(int price, boolean hasCream, boolean hasChocolate) {
        return (price + (hasCream ? 1 : 0) + (hasChocolate ? 2 : 0));
    }

    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    private void displayPrice(int number) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(NumberFormat.getCurrencyInstance().format(number));
    }

    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }

    // Increment button
    public void increment(View view) {
        if (quantity > 100) {
            putUpMessage("Cannot order more than 100 cups");
        } else {
            quantity++;
            display(quantity);
        }
        setButtonState();
    }

    // Decrement button
    public void decrement(View view) {
        if (quantity > 0) {
            quantity--;
            display(quantity);
        } else {
            putUpMessage("Can't go below zero cups");
        }
        setButtonState();
    }

    /**
     * Set the button state, i.e. if quantity is > 0 then allow decrement button and order button
     *   if quantity is less than max quantity (constant) then allow increment button
     */
    public void setButtonState() {
        Button bn = (Button) findViewById(R.id.increment_button);
        boolean currValue, shouldBe;

        // If quantity is less than Max Qty then allow increment button
        if (bn != null) {
            currValue = bn.isClickable();
            shouldBe = (quantity < MaxQuantity);
            if (currValue != shouldBe) {
                bn.setClickable(shouldBe);
                bn.setTextColor((shouldBe ? Color.BLACK : Color.LTGRAY));
            }
        }

        // If quantity above zero then allow decrement button
        bn = (Button) findViewById(R.id.decrement_button);
        if (bn != null) {
            currValue = bn.isClickable();
            shouldBe = (quantity > 0);
            if (currValue != shouldBe) {
                bn.setClickable(shouldBe);
                bn.setTextColor((shouldBe ? Color.BLACK : Color.LTGRAY));
            }
        }

        // If quantity above zero allow the order button
        bn = (Button) findViewById(R.id.order_button);
        if (bn != null) {
            currValue = bn.isClickable();
            shouldBe = (quantity > 0);
            if (currValue != shouldBe) {
                bn.setClickable(shouldBe);
                bn.setTextColor((shouldBe ? Color.BLACK : Color.LTGRAY));
            }
        }
    }

}
