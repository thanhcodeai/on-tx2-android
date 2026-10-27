# Quy ước làm bài Android TX2

Đây là quy ước của người dùng, dựa trên layout đã cung cấp và project
`BH03_V2_StudentsM`. Agent phải đọc toàn bộ tài liệu trước khi làm bài TX2.
Tài liệu hướng dẫn cách triển khai; không phải mô tả mọi chức năng hiện có trong repo.

## 1. Thứ tự ưu tiên và phạm vi

1. Yêu cầu trực tiếp của người dùng.
2. Đề bài kiểm tra được cung cấp.
3. Các quy định trong tài liệu này.
4. Code ví dụ tham khảo.

Nếu không thể giải quyết mâu thuẫn theo thứ tự trên và mâu thuẫn ảnh hưởng
đến kết quả, hỏi người dùng. Tự quyết định các chi tiết thông thường theo mẫu.
Không tự thêm chức năng, trường dữ liệu hoặc kiến trúc ngoài phạm vi đề.

**Bắt buộc theo quy ước:** Java + XML, Material 3, cấu trúc layout bên dưới,
cách đặt tên và cách chọn ngày bằng `MaterialDatePicker`.

**Tùy đề:** đối tượng quản lý, số trường, số nút, avatar, giới hạn ngày,
lưu dữ liệu và chức năng chia sẻ. Dùng RecyclerView cho danh sách; dùng Room
khi đề yêu cầu Room hoặc lưu dữ liệu dạng bảng trên máy theo mẫu TX2.

## 2. Cấu trúc thư mục và đặt tên

Với bài nhỏ, đặt các lớp Java chung package. Không tự thêm các tầng MVVM,
Repository hoặc DI nếu đề và người dùng không yêu cầu.

```text
app/src/main/
├── AndroidManifest.xml
├── java/<package>/
│   ├── MainActivity.java
│   ├── Student.java
│   ├── StudentAdapter.java
│   ├── StudentViewHolder.java
│   ├── StudentDAO.java       # Khi dùng Room
│   └── StudentRoomDB.java    # Khi dùng Room
└── res/
    ├── layout/
    │   ├── activity_main.xml
    │   └── student_item.xml
    ├── drawable/            # Icon, avatar nếu cần
    └── values/
        ├── strings.xml
        ├── colors.xml
        └── themes.xml
```

`Student` chỉ là ví dụ. Bài sản phẩm dùng `Product`, `ProductAdapter`,
`ProductViewHolder`, `ProductDAO`, `ProductRoomDB` và `product_item.xml`.
Không giữ các trường sinh viên trong bài sản phẩm.

File XML và ID dùng `snake_case`; lớp Java dùng `PascalCase`;
biến và hàm Java dùng `camelCase`.

| View | Tiền tố ID | Ví dụ |
|---|---|---|
| MaterialTextView tiêu đề | `mtv_` | `mtv_title` |
| MaterialCardView | `mcv_` | `mcv_edt` |
| TextInputLayout | `til_` | `til_dob` |
| TextInputEditText | `tie_` | `tie_dob` |
| MaterialButton | `mbtn_` | `mbtn_add` |
| LinearLayout | `lo_` | `lo_btn` |
| RecyclerView | `rcv_` | `rcv_students`, `rcv_products` |
| ImageView | `img_` | `img_avt` |
| TextView trong item | `tv_` | `tv_name`, `tv_mobie` |

Giữ `mobie` khi làm tiếp project mẫu để tương thích tên sẵn có.
Dùng `@+id/...` để khai báo, `@id/...` để tham chiếu ID đã khai báo.
Khi sửa project hiện có, không đổi tên hàng loạt ngoài yêu cầu;
nếu đổi phải đồng bộ XML và Java.

## 3. Layout màn hình chính: activity_main.xml

Giữ cấu trúc từ trên xuống:

```text
ConstraintLayout (main, margin 16dp)
├── MaterialTextView (mtv_title)
├── MaterialCardView (mcv_edt)
│   └── LinearLayout dọc (margin 6dp)
│       └── Các TextInputLayout → TextInputEditText
├── LinearLayout ngang (lo_btn)
│   └── Các MaterialButton
└── RecyclerView (rcv_<danh_sách>)
```

- Root `ConstraintLayout`: rộng/cao `match_parent`, ID `main`, margin `16dp`.
  Xử lý system bar insets để nội dung không bị che.
- Tiêu đề `MaterialTextView`: cao `wrap_content`, chữ `20sp`, padding dưới
  `8dp`; constraint trên, đầu và cuối vào parent.
- Card nhập liệu: cao `wrap_content`, bo góc `8dp`, elevation `1dp`;
  đặt dưới tiêu đề, hai cạnh nối parent.
- Bên trong card là `LinearLayout` dọc, margin `6dp`.
  Mỗi trường dùng `TextInputLayout` bọc `TextInputEditText`.
  Dùng style `@style/Widget.Material3.TextInputLayout.OutlinedBox`.
- Icon đầu ô dùng `app:startIconDrawable` khi phù hợp dữ liệu.
  Đặt chuỗi giao diện trong `strings.xml`, cỡ chữ dùng `sp`.
- Hàng nút nằm ngoài card, bên dưới card. Dùng `MaterialButton` với style
  `@style/Widget.Material3.Button.TextButton`, rộng `0dp`, weight `1`
  để chia đều. Có thể chia hai hàng khi nhiều nút hoặc nhãn dài.
- RecyclerView nằm dưới hàng nút. Khi đề yêu cầu chiếm diện tích còn lại,
  dùng chiều cao `0dp`, constraint trên vào đáy hàng nút, dưới vào đáy
  parent, đầu/cuối vào parent.
- Không bọc RecyclerView trong ScrollView. Nếu form dài trên màn hình nhỏ,
  chỉ bổ sung vùng cuộn cho form và giữ danh sách có diện tích sử dụng.
- Theme ứng dụng kế thừa theme Material 3, ví dụ
  `Theme.Material3.DayNight.NoActionBar`.

## 4. Layout một dòng: student_item.xml

```text
MaterialCardView (match_parent × wrap_content)
└── ConstraintLayout (margin 4dp)
    ├── ImageView (img_avt, bên trái, nếu cần)
    ├── TextView (tv_name, bên phải ảnh, phía trên)
    └── TextView (tv_mobie, bên phải ảnh, dưới tên)
```

- Card bo góc `8dp`, elevation `1dp`.
- Avatar theo mẫu: `60dp × 60dp`, padding `3dp`, nguồn `@drawable/ic_avt`.
  Chỉ tham chiếu drawable thực sự tồn tại.
- TextView rộng `0dp`, cao `wrap_content`, padding `3dp`;
  đầu nối cuối ảnh, cuối nối parent; các dòng nối tiếp nhau theo chiều dọc.
- Nếu không có ảnh, nối đầu các TextView vào parent.
- Bảo đảm chiều cao item bao trọn ảnh và văn bản; kiểm tra tên dài/nhiều dòng.
- Ảnh chỉ trang trí dùng `importantForAccessibility="no"`;
  ảnh mang thông tin cần `contentDescription` phù hợp.
- Thêm/bớt dòng theo đề; adapter phải inflate đúng layout item.

## 5. Quy định chọn ngày theo BH03_V2_StudentsM

**Phạm vi chặn nhập tay là ô ngày trên form**, đúng cơ chế project mẫu.
Áp dụng cho ngày sinh, ngày sản xuất, ngày hết hạn và các trường ngày khác.

Luồng bắt buộc:

1. Ô ngày là `TextInputEditText` trong `TextInputLayout`, có icon lịch
   `@android:drawable/ic_menu_day`, đặt `android:focusable="false"`.
   Không chỉ dùng `inputType="date"` vì vẫn cho gõ trực tiếp.
2. Sau `mapping()`, đăng ký `setOnClickListener()` cho ô để gọi hàm riêng
   `showDatePickerDialog()`.
3. Trong hàm này, tạo `MaterialDatePicker<Long>` bằng
   `MaterialDatePicker.Builder.datePicker()`; tiêu đề theo trường dữ liệu,
   chọn mặc định hôm nay bằng `MaterialDatePicker.todayInUtcMilliseconds()`.
4. Nhận ngày bằng `addOnPositiveButtonClickListener()`, định dạng
   `dd/MM/yyyy`, gọi `setText()` và `TextInputLayout.setErrorEnabled(false)`.
5. Hiển thị bằng `show(getSupportFragmentManager(), "DATE_PICKER")`.
   Hủy/đóng dialog không thay đổi giá trị ô.

Project mẫu mở lại lịch với ngày hôm nay, chưa giới hạn ngày tương lai.
Chỉ thêm giới hạn hoặc khôi phục ngày đã chọn khi đề/người dùng yêu cầu.

**Không tự sửa giao diện nội bộ của MaterialDatePicker để ẩn nút chuyển
sang nhập văn bản.** Project mẫu vẫn có chế độ đó; chặn nhập ở ô form
không đồng nghĩa khóa mọi cách nhập trong dialog. Chỉ mở rộng phạm vi này
khi người dùng yêu cầu rõ.

## 6. Xử lý Java và dữ liệu

- `MainActivity`: dùng `mapping()` để ánh xạ view; thiết lập RecyclerView,
  adapter, `LinearLayoutManager` và sự kiện các nút.
- `getStudent()` / `getProduct()`: đọc form, tạo đối tượng.
- `showStudent()` / `showProduct()`: đưa đối tượng đã chọn lên form.
- `validateData()` hoặc hàm tương đương: kiểm tra dữ liệu theo đề,
  báo lỗi bằng `TextInputLayout.setError()` và xóa lỗi khi hợp lệ.
- ViewHolder giữ các view của item. Adapter bind dữ liệu và gửi click về
  Activity qua interface callback nếu đề cần chọn dòng.
- Khi chọn dòng: kiểm tra vị trí hợp lệ, đổ dữ liệu lên form và lưu khóa
  của bản ghi đang chọn. Không chỉ dựa vào vị trí khi danh sách có thể đổi.
- Khi dùng Room: Entity có khóa chính; DAO khai báo thao tác cần thiết;
  RoomDB dùng singleton và application context. Ghi dữ liệu bằng
  `ExecutorService`, quan sát danh sách bằng `LiveData`.
- Khi đề yêu cầu chia sẻ: lấy nội dung trên form, dùng `Intent.ACTION_SEND`,
  `text/plain`, `Intent.EXTRA_TEXT` và `Intent.createChooser()`.

## 7. Ví dụ tham khảo: chọn ngày

Ví dụ minh họa dùng tên biến ngày sinh. Khi áp dụng phải đổi tên biến,
ID và tiêu đề theo đề; không sao chép cả đối tượng sinh viên sang bài khác.
XML dưới đây nằm trong root đã khai báo namespace `android` và `app`.

```xml
<com.google.android.material.textfield.TextInputLayout
    android:id="@+id/til_dob"
    style="@style/Widget.Material3.TextInputLayout.OutlinedBox"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:startIconDrawable="@android:drawable/ic_menu_day">

    <com.google.android.material.textfield.TextInputEditText
        android:id="@+id/tie_dob"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="@string/hint_dob"
        android:focusable="false" />
</com.google.android.material.textfield.TextInputLayout>
```

Khai báo `hint_dob` và `title_pick_dob` trong `strings.xml`.
Đoạn Java đặt trong Activity kế thừa `AppCompatActivity`:

```java
// Các import cần thiết:
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

// Thành viên của MainActivity:
private TextInputLayout tilDob;
private TextInputEditText tieDob;

private void mapping() {
    tilDob = findViewById(R.id.til_dob);
    tieDob = findViewById(R.id.tie_dob);
    // Ánh xạ các view còn lại của bài tại đây.
}

// Gọi sau mapping() trong onCreate(), sau setContentView():
private void setupDateInput() {
    tieDob.setOnClickListener(v -> showDatePickerDialog());
}

private void showDatePickerDialog() {
    MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.title_pick_dob)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build();

    datePicker.addOnPositiveButtonClickListener(selection -> {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        tieDob.setText(sdf.format(new Date(selection)));
        tilDob.setErrorEnabled(false);
    });
    datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
}
```

Phân biệt mẫu gốc và điều chỉnh kỹ thuật trong tài liệu:

| Nội dung | Phân loại |
|---|---|
| `focusable="false"`, click ô, MaterialDatePicker, chọn hôm nay, `setText()`, xóa lỗi | Cơ chế gốc BH03_V2_StudentsM |
| Formatter dùng UTC để khớp ngày UTC do MaterialDatePicker trả về, tránh lệch ngày ở múi giờ âm | Điều chỉnh kỹ thuật áp dụng trong ví dụ |
| Cỡ chữ `sp`, style widget OutlinedBox, chuỗi trong resources, nút rộng `0dp` + weight | Quy ước triển khai đã chuẩn hóa từ layout mẫu |
| `setKeyListener(null)`, click icon để mở lịch | Bổ sung tùy chọn, không bắt buộc để sao chép cơ chế gốc |
| Giới hạn ngày, mở lại đúng ngày đang có trong ô | Chỉ thêm khi đề/người dùng yêu cầu |

## 8. Đối chiếu đề sản phẩm TX2 hiện tại

Bảng này chỉ áp dụng cho đề quản lý sản phẩm đã cung cấp,
không mặc định áp dụng cho mọi đề TX2.

| Yêu cầu | Cách triển khai |
|---|---|
| Nhập liệu | Mã, tên, ngày sản xuất; ngày chọn bằng MaterialDatePicker |
| Bốn nút | THÊM, ĐỌC, CẬP NHẬT, CHIA SẺ |
| Danh sách | RecyclerView chiếm phần còn lại phía dưới |
| Click item | Đưa dữ liệu lên form, lưu bản ghi được chọn |
| THÊM | Thêm vào Room và cập nhật danh sách |
| ĐỌC | Đọc Room và hiển thị danh sách |
| CẬP NHẬT | Sửa tên/ngày của bản ghi được chọn, giữ khóa chính |
| CHIA SẺ | Chia sẻ tên và ngày sản xuất đang có trên form |

Không tự thêm xóa vì đề này không yêu cầu.

## 9. Checklist nghiệm thu cho agent

- [ ] Đã đọc đề và xác định đúng đối tượng, trường dữ liệu, chức năng.
- [ ] Đúng cấu trúc màn hình và item; ID XML khớp Java, đủ resources.
- [ ] Nội dung không bị system bars/bàn phím che; nút không cắt chữ.
- [ ] Danh sách chiếm phần còn lại nếu đề yêu cầu, item không chồng nội dung.
- [ ] Ô ngày không gõ trực tiếp; chạm mở lịch, xác nhận điền đúng ngày/định dạng,
      xóa lỗi; hủy giữ nguyên dữ liệu.
- [ ] Click item, các nút và lưu dữ liệu hoạt động đúng phạm vi đề.
- [ ] Chạy build và các kiểm tra phù hợp có sẵn; không coi `NO-SOURCE` là test pass.
- [ ] Báo rõ thay đổi, kết quả đã kiểm tra, lỗi và phần chưa chạy trên thiết bị.

Đây là checklist khi triển khai ứng dụng, không phải tuyên bố ứng dụng hiện tại
đã vượt qua các bước trên. Chỉ chỉnh tài liệu thì kiểm tra nội dung và diff,
không cần build lại ứng dụng.
