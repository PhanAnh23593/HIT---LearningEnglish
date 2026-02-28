# 🎓 HIT - Learning English Application



Một ứng dụng học tiếng Anh Desktop chuyên nghiệp được xây dựng bằng **JavaFX** và **MySQL**, thiết kế dành riêng cho sinh viên các chuyên ngành (CNTT, Kế toán, Y Dược,...) để trau dồi từ vựng và kỹ năng phát âm.



---



## 🚀 Tính năng nổi bật (Features)



* 🔐 **Hệ thống Tài khoản Bảo mật:** Đăng nhập, Đăng ký với mật khẩu được mã hóa an toàn bằng **BCrypt**. Phân quyền User và Admin rõ ràng.

* 📚 **Học Từ Vựng Theo Chuyên Ngành:** Cung cấp kho từ vựng phong phú chia theo các ngành (CNTT, Kinh tế, Y Dược,...).

* 🎧 **Luyện Nghe (Listening):** Tích hợp Google TTS (Text-to-Speech) để nghe phát âm chuẩn và giả lập luyện đọc.

* 📝 **Hệ thống Bài Kiểm Tra (Testing):** * Các dạng câu hỏi đa dạng (Trắc nghiệm từ, Nghe âm thanh đoán từ, Điền câu).

* Chấm điểm tự động và phân loại kết quả.

* 🧠 **Thuật Toán Lặp Lại Ngắt Quãng (Spaced Repetition):** * Theo dõi tiến độ học tập của từng người dùng.

* Tự động thăng cấp từ vựng lên "Đã thuộc" (Status 3) nếu trả lời đúng trong bài Test.

* Yêu cầu học lại (Status 0) nếu trả lời sai.

* 📊 **Dashboard Thống Kê:** Bảng danh sách sinh động, cho người dùng theo dõi tiến độ học tập.



---



## 🛠 Công nghệ sử dụng (Tech Stack)



* **Ngôn ngữ:** Java 21

* **Giao diện (UI):** JavaFX 21, FXML, CSS

* **Cơ sở dữ liệu:** MySQL

* **Công cụ quản lý:** Maven

* **Bảo mật & Thư viện phụ trợ:** jBcrypt (Mã hóa mật khẩu), Google TTS API, JavaFX Media (Xử lý âm thanh).



---



## ⚙️ Hướng dẫn Cài đặt & Chạy dự án (Local Setup)



### 1. Yêu cầu hệ thống (Prerequisites)

* JDK 21 hoặc mới hơn.

* MySQL Workbench (hoặc XAMPP).

* IntelliJ IDEA (Khuyên dùng) hoặc Eclipse.



### 2. Thiết lập Cơ sở dữ liệu (Database Setup)

1. Mở MySQL/XAMPP.

2. Chạy toàn bộ file script SQL đính kèm trong thư mục dự án (hoặc copy kịch bản tạo DB) để tự động tạo cơ sở dữ liệu `HIT_Learning_English` và nạp sẵn 105 từ vựng.

3. Thay đổi thông tin kết nối Database trong code (file `DatabaseConnection.java`):

```java

String url = "jdbc:mysql://localhost:3306/HIT_Learning_English";
String user = "root";
String password = "password";