package uk.ac.le.co2103.part2;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ProductRepository {
    private ProductDao productDao;
    private LiveData<List<Product>> allItems;

    ProductRepository(Application application) {
        ProductDB db = ProductDB.getDatabase(application);
        productDao = db.productDao();
        allItems = productDao.getProducts();
    }

    LiveData<List<Product>> getAllItems() {
        return allItems;
    }

    LiveData<List<Product>> getItemsById(int id) {
        return productDao.getProductsById(id);
    }

    void insert(Product product) {
        ProductDB.databaseWriteExecutor.execute(() -> {
            productDao.insert(product);
        });
    }
}
