package uk.ac.le.co2103.part2;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "products"
//        ,
//        foreignKeys = @ForeignKey(
//                entity = ShoppingList.class,
//                parentColumns = "listId",
//                childColumns = "listId",
//                onDelete = ForeignKey.CASCADE,
//                onUpdate = ForeignKey.CASCADE)
                )
public class Product {

    @NonNull
    @PrimaryKey
    private String name;

    @NonNull
    @ColumnInfo(name = "quantity")
    private int quantity;

    @NonNull
    @ColumnInfo(name = "unit")
    private String unit;

    @ColumnInfo(name = "listId")
    private int listId;

    public Product(String name, int quantity, String unit, int listId) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.listId = listId;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}