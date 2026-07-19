package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.OrganizerRequest;

/**
 * Author: PhucNTHE173021
 *
 * Version: 1
 *
 * Last Update Date: 17/7/2026
 *
 * Purpose: xu ly yeu cau tro thanh Organizer cua Runner
 */
public class OrganizerRequestDAO extends DBContext {

    //lay yeu cau moi nhat de Runner theo doi trang thai
    public OrganizerRequest getLatestRequestByRunner(int runnerId) {
        String sql = """
            SELECT TOP 1
                requestId,
                requesterId,
                requester.fullName AS requesterName,
                requester.email AS requesterEmail,
                approverId,
                approver.fullName AS approverName,
                reason,
                orgRequest.status,
                requestDate,
                approvedDate
            FROM OrganizerRequest orgRequest
            JOIN Users requester
                ON orgRequest.requesterId = requester.userId
            LEFT JOIN Users approver
                ON orgRequest.approverId = approver.userId
            WHERE requesterId = ?
            ORDER BY requestDate DESC
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, runnerId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRequest(resultSet);
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Khong the lay yeu cau Organizer.", exception);
        }

        return null;
    }

    //kiem tra Runner co yeu cau PENDING dang cho Admin xu ly
    public boolean hasPendingRequest(int runnerId) {
        String sql = """
            SELECT 1
            FROM OrganizerRequest
            WHERE requesterId = ?
              AND status = 'PENDING'
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, runnerId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Khong the kiem tra yeu cau dang cho.", exception);
        }
    }

    //tao yeu cau Organizer neu Runner chua co yeu cau PENDING
    public boolean createRequest(int runnerId, String reason) {
        String sql = """
            INSERT INTO OrganizerRequest (
                requesterId,
                reason,
                status,
                requestDate
            )
            SELECT ?, ?, 'PENDING', GETDATE()
            WHERE NOT EXISTS (
                SELECT 1
                FROM OrganizerRequest
                WHERE requesterId = ?
                  AND status = 'PENDING'
            )
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, runnerId);
            statement.setString(2, reason);
            statement.setInt(3, runnerId);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Khong the gui yeu cau Organizer.", exception);
        }
    }

    //cac method Admin do anhdu bo sung

    //lay cac yeu cau Organizer dang cho Admin duyet
    public List<OrganizerRequest> getPendingRequestsForAdmin() {
        List<OrganizerRequest> requests = new ArrayList<>();
        String sql = """
            SELECT
                orgRequest.requestId,
                orgRequest.requesterId,
                requester.fullName AS requesterName,
                requester.email AS requesterEmail,
                orgRequest.approverId,
                approver.fullName AS approverName,
                orgRequest.reason,
                orgRequest.status,
                orgRequest.requestDate,
                orgRequest.approvedDate
            FROM OrganizerRequest orgRequest
            JOIN Users requester
                ON orgRequest.requesterId = requester.userId
            LEFT JOIN Users approver
                ON orgRequest.approverId = approver.userId
            WHERE orgRequest.status = 'PENDING'
            ORDER BY orgRequest.requestDate ASC
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                requests.add(mapRequest(resultSet));
            }
        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Khong the lay yeu cau Organizer cho Admin.", exception);
        }

        return requests;
    }

    //duyet yeu cau va nang quyen Runner thanh Organizer neu duoc chap nhan
    public boolean reviewRequestByAdmin(int requestId,
            int adminId,
            String status) {
        String updateRequestSql = """
            UPDATE OrganizerRequest
            SET approverId = ?,
                status = ?,
                approvedDate = GETDATE()
            WHERE requestId = ?
              AND status = 'PENDING'
              AND ? IN ('APPROVED', 'REJECTED')
            """;

        String updateRoleSql = """
            UPDATE Users
            SET roleId = (
                SELECT roleId
                FROM Roles
                WHERE roleName = 'ORGANIZER'
            )
            WHERE userId = (
                SELECT requesterId
                FROM OrganizerRequest
                WHERE requestId = ?
            )
            """;

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement statement =
                    connection.prepareStatement(updateRequestSql)) {
                statement.setInt(1, adminId);
                statement.setString(2, status);
                statement.setInt(3, requestId);
                statement.setString(4, status);

                if (statement.executeUpdate() == 0) {
                    connection.rollback();
                    return false;
                }
            }

            if ("APPROVED".equals(status)) {
                try (PreparedStatement statement =
                        connection.prepareStatement(updateRoleSql)) {
                    statement.setInt(1, requestId);

                    if (statement.executeUpdate() == 0) {
                        connection.rollback();
                        return false;
                    }
                }
            }

            connection.commit();
            return true;
        } catch (SQLException exception) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                exception.addSuppressed(rollbackException);
            }

            throw new RuntimeException(
                    "Khong the duyet yeu cau Organizer.", exception);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException exception) {
                throw new RuntimeException(
                        "Khong the khoi phuc transaction.", exception);
            }
        }
    }

    //chuyen mot dong ResultSet thanh OrganizerRequest
    private OrganizerRequest mapRequest(ResultSet resultSet)
            throws SQLException {
        OrganizerRequest request = new OrganizerRequest();
        request.setRequestId(resultSet.getInt("requestId"));
        request.setRequesterId(resultSet.getInt("requesterId"));
        request.setRequesterName(resultSet.getString("requesterName"));
        request.setRequesterEmail(resultSet.getString("requesterEmail"));

        int approverId = resultSet.getInt("approverId");
        request.setApproverId(resultSet.wasNull() ? null : approverId);
        request.setApproverName(resultSet.getString("approverName"));
        request.setReason(resultSet.getString("reason"));
        request.setStatus(resultSet.getString("status"));

        if (resultSet.getTimestamp("requestDate") != null) {
            request.setRequestDate(resultSet.getTimestamp("requestDate")
                    .toLocalDateTime());
        }

        if (resultSet.getTimestamp("approvedDate") != null) {
            request.setApprovedDate(resultSet.getTimestamp("approvedDate")
                    .toLocalDateTime());
        }

        return request;
    }
}
