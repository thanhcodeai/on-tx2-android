# Quy ước mẫu thiết kế bài Android TX2

Tài liệu này ghi lại mẫu `activity_main.xml` và `student_item.xml` do người dùng cung cấp. Khi làm bài TX2, ưu tiên giữ cấu trúc và phong cách của mẫu; thay trường dữ liệu, nhãn nút và xử lý Java theo đề. Đây là quy ước của người dùng, không khẳng định mọi chi tiết là yêu cầu bắt buộc của giảng viên.

## 1. Công nghệ và cấu trúc thư mục

- Viết xử lý bằng **Java**, giao diện bằng **XML**.
- Dùng **Material 3**, **RecyclerView**; dùng **Room Database** khi đề yêu cầu lưu dữ liệu trên máy.
- Với bài nhỏ, đặt các lớp Java chung package như mẫu hiện tại; không tự thêm các tầng kiến trúc phức tạp.

```text
app/src/main/
├── AndroidManifest.xml
├── java/<package>/
│   ├── MainActivity.java
│   ├── Student.java
│   ├── StudentDAO.java
│   ├── StudentRoomDB.java
│   ├── StudentAdapter.java
│   └── StudentViewHolder.java
└── res/
    ├── layout/
    │   ├── activity_main.xml
    │   └── student_item.xml
    ├── drawable/
    │   └── ic_avt.xml hoặc ảnh avatar tương ứng
    └── values/
        ├── strings.xml
        ├── colors.xml
        └── themes.xml
```

`Student` là tên minh họa. Đề sản phẩm dùng `Product`, `ProductDAO`, `ProductRoomDB`, `ProductAdapter`, `ProductViewHolder` và `product_item.xml`. Không đưa avatar hoặc trường sinh viên vào bài sản phẩm nếu đề không cần.

## 2. Mẫu activity_main.xml

Giữ thứ tự bố trí từ trên xuống:

```text
ConstraintLayout (main, margin 16dp)
├── MaterialTextView (mtv_title)
├── MaterialCardView (mcv_edt)
│   └── LinearLayout dọc (margin 6dp)
│       ├── TextInputLayout (til_name)
│       │   └── TextInputEditText (tie_name)
│       ├── TextInputLayout (til_dob, icon lịch)
│       │   └── TextInputEditText (tie_dob)
│       ├── TextInputLayout (til_mobie, icon điện thoại)
│       │   └── TextInputEditText (tie_mobie)
│       └── TextInputLayout (til_email, icon email)
│           └── TextInputEditText (tie_email)
├── LinearLayout ngang (lo_btn)
│   └── Các MaterialButton
└── RecyclerView (rcv_students)
```

### Khung và tiêu đề

- Root: `androidx.constraintlayout.widget.ConstraintLayout`, ID `main`, rộng/cao `match_parent`, margin `16dp`.
- Tiêu đề: `MaterialTextView`, ID `mtv_title`, cao `wrap_content`, padding dưới `8dp`, chữ `20sp`.
- Tiêu đề constraint vào cạnh trên, trái, phải của root.
- Dùng `sp` cho cỡ chữ; mẫu gốc ghi `20dp`, quy ước dùng `20sp` để hỗ trợ cài đặt cỡ chữ của thiết bị.

### Card nhập liệu

- `MaterialCardView`, ID `mcv_edt`, rộng `match_parent`, cao `wrap_content`.
- `app:cardCornerRadius="8dp"`, `app:cardElevation="1dp"`.
- Constraint phía trên vào dưới `mtv_title`, hai bên vào parent.
- Bên trong là `LinearLayout` dọc, rộng `match_parent`, cao `wrap_content`, margin `6dp`.
- Mỗi trường gồm `TextInputLayout` bọc `TextInputEditText`; không thay toàn bộ form bằng các `EditText` rời.
- Mẫu gốc dùng style `ThemeOverlay.Material3.AutoCompleteTextView.OutlinedBox`. Khi triển khai ô nhập văn bản, dùng style widget đúng loại `@style/Widget.Material3.TextInputLayout.OutlinedBox` để thể hiện ô có viền.
- Thêm `app:startIconDrawable` cho trường cần icon, theo ý nghĩa dữ liệu.
- **Mọi trường liên quan đến ngày/tháng/năm đều phải cho người dùng chọn bằng bộ chọn ngày, không cho nhập tay** (ngày sinh, ngày sản xuất, ngày hết hạn và các trường ngày khác).
- **Dùng `MaterialDatePicker<Long>` theo project `BH03_V2_StudentsM`**, import `com.google.android.material.datepicker.MaterialDatePicker`; tạo bằng `MaterialDatePicker.Builder.datePicker()` trong hàm riêng `showDatePickerDialog()`. Tên hàm này không có nghĩa là dùng lớp `android.app.DatePickerDialog`.
- Ô ngày dùng `TextInputEditText` bên trong `TextInputLayout`, có icon lịch `@android:drawable/ic_menu_day` và `android:focusable="false"` như project mẫu. Bổ sung `android:clickable="true"` và `setKeyListener(null)` để chặn nhập tay; không chỉ đặt `inputType="date"`.
- Trong `onCreate()`, sau `mapping()`, đăng ký click cho ô ngày để gọi `showDatePickerDialog()`. Nếu icon lịch cũng cho phép nhấn, nối `setStartIconOnClickListener()` vào cùng hàm.
- Đặt tiêu đề theo trường, ví dụ `CHỌN NGÀY SINH` hoặc `CHỌN NGÀY SẢN XUẤT`; mặc định chọn hôm nay bằng `MaterialDatePicker.todayInUtcMilliseconds()` **đúng như project mẫu**.
- Dùng `addOnPositiveButtonClickListener()` để nhận ngày sau khi xác nhận, định dạng `dd/MM/yyyy`, gọi `tieDob.setText(...)` và `tilDob.setErrorEnabled(false)` để xóa báo lỗi. Hủy hoặc đóng lịch không thay đổi ô ngày.
- Mở dialog bằng `datePicker.show(getSupportFragmentManager(), "DATE_PICKER")`.
- Project mẫu định dạng bằng `SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())` và `new Date(selection)`. Khi áp dụng, đặt timezone của formatter là UTC vì `MaterialDatePicker` trả ngày theo UTC, tránh lệch ngày trên thiết bị ở múi giờ âm. Đây là bổ sung kỹ thuật cho mẫu, không phải dòng có sẵn trong project.
- Chỉ thêm giới hạn ngày khi nghiệp vụ của đề yêu cầu; project mẫu chưa đặt giới hạn ngày.

### Mẫu code chọn ngày theo BH03_V2_StudentsM

XML của trường ngày (dùng cùng style ô nhập đã quy định ở trên):

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
        android:hint="Ngày sinh"
        android:focusable="false"
        android:clickable="true" />
</com.google.android.material.textfield.TextInputLayout>
```

Java trong `MainActivity` (khai báo và ánh xạ `tilDob`, `tieDob` trong `mapping()`):

```java
import com.google.android.material.datepicker.MaterialDatePicker;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

// Trong onCreate(), sau mapping():
// tieDob.setKeyListener(null);
// tieDob.setOnClickListener(v -> showDatePickerDialog());
// tilDob.setStartIconOnClickListener(v -> showDatePickerDialog());

private void showDatePickerDialog() {
    MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("CHỌN NGÀY SINH")
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
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

`INPUT_MODE_CALENDAR` đặt chế độ mở ban đầu là lịch; `MaterialDatePicker` tiêu chuẩn vẫn có nút chuyển sang nhập văn bản. Không hiểu thuộc tính này là đã khóa mọi chế độ nhập tay. Khi áp dụng yêu cầu chỉ chọn ngày, cần vô hiệu hóa hoặc ẩn nút chuyển chế độ bằng cách phù hợp với phiên bản Material đang dùng, đồng thời kiểm tra người dùng không thể chuyển sang nhập văn bản. Project mẫu chưa xử lý phần khóa chuyển chế độ này.

Với bài sản phẩm, đổi `tilDob`/`tieDob` thành biến tương ứng ngày sản xuất và đổi tiêu đề dialog; giữ nguyên luồng mở lịch → xác nhận → định dạng → hiển thị → xóa lỗi.

### Hàng nút

- `LinearLayout` ngang, ID `lo_btn`, đặt dưới `mcv_edt`.
- Dùng `MaterialButton`, style `@style/Widget.Material3.Button.TextButton`.
- Các nút có `layout_weight="1"`; quy ước dùng `layout_width="0dp"` để chia đều chiều rộng. Mẫu gốc dùng `wrap_content` kết hợp weight.
- Giữ hàng nút riêng bên ngoài card nhập liệu. Số nút và nhãn theo đề; với nhiều nút hoặc nhãn dài, có thể chia thành hai hàng để tránh chữ bị cắt.

### Danh sách

- `RecyclerView` đặt dưới hàng nút, hai cạnh constraint vào parent.
- Mẫu gốc đặt chiều cao `wrap_content` và chỉ constraint phía trên.
- **Khi đề yêu cầu danh sách chiếm phần màn hình còn lại**, dùng chiều cao `0dp`, constraint trên vào đáy `lo_btn` và dưới vào đáy parent:

```xml
<androidx.recyclerview.widget.RecyclerView
    android:id="@+id/rcv_students"
    android:layout_width="0dp"
    android:layout_height="0dp"
    app:layout_constraintTop_toBottomOf="@id/lo_btn"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintEnd_toEndOf="parent" />
```

- Không bọc cả `RecyclerView` trong `ScrollView`. Chỉ bổ sung cuộn cho khu nhập liệu khi cần hỗ trợ màn hình nhỏ và vẫn bảo đảm vùng danh sách có diện tích sử dụng.

## 3. Mẫu student_item.xml

```text
MaterialCardView (rộng match_parent, cao wrap_content)
└── ConstraintLayout (margin 4dp)
    ├── ImageView (img_avt, bên trái)
    ├── TextView (tv_name, bên phải ảnh, hàng trên)
    └── TextView (tv_mobie, bên phải ảnh, dưới tên)
```

- Card: bo góc `8dp`, elevation `1dp`.
- `ImageView`: kích thước `60dp × 60dp`, padding `3dp`, ảnh `@drawable/ic_avt`, constraint đầu và trên vào parent.
- `tv_name`: rộng `0dp`, cao `wrap_content`, padding `3dp`; đầu nối cuối ảnh, cuối nối parent, trên nối parent.
- `tv_mobie`: rộng `0dp`, cao `wrap_content`, padding `3dp`; đầu nối cuối ảnh, cuối nối parent, trên nối dưới `tv_name`.
- Giữ tên `mobie` khi làm tiếp mẫu đã cung cấp để tương thích Java/XML hiện có; không tự đổi ID sang `mobile` ở riêng một phía.
- Khi thêm trường, tiếp tục xếp các `TextView` bằng constraint, tránh chồng nội dung.
- Để chiều cao item bao trọn cả ảnh và văn bản, bổ sung constraint đáy phù hợp cho ảnh và chuỗi nội dung; kiểm tra tên dài, nhiều dòng.
- Nếu avatar chỉ trang trí, đặt `android:importantForAccessibility="no"`; nếu mang thông tin, cung cấp `contentDescription` phù hợp.
- Adapter phải inflate đúng file item của đối tượng đang quản lý.

## 4. Quy ước đặt tên

Tên file XML và ID dùng `snake_case`; tên lớp Java dùng `PascalCase`; biến và hàm Java dùng `camelCase`.

| Thành phần | Tiền tố ID | Ví dụ |
|---|---|---|
| MaterialTextView tiêu đề | `mtv_` | `mtv_title` |
| MaterialCardView | `mcv_` | `mcv_edt` |
| TextInputLayout | `til_` | `til_name` |
| TextInputEditText | `tie_` | `tie_name` |
| MaterialButton | `mbtn_` | `mbtn_add`, `mbtn_read` |
| LinearLayout | `lo_` | `lo_btn` |
| RecyclerView | `rcv_` | `rcv_students`, `rcv_products` |
| ImageView | `img_` | `img_avt` |
| TextView trong item | `tv_` | `tv_name`, `tv_mobie` |

Dùng `@+id/...` khi khai báo ID của view; dùng `@id/...` khi tham chiếu view đã khai báo. Với code cũ đang hoạt động, đổi tên phải đồng bộ XML và Java, chỉ thực hiện khi có yêu cầu chỉnh sửa.

## 5. Quy ước xử lý Java

- `MainActivity`: ánh xạ giao diện trong `mapping()`, thiết lập RecyclerView và đăng ký sự kiện.
- `getStudent()` / `getProduct()`: đọc form và tạo đối tượng.
- `showStudent()` / `showProduct()`: đưa đối tượng được chọn lên form.
- Entity: khai báo các trường và khóa chính bằng annotation Room.
- DAO: khai báo truy vấn và thao tác dữ liệu đúng yêu cầu đề.
- RoomDB: tạo database dùng chung theo singleton, sử dụng application context.
- ViewHolder: giữ tham chiếu các view của một item.
- Adapter: inflate item, bind dữ liệu, trả sự kiện click về Activity qua interface callback.
- Khi click dòng: lấy vị trí hợp lệ, hiển thị dữ liệu lên form và lưu khóa chính của bản ghi đang chọn. Không chỉ dựa vào vị trí vì danh sách có thể đổi thứ tự.
- Ghi Room trên luồng nền bằng `ExecutorService`; quan sát danh sách bằng `LiveData` để cập nhật RecyclerView.
- Kiểm tra dữ liệu rỗng, ngày đã được chọn hợp lệ và khóa trùng theo nghiệp vụ. Các trường ngày phải dùng bộ chọn ngày, không nhận dữ liệu nhập tay.
- Nếu đề yêu cầu chia sẻ: lấy thông tin đang có trên form, dùng `Intent.ACTION_SEND`, MIME `text/plain`, `Intent.EXTRA_TEXT` và `Intent.createChooser()`.

## 6. Áp dụng cho đề quản lý sản phẩm TX2

| Thành phần | Nội dung theo đề |
|---|---|
| Tiêu đề | Quản lý sản phẩm cửa hàng |
| Form trong card | Mã sản phẩm, tên sản phẩm, ngày sản xuất (chọn bằng bộ chọn ngày, không nhập tay) |
| Nút | THÊM, ĐỌC, CẬP NHẬT, CHIA SẺ |
| RecyclerView | Danh sách sản phẩm, chiếm phần còn lại phía dưới |
| Item | Card chứa thông tin mã, tên, ngày sản xuất; ảnh chỉ thêm khi cần |
| Click item | Đổ dữ liệu lên form, lưu bản ghi được chọn |
| THÊM | Ghi sản phẩm mới vào Room, cập nhật danh sách |
| ĐỌC | Đọc dữ liệu Room và hiển thị danh sách |
| CẬP NHẬT | Sửa tên/ngày của bản ghi đang chọn, giữ khóa chính |
| CHIA SẺ | Chia sẻ tên và ngày sản xuất đang hiển thị trên form |

## 7. Cách dùng quy ước cho lần sau

Có thể yêu cầu: **“Làm bài TX2 theo QUY_UOC_TX2.md, giữ mẫu ConstraintLayout + MaterialCardView nhập liệu + hàng MaterialButton + RecyclerView và item dùng ConstraintLayout.”**

Đọc đề trước khi triển khai; quy ước định hướng cách tổ chức và trình bày, còn số trường, số nút, ảnh và nghiệp vụ phải theo đề. Tài liệu này không tự thay đổi layout hiện tại của repository.
