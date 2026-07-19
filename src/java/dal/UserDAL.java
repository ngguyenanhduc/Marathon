/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 *
 * @author anhdu
 */
public class UserDAL extends DBContext{
    
 // Duyệt/Từ chối quyền Organizer (Cập nhật Role và Status)
    public boolean updateUserRoleAndStatus(String userId, String role, String status) {
        String sql = "UPDATE Users SET role = ?, status = ? WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, role);
            st.setString(2, status);
            st.setString(3, userId);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật trạng thái người dùng (Active, Inactive, Banned)
    public boolean updateUserStatus(String userId, String status) {
        String sql = "UPDATE Users SET status = ? WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, status);
            st.setString(2, userId);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    }

