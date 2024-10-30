package uk.ac.le.co2103.part2;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {
    private ProductRepository repo;

    private static final String TAG = ProductViewModel.class.getSimpleName();

    private final LiveData<List<Product>> allProducts;

    public ProductViewModel(Application application) {
        super(application);
        repo = new ProductRepository(application);
        allProducts = repo.getAllItems();
    }

    LiveData<List<Product>> getAllProducts() {
        Log.d(TAG, "Getting all products...");
        if(allProducts.getValue() == null) {
            Log.d(TAG, "List is null.");
        } else {
            Log.d(TAG, "List size: " + allProducts.getValue().size());
        }
        return allProducts;
    }

    LiveData<List<Product>> getProductsById(int id) {
        return repo.getItemsById(id);
    }

    public void insert(Product product){
        repo.insert(product);
    }
}
