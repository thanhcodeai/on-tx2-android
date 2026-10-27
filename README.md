# Bài TX2 – Quản lý sách

Đề được cung cấp trực tiếp trong tin nhắn: Java + XML, Material 3, ba trường
mã sách / tên sách / ngày xuất bản; bốn nút THÊM / ĐỌC / XÓA / CHIA SẺ;
RecyclerView hiển thị đủ ba thông tin, click đưa lên form, dữ liệu lưu Room.

## Triển khai
- Giữ package com.example.ontx22 và toolchain project mới.
- Book, BookDAO, BookRoomDB, BookAdapter, BookViewHolder cùng package.
- Mã sách là khóa chính. Mã trùng được báo lỗi, không ghi đè sách cũ.
- Room singleton dùng application context; ghi bằng ExecutorService, danh sách LiveData.
- ĐỌC gắn lại truy vấn Room, chỉ giữ một observer.
- XÓA theo khóa bản ghi đã chọn, không theo nội dung mã bị sửa trên form.
- CHIA SẺ tên và ngày trên form qua ACTION_SEND, text/plain và chooser.
- Ô ngày không cho gõ trực tiếp. MaterialDatePicker mặc định hôm nay,
  định dạng dd/MM/yyyy theo UTC; hủy giữ nguyên dữ liệu, không giới hạn ngày.
- Không có nút cập nhật vì đề sách không yêu cầu.

## Chạy
Mở project bằng Android Studio, Sync Gradle, chọn emulator/điện thoại API 24 trở lên,
chọn cấu hình app và nhấn Run. Dùng Gradle JDK đi kèm Android Studio.

Build và kiểm tra:
    .\gradlew.bat assembleDebug lintDebug testDebugUnitTest

Nếu terminal chưa có Java, đặt JAVA_HOME tới thư mục jbr của Android Studio.
APK: app/build/outputs/apk/debug/app-debug.apk

## Kiểm tra thủ công
1. Mở app: kiểm tra ba ô, bốn nút, danh sách; chữ tiếng Việt, sáng/tối,
   tên sách dài, màn hình nhỏ và khi bàn phím hiện.
2. Bấm THÊM khi trống: từng trường cần báo lỗi.
3. Chạm ô ngày: không hiện bàn phím nhập trực tiếp; chọn ngày và xác nhận,
   kiểm tra dd/MM/yyyy. Mở lại mặc định hôm nay; hủy không đổi giá trị.
4. Nhập S01, tên sách, ngày; THÊM: sách xuất hiện với đủ ba thông tin.
   Thêm lại S01: báo trùng, bản ghi cũ không bị thay đổi.
5. Thêm S02; bấm ĐỌC. Đóng/mở lại app và ĐỌC: dữ liệu vẫn còn.
6. Chọn S01: cả ba trường điền đúng. Sửa mã trên form thành S02 rồi XÓA:
   chỉ S01 bị xóa, S02 vẫn còn. XÓA khi chưa chọn phải báo chọn sách.
7. Chọn S02, xoay màn hình, XÓA: vẫn dùng đúng khóa bản ghi được chọn.
8. CHIA SẺ khi thiếu tên/ngày: báo lỗi. Khi hợp lệ: chooser mở, chọn ứng dụng
   và kiểm tra nội dung tên sách, ngày xuất bản. Hủy chooser quay lại app.

Test mẫu mặc định chỉ kiểm tra phép cộng, không kiểm chứng nghiệp vụ.
Các bước giao diện, Room qua vòng đời app và chia sẻ cần chạy trên thiết bị.
