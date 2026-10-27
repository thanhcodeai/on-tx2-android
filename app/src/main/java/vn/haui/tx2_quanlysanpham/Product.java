package vn.haui.tx2_quanlysanpham;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "product_db")
public class Product {
    @PrimaryKey
    @NonNull
    private String code = "";
    private String name, productionDate;

    public Product() {}
    public String getCode() { return code; }
    public void setCode(@NonNull String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getProductionDate() { return productionDate; }
    public void setProductionDate(String productionDate) { this.productionDate = productionDate; }
}