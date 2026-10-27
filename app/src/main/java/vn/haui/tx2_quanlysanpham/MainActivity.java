package vn.haui.tx2_quanlysanpham;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private EditText edtCode, edtName, edtProductionDate;
    private Button btnAdd, btnRead, btnUpdate, btnShare;
    private RecyclerView rcvProducts;
    private TextView tvEmpty, tvCount;
    private List<Product> products;
    private ProductAdapter productAdapter;
    private int position = -1;
    private String codeSelected;
    private ProductRoomDB db;
    private LiveData<List<Product>> productData;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
        mapping();
        if (savedInstanceState != null) {
            codeSelected = savedInstanceState.getString("codeSelected");
        }
        products = new ArrayList<>();
        productAdapter = new ProductAdapter(products, pos -> {
            position = pos;
            codeSelected = products.get(position).getCode();
            showProduct(products.get(position));
        });
        rcvProducts.setLayoutManager(new LinearLayoutManager(this));
        rcvProducts.setAdapter(productAdapter);
        db = ProductRoomDB.getInstance(this);
        readProducts();

        btnRead.setOnClickListener(v -> {
            readProducts();
            toast("Đã đọc dữ liệu");
        });
        btnAdd.setOnClickListener(v -> {
            Product p = getProduct();
            if (!validateProduct(p)) return;
            executorService.execute(() -> {
                try {
                    db.getProductDAO().insertProduct(p);
                    runOnUiThread(() -> toast("Đã thêm sản phẩm"));
                } catch (SQLiteConstraintException e) {
                    runOnUiThread(() -> toast("Mã sản phẩm đã tồn tại"));
                } catch (Exception e) {
                    runOnUiThread(() -> toast("Không thể thêm sản phẩm"));
                }
            });
        });
        btnUpdate.setOnClickListener(v -> {
            if (codeSelected == null) {
                toast("Hãy chọn sản phẩm trong danh sách");
                return;
            }
            Product p = getProduct();
            if (!codeSelected.equals(p.getCode())) {
                edtCode.setError("Giữ nguyên mã sản phẩm đang chọn");
                return;
            }
            if (!validateProduct(p)) return;
            p.setCode(codeSelected);
            executorService.execute(() -> {
                try {
                    int count = db.getProductDAO().updateProduct(p);
                    runOnUiThread(() -> toast(count > 0 ? "Đã cập nhật sản phẩm" : "Sản phẩm không còn tồn tại"));
                } catch (Exception e) {
                    runOnUiThread(() -> toast("Không thể cập nhật sản phẩm"));
                }
            });
        });
        btnShare.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String date = edtProductionDate.getText().toString().trim();
            if (name.isEmpty() || date.isEmpty()) {
                toast("Nhập tên sản phẩm và ngày sản xuất để chia sẻ");
                return;
            }
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Tên sản phẩm: " + name + "\nNgày sản xuất: " + date);
            try {
                startActivity(Intent.createChooser(intent, "Chia sẻ sản phẩm"));
            } catch (ActivityNotFoundException e) {
                toast("Không có ứng dụng hỗ trợ chia sẻ");
            }
        });
    }

    private void mapping() {
        edtCode = findViewById(R.id.edt_code);
        edtName = findViewById(R.id.edt_name);
        edtProductionDate = findViewById(R.id.edt_production_date);
        btnAdd = findViewById(R.id.btn_add);
        btnRead = findViewById(R.id.btn_read);
        btnUpdate = findViewById(R.id.btn_update);
        btnShare = findViewById(R.id.btn_share);
        rcvProducts = findViewById(R.id.rcv_products);
        tvEmpty = findViewById(R.id.tv_empty);
        tvCount = findViewById(R.id.tv_count);
    }

    private void readProducts() {
        if (productData != null) productData.removeObservers(this);
        productData = db.getProductDAO().getAllProducts();
        productData.observe(this, lstProducts -> {
            products = lstProducts;
            productAdapter.setLstProducts(products);
            productAdapter.notifyDataSetChanged();
            tvCount.setText("Danh sách sản phẩm (" + products.size() + ")");
            tvEmpty.setVisibility(products.isEmpty() ? android.view.View.VISIBLE : android.view.View.GONE);
        });
    }

    private Product getProduct() {
        Product p = new Product();
        p.setCode(edtCode.getText().toString().trim());
        p.setName(edtName.getText().toString().trim());
        p.setProductionDate(edtProductionDate.getText().toString().trim());
        return p;
    }

    private void showProduct(Product p) {
        edtCode.setText(p.getCode());
        edtName.setText(p.getName());
        edtProductionDate.setText(p.getProductionDate());
        edtCode.setError(null);
        edtName.setError(null);
        edtProductionDate.setError(null);
    }

    private boolean validateProduct(Product p) {
        if (p.getCode().isEmpty()) {
            edtCode.setError("Nhập mã sản phẩm");
            return false;
        }
        if (p.getName().isEmpty()) {
            edtName.setError("Nhập tên sản phẩm");
            return false;
        }
        String date = p.getProductionDate();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ROOT);
        format.setLenient(false);
        ParsePosition parsePosition = new ParsePosition(0);
        if (!date.matches("\\d{2}/\\d{2}/\\d{4}") ||
                format.parse(date, parsePosition) == null || parsePosition.getIndex() != date.length()) {
            edtProductionDate.setError("Nhập ngày hợp lệ theo dd/MM/yyyy");
            return false;
        }
        return true;
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("codeSelected", codeSelected);
    }

    @Override
    protected void onDestroy() {
        executorService.shutdown();
        super.onDestroy();
    }
}