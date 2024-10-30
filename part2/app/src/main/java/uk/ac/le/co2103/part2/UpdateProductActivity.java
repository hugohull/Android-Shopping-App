package uk.ac.le.co2103.part2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class UpdateProductActivity extends AppCompatActivity {

    private static final String TAG = UpdateProductActivity.class.getSimpleName();

    private TextView productNameTV;
    private TextView productQuantityTV;
    private TextView productUnitTV;
//    private Button minusButton;
//    private Button
private ProductDao productDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_updateproduct);

        // Getting views
        productNameTV = findViewById(R.id.viewProductName);
        productQuantityTV = findViewById(R.id.viewProductQuantity);
        productUnitTV = findViewById(R.id.viewProductUnit);

        // DAO
        productDao = ProductDB.getDatabase(getApplicationContext()).productDao();

        // Getting Name of product pressed
        Bundle extras = getIntent().getExtras();
        String productName = extras.getString("Name");
        Log.d(TAG, "Name of list received is "+ productName);

        // set views
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // get product
                Product selectedProduct = productDao.getProductByName(productName);
                Log.d(TAG, "Selected product is "+ productName);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //set views
                        productNameTV.setText(selectedProduct.getName());
                        productQuantityTV.setText(String.valueOf(selectedProduct.getQuantity()));
                        productUnitTV.setText(selectedProduct.getUnit());
                    }
                });
            }
        });

        // Minus button
        Button minusButton = findViewById(R.id.buttonMinus);
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementQuantity();
            }
        });

        // Plus button
        Button plusButton = findViewById(R.id.buttonPlus);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementQuantity();
            }
        });

        // save button
        Button saveButton = findViewById(R.id.product_update_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Product selectedProduct = productDao.getProductByName(productName);
                        Log.d(TAG, "Selected product is "+ productName);

                        // update the selected product with the new values
                        String newName = productNameTV.getText().toString();
                        int newQuantity = Integer.parseInt(productQuantityTV.getText().toString());
                        String newUnit = productUnitTV.getText().toString();

//                        selectedProduct.setName(newName);
//                        selectedProduct.setQuantity(newQuantity);
//                        selectedProduct.setUnit(newUnit);

                        productDao.updateProductQuantity(selectedProduct.getName(), newQuantity);
//                        productQuantityTV.setText(String.valueOf(newQuantity));
                    }
                });
                executor.shutdown();

                // show a toast message to confirm the update
                Toast.makeText(getApplicationContext(), "Product updated", Toast.LENGTH_SHORT).show();

                // go back to the ShoppingListActivity
                Intent intent = new Intent(getApplicationContext(), ShoppingListActivity.class);
//                intent.putExtra("")
//                startActivity(intent);
                intent.putExtra("ShoppingListId", getIntent().getIntExtra("ShoppingListId", -1));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    // Button methods
    private void decrementQuantity() {
        int quantity = Integer.parseInt(productQuantityTV.getText().toString());
        if (quantity > 1) {
            quantity--;
            productQuantityTV.setText(String.valueOf(quantity));
        }
    }

    private void incrementQuantity() {
        int quantity = Integer.parseInt(productQuantityTV.getText().toString());
        quantity++;
        productQuantityTV.setText(String.valueOf(quantity));
    }
}
