# TX2 - Quản lý sản phẩm cửa hàng

Project độc lập, Java + XML, Room Database, RecyclerView, Material 3.
Mở thư mục TX2_QuanLySanPham bằng Android Studio rồi Sync và Run app.
Project B4V4_RoomDB bên ngoài được giữ nguyên.

## Cách dùng
- Nhập mã duy nhất, tên, ngày sản xuất theo dd/MM/yyyy rồi nhấn THÊM.
- ĐỌC tải lại danh sách từ Room. Danh sách cũng tự cập nhật khi thêm/sửa.
- Chạm sản phẩm để đưa dữ liệu lên form và ghi nhớ mã đang chọn.
- Giữ nguyên mã, sửa tên/ngày rồi nhấn CẬP NHẬT.
- CHIA SẺ gửi tên và ngày đang có trên form qua trình chọn ứng dụng.
- Không có chức năng xóa vì đề chỉ yêu cầu bốn nút trên.

## Cấu trúc theo mẫu thầy
Product → ProductDAO → ProductRoomDB; ProductViewHolder + ProductAdapter;
MainActivity dùng mapping(), getProduct(), showProduct(), callback chọn dòng,
ExecutorService ghi dữ liệu và LiveData quan sát danh sách.

## Kiểm tra thủ công
1. Thêm SP01, Bánh quy, 20/10/2026: xuất hiện trong danh sách.
2. Thêm lại SP01: báo trùng, không ghi đè.
3. Thử bỏ trống hoặc nhập 31/02/2026: báo lỗi.
4. Chọn SP01, sửa tên rồi cập nhật: một bản ghi được sửa.
5. Cập nhật khi chưa chọn hoặc đổi mã đang chọn: báo lỗi.
6. Đóng/mở app, nhấn ĐỌC: dữ liệu vẫn còn.
7. Chọn sản phẩm, nhấn CHIA SẺ: kiểm tra tên và ngày trong nội dung.
8. Xoay màn hình sau khi chọn rồi cập nhật: vẫn sửa đúng sản phẩm.