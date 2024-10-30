package uk.ac.le.co2103.part2;

import android.app.Application;

import androidx.lifecycle.LiveData;

import org.apache.http.conn.ssl.StrictHostnameVerifier;

import java.util.List;

public class ShoppingListRepository {
    private ShoppingListDao shoppingListDao;
    private LiveData<List<ShoppingList>> allItems;

    ShoppingListRepository(Application application) {
        ShoppingListDB db = ShoppingListDB.getDatabase(application);
        shoppingListDao = db.shoppingListDao();
        allItems = shoppingListDao.getShoppingLists();
    }

    LiveData<List<ShoppingList>> getAllItems() {
        return allItems;
    }

    void insert(ShoppingList shoppingList) {
        ShoppingListDB.databaseWriteExecutor.execute(() -> {
            shoppingListDao.insert(shoppingList);
        });
    }

    void delete(ShoppingList shoppingList){
        ShoppingListDB.databaseWriteExecutor.execute(() -> {
            shoppingListDao.deleteListById(shoppingList.getListId());
        });
    }
}
