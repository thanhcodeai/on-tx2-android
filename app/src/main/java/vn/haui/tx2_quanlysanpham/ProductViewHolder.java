package vn.haui.tx2_quanlysanpham;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    TextView tvCode, tvName, tvProductionDate;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        tvCode = itemView.findViewById(R.id.tv_code);
        tvName = itemView.findViewById(R.id.tv_name);
        tvProductionDate = itemView.findViewById(R.id.tv_production_date);
    }
}