package uk.ac.le.co2103.part2;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Product.class, ShoppingList.class}, version = 1, exportSchema = false)
public abstract class ProductDB extends RoomDatabase {

    public abstract ProductDao productDao();

    private static volatile ProductDB INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static ProductDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ProductDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ProductDB.class, "product_db").addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback sRoomDatabaseCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                ProductDao dao = INSTANCE.productDao();
//                dao.deleteAll();

//                Product product1 = new Product("Product 1 test", 1, "Kg", 1);
//                dao.insert(product1);
//                Product product2= new Product("Product 2 test", 69, "Unit", 2);
//                dao.insert(product2);
            });
        }
    };

    public static void resetDatabase(Context context) {
        INSTANCE = null;
        context.deleteDatabase("product_db");
    }
}
