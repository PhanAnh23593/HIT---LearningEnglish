package org.example.dao;

import org.example.constant.AppError;
import org.example.model.User;
import org.example.utils.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public boolean checkUsername(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean checkEmail(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean register(User user) {
        if (checkUsername(user.getUsername())) {
            System.out.println("Username " + AppError.ERROR_EXITS);
            return false;
        }
        if (checkEmail(user.getEmail())) {
            System.out.println("Email " + AppError.ERROR_EXITS);
            return false;
        }

        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        String sql = "INSERT INTO users (username, email, password, full_name, phone_number, role, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, hashedPassword);
            pstmt.setString(4, user.getFullName());
            pstmt.setString(5, user.getPhoneNumber());
            pstmt.setString(6, "USER");
            pstmt.setInt(7, 1);

            int rowAffected = pstmt.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND status = 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");

                    if (BCrypt.checkpw(password, storedHash)) {
                        User user = new User();
                        user.setId(rs.getInt("id"));
                        user.setUsername(rs.getString("username"));
                        user.setPassword(storedHash);
                        user.setFullName(rs.getString("full_name"));
                        user.setEmail(rs.getString("email"));
                        user.setAvatar(rs.getString("avatar"));
                        user.setFirstlogin(rs.getBoolean("first_login"));
                        user.setMajor(rs.getString("major"));
                        if(rs.getDate("birthday")!= null){
                            user.setBirthday(rs.getDate("birthday").toLocalDate());
                        }
                        user.setRole(rs.getString("role"));
                        user.setStatus(rs.getInt("status"));
                        user.setPhoneNumber(rs.getString("phone_number"));
                        java.sql.Timestamp createTs = rs.getTimestamp("created_at");
                        if (createTs != null) {
                            user.setCreatedAt(createTs.toLocalDateTime());
                        }
                        java.sql.Timestamp updateTs = rs.getTimestamp("updated_at");
                        if (updateTs != null) {
                            user.setUpdateAt(updateTs.toLocalDateTime());
                        }
                        java.sql.Timestamp lastLoginTs = rs.getTimestamp("last_login");
                        if (lastLoginTs != null) {
                            user.setLastLogin(lastLoginTs.toLocalDateTime());
                        }
                        if (rs.getDate("last_learning_date") != null) {
                            user.setLastLearningDate(rs.getDate("last_learning_date").toLocalDate());
                        }
                        LastLogin(user.getId());
                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean changePassword(int userId, String newHashedPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newHashedPassword);
            pstmt.setInt(2, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




    private void LastLogin(int userId) {
        String sql = "UPDATE users SET last_login = NOW() WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public boolean FirstLogin(int userId, String avatar, LocalDate birthday, String major) {
        String sql = "UPDATE users SET avatar = ?, birthday = ?, major = ?, first_login = 0 WHERE id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, avatar);

            if (birthday != null) {
                ps.setDate(2, Date.valueOf(birthday));
            } else {
                ps.setNull(2, java.sql.Types.DATE);
            }

            ps.setString(3, major);

            ps.setInt(4, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




    public void updateLastLearningDate(int userId) {
        String sql = "UPDATE users SET last_learning_date = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public boolean updateUserProfile(User user) {
        String sql = "UPDATE users SET full_name = ?, phone_number = ?, email = ?, birthday = ?, avatar = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getPhoneNumber());
            pstmt.setString(3, user.getEmail());

            if (user.getBirthday() != null) {
                pstmt.setDate(4, Date.valueOf(user.getBirthday()));
            } else {
                pstmt.setNull(4, java.sql.Types.DATE);
            }

            pstmt.setString(5, user.getAvatar());
            pstmt.setInt(6, user.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT id, username, email, role FROM users";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }
}