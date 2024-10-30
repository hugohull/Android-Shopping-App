package uk.ac.le.co2103.part2;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductViewHolder extends RecyclerView.ViewHolder {

    private final TextView productNameTextView;
    private final TextView productQuantityTextView;
    private final TextView productUnitTextView;

    private ProductDao productDao;

    public ProductViewHolder(View itemView) {
        super(itemView);
        productNameTextView = itemView.findViewById(R.id.product_name);
        productQuantityTextView = itemView.findViewById(R.id.product_quantity);
        productUnitTextView = itemView.findViewById(R.id.product_unit);
    }

    public ProductViewHolder(View itemView, ProductDao productDao) {
        super(itemView);
        productNameTextView = itemView.findViewById(R.id.product_name);
        productQuantityTextView = itemView.findViewById(R.id.product_quantity);
        productUnitTextView = itemView.findViewById(R.id.product_unit);
        this.productDao = productDao;
    }

    public void bind(Product product) {
        productNameTextView.setText(product.getName());
        productQuantityTextView.setText(String.valueOf(product.getQuantity()));
        productUnitTextView.setText(product.getUnit());
    }

    static ProductViewHolder create(ViewGroup parent, ProductDao productDao){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_product, parent, false);
        return new ProductViewHolder(view, productDao);
    }
}
