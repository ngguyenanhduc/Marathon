package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                request.status,
                requestDate,
                approvedDate
            FROM OrganizerRequest request
            JOIN Users requester
                ON request.requesterId = requester.userId
            LEFT JOIN Users approver
                ON request.approverId = approver.userId
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
