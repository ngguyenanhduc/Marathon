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
public class RaceDAL extends DBContext{
    // 2. Duyệt/từ chối giải chạy do organizer tạo (APPROVED / REJECTED)
    public boolean reviewRace(int raceId, int adminId, String status) {
        String sql = "UPDATE Race SET approvedByUserId = ?, status = ?, approvedDate = GETDATE() WHERE raceId = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, adminId);
            st.setString(2, status);
            st.setInt(3, raceId);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
