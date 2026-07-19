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
 // Duyệt hoặc Từ chối giải chạy
    public boolean updateRaceStatus(String raceId, String status) {
        String sql = "UPDATE Races SET status = ? WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, status);
            st.setString(2, raceId);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

   // Sửa tham số truyền vào: Nhận 'registrationId' thay vì raceId và bib
public boolean insertRaceResult(String registrationId, String finishTime) {
    // Sửa đúng tên bảng RaceResult và các cột registrationId, finishTime, status
    String sql = "INSERT INTO RaceResult (registrationId, finishTime, status) VALUES (?, ?, 'FINISHED')";
    try (PreparedStatement st = connection.prepareStatement(sql)) {
        st.setInt(1, Integer.parseInt(registrationId));
        st.setString(2, finishTime); // Định dạng hh:mm:ss khớp với kiểu TIME trong SQL
        return st.executeUpdate() > 0;
    } catch (SQLException | NumberFormatException e) {
        e.printStackTrace();
    }
    return false;
}

    // Đồng bộ và tính toán lại thứ hạng (Ranking) tự động dựa trên finish_time tăng dần
    public void calculateRankings(String raceId) {
        String sql = "WITH RankedResults AS (" +
                     "  SELECT id, DENSE_RANK() OVER (ORDER BY finish_time ASC) as new_rank " +
                     "  FROM RaceResults WHERE race_id = ? AND status = 'Finished'" +
                     ") " +
                     "UPDATE RaceResults " +
                     "SET ranking = RankedResults.new_rank " +
                     "FROM RaceResults " +
                     "INNER JOIN RankedResults ON RaceResults.id = RankedResults.id";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, raceId);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
