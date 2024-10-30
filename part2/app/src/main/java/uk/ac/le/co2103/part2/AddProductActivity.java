package uk.ac.le.co2103.part2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddProductActivity extends AppCompatActivity {

    private static final String TAG = AddProductActivity.class.getSimpleName();

    private EditText editTextName;
    private EditText editTextQuantity;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate()");

        setContentView(R.layout.activity_addproduct);

        editTextName = findViewById(R.id.editTextName);
        editTextQuantity = findViewById(R.id.editTextQuanitity);
        spinner = findViewById(R.id.spinner);

        Button addButton = findViewById(R.id.product_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create product
                String name = editTextName.getText().toString();
                try {
                    int quantity = Integer.parseInt(editTextQuantity.getText().toString());
                    String unit = spinner.getSelectedItem().toString();
                    ProductDao productDao = ProductDB.getDatabase(getApplicationContext()).productDao();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //check if product already exists
                            Product existingProduct = productDao.getProductByName(name);
                            if (existingProduct != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AddProductActivity.this, "Product already exists", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                //insert new product into database
                                Product product = new Product(name, quantity, unit, getIntent().getIntExtra("Id", -1));
                                productDao.insert(product);

                                //navigate back to shopping list activity
                                Intent intent = new Intent(AddProductActivity.this, ShoppingListActivity.class);
                                intent.putExtra("ID", getIntent().getIntExtra("Id", -1));
                                startActivity(intent);
                            }
                        }
                    }).start();
                }catch (NumberFormatException e) {
                    // input is not a valid number, show a toast message to the user
                    Toast.makeText(AddProductActivity.this, "Quantity input must be a number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}