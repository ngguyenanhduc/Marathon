package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.User;

/**
 * Author: PhucNTHE173021
 *
 * Version: 1
 *
 * Last Update Date: 17/7/2026
 *
 * Purpose: truy van tai khoan va cap nhat thong tin nguoi dung
 */
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

    //cac method Auth va Runner do PhucNTHE173021 bo sung

    //kiem tra username da duoc su dung hay chua
    public boolean usernameExists(String username) {
        String sql = "SELECT 1 FROM Users WHERE userName = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Khong the kiem tra username.", exception);
        }
    }

    //kiem tra email co thuoc mot tai khoan khac hay khong
    public boolean emailExists(String email, Integer excludedUserId) {
        String sql = """
            SELECT 1
            FROM Users
            WHERE email = ?
              AND (? IS NULL OR userId <> ?)
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);

            if (excludedUserId == null) {
                statement.setNull(2, java.sql.Types.INTEGER);
                statement.setNull(3, java.sql.Types.INTEGER);
            } else {
                statement.setInt(2, excludedUserId);
                statement.setInt(3, excludedUserId);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Khong the kiem tra email.", exception);
        }
    }

    //tao tai khoan Runner sau khi servlet da validate du lieu
    public boolean createRunner(User user, int runnerRoleId) {
        String sql = """
            INSERT INTO Users (
                userName,
                password,
                roleId,
                fullName,
                email,
                phone,
                status,
                createdAt
            )
            VALUES (?, ?, ?, ?, ?, ?, 'ACTIVE', GETDATE())
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getPassword());
            statement.setInt(3, runnerRoleId);
            statement.setString(4, user.getFullName());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getPhone());

            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Khong the tao tai khoan Runner.", exception);
        }
    }

    //cap nhat ho ten, email va so dien thoai cua Runner
    public boolean updateProfile(User user) {
        String sql = """
            UPDATE Users
            SET fullName = ?,
                email = ?,
                phone = ?
            WHERE userId = ?
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getFullName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPhone());
            statement.setInt(4, user.getUserId());

            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Khong the cap nhat ho so Runner.", exception);
        }
    }

    //cac method Admin do anhdu bo sung

    //lay danh sach user cho trang quan tri Admin
    public List<User> getUsersForAdmin(String keyword,
            String roleName,
            String status) {
        List<User> users = new ArrayList<>();
        String searchValue = keyword == null ? "" : keyword.trim();
        String roleValue = roleName == null ? "" : roleName.trim();
        String statusValue = status == null ? "" : status.trim();
        String likeValue = "%" + searchValue + "%";

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
            WHERE (? = ''
                   OR u.userName LIKE ?
                   OR u.fullName LIKE ?
                   OR u.email LIKE ?)
              AND (? = '' OR r.roleName = ?)
              AND (? = '' OR u.status = ?)
            ORDER BY
                r.roleName ASC,
                u.createdAt DESC,
                u.userId DESC
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, searchValue);
            statement.setString(2, likeValue);
            statement.setString(3, likeValue);
            statement.setString(4, likeValue);
            statement.setString(5, roleValue);
            statement.setString(6, roleValue);
            statement.setString(7, statusValue);
            statement.setString(8, statusValue);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(mapUser(resultSet));
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Khong the lay danh sach user cho Admin.", exception);
        }

        return users;
    }

    //cap nhat trang thai user theo cac gia tri hop le cua database
    public boolean updateUserStatus(int userId, String status) {
        String sql = """
            UPDATE Users
            SET status = ?
            WHERE userId = ?
              AND ? IN ('ACTIVE', 'INACTIVE', 'BANNED')
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setInt(2, userId);
            statement.setString(3, status);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Khong the cap nhat trang thai user.", exception);
        }
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
