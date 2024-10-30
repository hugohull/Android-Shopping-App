package uk.ac.le.co2103.part2;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductDao {

    @Insert
    void insert(Product product);

    @Query("SELECT * FROM products")
    LiveData<List<Product>> getProducts();

    @Query("SELECT * FROM products WHERE listId = :listId")
    LiveData<List<Product>> getProductsById(int listId);

    @Query("SELECT * FROM products WHERE name = :name")
    Product getProductByName(String name);

    @Query("DELETE FROM products")
    void deleteAll();

    @Query("DELETE FROM products WHERE listId = :listId")
    void deleteProductsByListId(int listId);

    @Query("DELETE FROM products WHERE name = :name")
    void deleteProductsByName(String name);

    @Query("UPDATE products SET quantity = :newQuantity WHERE name = :productName")
    void updateProductQuantity(String productName, int newQuantity);
}
