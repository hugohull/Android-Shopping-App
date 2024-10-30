package uk.ac.le.co2103.part2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ShoppingListViewHolder extends RecyclerView.ViewHolder {
    private final TextView itemTextView;
    private final ImageView imageView;

    private ShoppingListDao shoppingListDao;
    private ProductDao productDao;
    private Context context;

    private ShoppingListViewHolder(View itemView,ShoppingListDao shoppingListDao, ProductDao productDao) {
        super(itemView);
        itemTextView = itemView.findViewById(R.id.list_name);
        imageView = itemView.findViewById(R.id.list_image);
        this.shoppingListDao = shoppingListDao;
        this.productDao = productDao;
        context = itemView.getContext();
    }

    public ShoppingListViewHolder(View itemView) {
        super(itemView);
        itemTextView = itemView.findViewById(R.id.list_name);
        imageView = itemView.findViewById(R.id.list_image);
    }

    public void bind(int listId, String listName, String listImage) {
        itemTextView.setText(listName);

        if (listImage != null) {
            Glide.with(itemView.getContext())
                    .load(Uri.parse(listImage))
                    .into(imageView);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(itemView.getContext(), ShoppingListActivity.class);
                intent.putExtra("ID", listId);
                System.out.println("ID of list clicked is " + listId);
                itemView.getContext().startActivity(intent);
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Delete Shopping List");
                builder.setMessage("Are you sure you want to delete this shopping list?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // Delete
                                int position = getAdapterPosition();
                                List<ShoppingList> shoppingListList = shoppingListDao.getShoppingListList();
                                ShoppingList shoppingListToDelete = shoppingListList.get(position);
                                shoppingListDao.deleteListById(shoppingListToDelete.getListId());
                                productDao.deleteProductsByListId(shoppingListToDelete.getListId());
                            }
                        }).start();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
                return true;
            }
        });
    }


    public ImageView getImageView() {
        return imageView;
    }

    static ShoppingListViewHolder create(ViewGroup parent, ShoppingListDao shoppingListDao, ProductDao productDao) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new ShoppingListViewHolder(view, shoppingListDao, productDao);
    }

    public void bind(String text) {
        itemTextView.setText(text);
    }
}
