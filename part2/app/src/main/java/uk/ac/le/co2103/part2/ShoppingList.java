package uk.ac.le.co2103.part2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "shoppingLists")
public class ShoppingList {

    @PrimaryKey(autoGenerate = true)
    public int listId;

    @NonNull
    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "image")
    public String image;

    public ShoppingList(@NonNull String name, @Nullable String image) {
        this.name = name;
        this.image = image;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public int getListId() {
        return listId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
