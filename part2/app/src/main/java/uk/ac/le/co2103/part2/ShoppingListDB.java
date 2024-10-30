package uk.ac.le.co2103.part2;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {ShoppingList.class}, version=4, exportSchema = false)
public abstract class ShoppingListDB extends RoomDatabase{

    public abstract ShoppingListDao shoppingListDao();

    private static volatile ShoppingListDB INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static ShoppingListDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ShoppingListDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ShoppingListDB.class, "shoppinglist_db").fallbackToDestructiveMigration().addCallback(sRoomDatabaseCallback).build();
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
                ShoppingListDao dao = INSTANCE.shoppingListDao();
                dao.deleteAll();

//                ShoppingList shoppingList = new ShoppingList("List 1 test", "");
//                dao.insert(shoppingList);
//                shoppingList = new ShoppingList("List 2 test", "");
//                dao.insert(shoppingList);
            });
        }
    };

    public static void resetDatabase(Context context) {
        INSTANCE = null;
        context.deleteDatabase("shoppinglist_db");
    }
}
