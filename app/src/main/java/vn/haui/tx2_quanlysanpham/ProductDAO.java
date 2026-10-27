package vn.haui.tx2_quanlysanpham;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface ProductDAO {
    @Insert
    void insertProduct(Product product);
    @Update
    int updateProduct(Product product);
    @Query("SELECT * FROM product_db ORDER BY code")
    LiveData<List<Product>> getAllProducts();
}