package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Role;

/**
 * Author: PhucNTHE173021
 *
 * Version: 1
 *
 * Last Update Date: 17/7/2026
 *
 * Purpose: truy van thong tin role de phuc vu dang ky tai khoan
 */
public class RoleDAO extends DBContext {

    //lay role theo ten de khong phai hard-code roleId trong servlet
    public Role getRoleByName(String roleName) {
        String sql = """
            SELECT roleId, roleName
            FROM Roles
            WHERE roleName = ?
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, roleName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Role(
                            resultSet.getInt("roleId"),
                            resultSet.getString("roleName"));
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Khong the lay thong tin role.", exception);
        }

        return null;
    }
}
