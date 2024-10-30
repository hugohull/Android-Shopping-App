package uk.ac.le.co2103.part2;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ShoppingListDao {

    @Insert
    void insert(ShoppingList shoppingList);

    @Query("SELECT * FROM shoppingLists")
    LiveData<List<ShoppingList>> getShoppingLists();

    @Query("SELECT * FROM shoppingLists")
    List<ShoppingList> getShoppingListList();

    @Query("SELECT * FROM shoppingLists WHERE name = :name")
    ShoppingList getShoppingListByName(String name);

    // Implement deletion
    @Query("DELETE FROM shoppingLists")
    void deleteAll();

    @Query("DELETE FROM shoppingLists WHERE listId = :shoppingListId")
    void deleteListById(int shoppingListId);
}
