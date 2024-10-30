package uk.ac.le.co2103.part2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ShoppingListActivity extends AppCompatActivity{

    private static final String TAG = ShoppingListActivity.class.getSimpleName();

    private ProductViewModel productViewModel;

    private static final int CREATE_PRODUCT_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_shopping_list);

        Log.d(TAG, "Creating recyclerView");
        RecyclerView recyclerView = findViewById(R.id.mRecyclerView);
        // Add Dao
        ProductDao productDao = ProductDB.getDatabase(getApplicationContext()).productDao();

        final ProductAdapter adapter = new ProductAdapter(new ProductAdapter.ListDiff(), productDao);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Getting Id of list pressed if applicable
        Bundle extras = getIntent().getExtras();
        int shoppingListId = -1;
        if (extras != null) {
            shoppingListId = extras.getInt("ID");
            Log.d(TAG, "ID of list received is "+ shoppingListId);
        }

        Log.d(TAG, "Adding items to adapter");
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        //for testing
//        productViewModel.getAllProducts().observe(this, products -> {
        productViewModel.getProductsById(shoppingListId).observe(this, products -> {
            if(products == null) {
                Log.d(TAG, "Products list is null.");
            } else {
                Log.d(TAG, "Products list size: " + products.size());
            }
            adapter.submitList(products);
        });

        // FAB
        final FloatingActionButton button = findViewById(R.id.fabAddProduct);
        int finalShoppingListId = shoppingListId;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShoppingListActivity.this, AddProductActivity.class);
                intent.putExtra("Id", finalShoppingListId);
                startActivityForResult(intent, CREATE_PRODUCT_REQUEST_CODE);
            }
        });
    }

}
