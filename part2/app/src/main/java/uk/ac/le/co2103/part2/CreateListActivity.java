package uk.ac.le.co2103.part2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class CreateListActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = CreateListActivity.class.getSimpleName();
    public static final String EXTRA_REPLY = "uk.edu.le.co2103.lab13.REPLY";

    private List<String> inUse = new ArrayList<String>();

    private EditText nameEditText;
    private ImageView imageView;
    private Uri imageUri;

    private ShoppingListDao shoppingListDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_createlist);

        nameEditText = findViewById(R.id.nameEditText);
        imageView = findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        Button createButton = findViewById(R.id.button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createList();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private void createList() {
        String name = nameEditText.getText().toString();
        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a list name", Toast.LENGTH_SHORT).show();
            return;
        }
        ShoppingListDao shoppingListDao = ShoppingListDB.getDatabase(getApplicationContext()).shoppingListDao();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //check if product already exists
                ShoppingList existingList = shoppingListDao.getShoppingListByName(name);
                if (existingList != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CreateListActivity.this, "Shopping list already exists", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // new list
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("listName", name);

                    if (imageUri != null) {
                        resultIntent.putExtra("listImage", imageUri.toString());
                    }
                    Log.i(TAG, "Adding item to list");
                    resultIntent.putExtra(EXTRA_REPLY, name);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        }).start();
    }

}
