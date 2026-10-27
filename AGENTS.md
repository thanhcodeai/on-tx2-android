# Hướng dẫn cho agent Codex

Trước khi làm bài TX2 hoặc chỉnh giao diện Android, đọc toàn bộ
`QUY_UOC_TX2.md` trong thư mục gốc repository.

## Thứ tự ưu tiên

1. Yêu cầu trực tiếp của người dùng.
2. Đề bài kiểm tra được cung cấp.
3. Quy định trong `QUY_UOC_TX2.md`.
4. Code ví dụ tham khảo.

Nếu có mâu thuẫn ảnh hưởng đến kết quả mà không thể giải quyết theo
thứ tự trên, hỏi người dùng.

## Cách áp dụng

- Giữ cấu trúc layout và cách đặt tên theo quy ước.
- Đổi đối tượng, trường dữ liệu, nhãn nút và chức năng theo đề.
- Không tự thêm chức năng hoặc kiến trúc ngoài phạm vi đề.
- Trường ngày dùng `MaterialDatePicker` theo mẫu `BH03_V2_StudentsM`;
  không cho gõ trực tiếp vào ô ngày trên form.
- Phân biệt mẫu gốc với các bổ sung kỹ thuật trong tài liệu.
- Không mặc định code ví dụ là yêu cầu bắt buộc.

## Kiểm tra kết quả

- Đối chiếu giao diện và từng chức năng với đề bài.
- Chạy build và các kiểm tra phù hợp có sẵn.
- Báo rõ kết quả, lỗi còn lại và phần chưa kiểm thử.
