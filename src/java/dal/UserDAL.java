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
    public boolean handleOrganizerRequest(int requestId, int adminId, String status) {
        String updateRequestSql = "UPDATE OrganizerRequest SET approverId = ?, status = ?, approvedDate = GETDATE() WHERE requestId = ?";
        String updateUserRoleSql = "UPDATE Users SET roleId = 2 WHERE userId = (SELECT requesterId FROM OrganizerRequest WHERE requestId = ?)";
        
        try {
            // Tắt auto commit để thực hiện transaction đồng thời 2 bảng
            connection.setAutoCommit(false);

            // Cập nhật đơn yêu cầu
            PreparedStatement st1 = connection.prepareStatement(updateRequestSql);
            st1.setInt(1, adminId);
            st1.setString(2, status);
            st1.setInt(3, requestId);
            st1.executeUpdate();

            // Nếu admin bấm duyệt ('APPROVED'), tiến hành nâng quyền cho user đó luôn
            if ("APPROVED".equals(status)) {
                PreparedStatement st2 = connection.prepareStatement(updateUserRoleSql);
                st2.setInt(1, requestId);
                st2.executeUpdate();
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }

    // 3. Quản lý user: ACTIVE, INACTIVE, BANNED
    public boolean updateUserStatus(int userId, String newStatus) {
        String sql = "UPDATE Users SET status = ? WHERE userId = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, newStatus);
            st.setInt(2, userId);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
