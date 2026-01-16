package org.example.constant;

public interface AppError {

    // DAO
    String NOTFOUND_LIBRARY_MYSQL = "0001 ERROR: Không tìm thấy thư viện MySQL JDBC Driver!";
    String CONNECT_ERROR_MYSQL = "0002 ERROR: Không thể kết nối đến Database!";
    String ERROR_EXITS = " ERROR: Dữ liệu đã tồn tại"; // Mình sửa lại chút cho đúng format

    // AUTHSERVICE
    String REGISTER_USERNAME_SHORT = "0003 ERROR: Tên đăng nhập phải từ 5 ký tự trở lên!";
    String REGISTER_USERNAME_EXISTS = "0004 ERROR: Tên đăng nhập đã tồn tại!";
    String REGISTER_USERNAME_INVALID_CHARS = "0008 ERROR: Tên đăng nhập không được chứa ký tự đặc biệt!"; // Mới

    //AUTHSERVICE : PASSWORD
    String REGISTER_PASSWORD_SHORT = "0005 ERROR: Mật khẩu phải từ 6 ký tự trở lên!";
    String REGISTER_PASSWORD_NO_UPPER = "0009 ERROR: Mật khẩu phải chứa ít nhất 1 chữ hoa!"; // Mới
    String REGISTER_PASSWORD_NO_DIGIT = "0010 ERROR: Mật khẩu phải chứa ít nhất 1 chữ số!"; // Mới

    // AUTH SERVICE : EMAIL
    String REGISTER_EMAIL_INVALID = "0006 ERROR: Email không đúng định dạng!";
    String REGISTER_EMAIL_EXISTS = "0007 ERROR: Email đã được sử dụng!";

    //AUTH SERVICE:
    String REGISTER_PHONE_INVALID = "0011 ERROR: Số điện thoại phải bao gồm 10 chữ số!";

    //AUTH SERVICE
    String REGISTER_FULLNAME_EMPTY = "0013 ERROR: Vui lòng nhập họ và tên!";
}