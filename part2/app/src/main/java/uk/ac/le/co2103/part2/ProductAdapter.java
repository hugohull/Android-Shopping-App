package uk.ac.le.co2103.part2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductAdapter extends ListAdapter<Product, ProductViewHolder> {

    private ProductDao productDao;

    public ProductAdapter(@NonNull DiffUtil.ItemCallback<Product> diffCallback) {
        super(diffCallback);
    }

    public ProductAdapter(DiffUtil.ItemCallback<Product> diffCallback, ProductDao productDao) {
        super(diffCallback);
        this.productDao = productDao;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ProductViewHolder.create(parent, productDao);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product currentProduct = getItem(position);
        holder.bind(currentProduct);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a toast message with the product details
                String toastMessage = "Name: " + currentProduct.getName() + ", Quantity: " + currentProduct.getQuantity() + ", Unit: " + currentProduct.getUnit();
                Toast.makeText(v.getContext(), toastMessage, Toast.LENGTH_SHORT).show();
                // Edit/delete dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Options");
                builder.setItems(new CharSequence[]{"Edit", "Delete"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                // Edit product
                                Intent intent = new Intent(v.getContext(), UpdateProductActivity.class);
                                intent.putExtra("Name", currentProduct.getName());
                                v.getContext().startActivity(intent);
                                break;
                            case 1:
                                // Delete productDe
                                ExecutorService executor = Executors.newSingleThreadExecutor();
                                executor.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        productDao.deleteProductsByName(currentProduct.getName());
                                    }
                                });
                                executor.shutdown();
                                break;
                        }
                    }
                });
                builder.show();
//                return true;
            }
        });
    }
        static class ListDiff extends DiffUtil.ItemCallback<Product> {

        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }
}
