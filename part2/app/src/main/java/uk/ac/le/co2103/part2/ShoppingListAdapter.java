package uk.ac.le.co2103.part2;

import android.net.Uri;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.bumptech.glide.Glide;

public class ShoppingListAdapter extends ListAdapter<ShoppingList, ShoppingListViewHolder> {

    private ShoppingListDao shoppingListDao;
    private ProductDao productDao;
    public ShoppingListAdapter(@NonNull DiffUtil.ItemCallback<ShoppingList> diffCallback) {
        super(diffCallback);
    }

    public ShoppingListAdapter(ListDiff diffCallback, ShoppingListDao shoppingListDao, ProductDao productDao) {
        super(diffCallback);
        this.shoppingListDao = shoppingListDao;
        this.productDao = productDao;
    }

    @Override
    public ShoppingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ShoppingListViewHolder.create(parent, shoppingListDao, productDao);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListViewHolder holder, int position) {
        ShoppingList currentList = getItem(position);
        holder.bind(currentList.getListId(), currentList.getName(), "");
        if (currentList.getImage() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(currentList.getImage()))
                    .into(holder.getImageView());
        }
    }

    static class ListDiff extends DiffUtil.ItemCallback<ShoppingList> {

        @Override
        public boolean areItemsTheSame(@NonNull ShoppingList oldList, @NonNull ShoppingList newList) {
            return oldList == newList;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ShoppingList oldList, @NonNull ShoppingList newList) {
            return oldList.getName().equals(newList.getName());
        }
    }

    public ShoppingList getShoppingListAt(int position) {
        return getItem(position);
    }
}
