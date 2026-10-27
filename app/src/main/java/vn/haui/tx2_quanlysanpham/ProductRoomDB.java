package vn.haui.tx2_quanlysanpham;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Product.class}, version = 1, exportSchema = false)
public abstract class ProductRoomDB extends RoomDatabase {
    public abstract ProductDAO getProductDAO();
    private static ProductRoomDB instance;

    public static synchronized ProductRoomDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ProductRoomDB.class, "product_db").build();
        }
        return instance;
    }
}