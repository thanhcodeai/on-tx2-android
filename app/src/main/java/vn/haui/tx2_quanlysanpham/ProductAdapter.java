package vn.haui.tx2_quanlysanpham;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {
    private List<Product> lstProducts;
    private final OnClickProductListener listener;

    public ProductAdapter(List<Product> lstProducts, OnClickProductListener listener) {
        this.lstProducts = lstProducts;
        this.listener = listener;
    }

    public void setLstProducts(List<Product> lstProducts) {
        this.lstProducts = lstProducts;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product p = lstProducts.get(position);
        holder.tvCode.setText("Mã: " + p.getCode());
        holder.tvName.setText(p.getName());
        holder.tvProductionDate.setText("Ngày sản xuất: " + p.getProductionDate());
        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (listener != null && pos != RecyclerView.NO_POSITION) {
                listener.onClickItemViewProduct(pos);
            }
        });
    }

    @Override
    public int getItemCount() { return lstProducts.size(); }

    public interface OnClickProductListener {
        void onClickItemViewProduct(int pos);
    }
}