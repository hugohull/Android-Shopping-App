package uk.ac.le.co2103.part2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ShoppingListViewModel shoppingListViewModel;
    private static final int CREATE_LIST_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate()");

//        ShoppingListDB.resetDatabase(this);
//        ProductDB.resetDatabase(this);

        setContentView(R.layout.activity_main);

        Log.d(TAG, "Creating recyclerView");
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        // Adding Daos
        ShoppingListDao shoppingListDao = ShoppingListDB.getDatabase(getApplicationContext()).shoppingListDao();
        ProductDao productDao = ProductDB.getDatabase(getApplicationContext()).productDao();
        final ShoppingListAdapter adapter = new ShoppingListAdapter(new ShoppingListAdapter.ListDiff(), shoppingListDao, productDao);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Log.d(TAG, "Adding items to adapter");
        shoppingListViewModel = new ViewModelProvider(this).get(ShoppingListViewModel.class);
        shoppingListViewModel.getAllShoppingLists().observe(this, items -> {
            adapter.submitList(items);
        });

        // FAB
        final FloatingActionButton button = findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateListActivity.class);
                startActivityForResult(intent, CREATE_LIST_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_LIST_REQUEST_CODE && resultCode == RESULT_OK) {
            String listName = data.getStringExtra("listName");
            String listImage = data.getStringExtra("listImage");

            ShoppingList newShoppingList = new ShoppingList(listName, listImage);

            shoppingListViewModel.insert(newShoppingList);
        }
    }

}