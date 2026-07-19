package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.User;

public class UserDAO extends DBContext {

    public User getUserByUsername(String username) {
        String sql = """
            SELECT
                u.userId,
                u.userName,
                u.password,
                u.roleId,
                r.roleName,
                u.fullName,
                u.email,
                u.phone,
                u.status,
                u.createdAt
            FROM Users u
            JOIN Roles r
                ON u.roleId = r.roleId
            WHERE u.userName = ?
            """;

        try (PreparedStatement statement =
                connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet =
                    statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }

        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Không thể lấy người dùng theo username.",
                    exception
            );
        }

        return null;
    }

    public User getUserById(int userId) {
        String sql = """
            SELECT
                u.userId,
                u.userName,
                u.password,
                u.roleId,
                r.roleName,
                u.fullName,
                u.email,
                u.phone,
                u.status,
                u.createdAt
            FROM Users u
            JOIN Roles r
                ON u.roleId = r.roleId
            WHERE u.userId = ?
            """;

        try (PreparedStatement statement =
                connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet =
                    statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }

        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Không thể lấy người dùng theo ID.",
                    exception
            );
        }

        return null;
    }

    private User mapUser(ResultSet resultSet)
            throws SQLException {

        User user = new User();

        user.setUserId(
                resultSet.getInt("userId")
        );

        user.setUserName(
                resultSet.getString("userName")
        );

        user.setPassword(
                resultSet.getString("password")
        );

        user.setRoleId(
                resultSet.getInt("roleId")
        );

        user.setRoleName(
                resultSet.getString("roleName")
        );

        user.setFullName(
                resultSet.getString("fullName")
        );

        user.setEmail(
                resultSet.getString("email")
        );

        user.setPhone(
                resultSet.getString("phone")
        );

        user.setStatus(
                resultSet.getString("status")
        );

        if (resultSet.getTimestamp("createdAt") != null) {
            user.setCreatedAt(
                    resultSet
                            .getTimestamp("createdAt")
                            .toLocalDateTime()
            );
        }

        return user;
    }
}