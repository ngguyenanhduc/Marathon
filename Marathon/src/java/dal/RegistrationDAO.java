/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author MinhTQHE190232
 */

package dal;



import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.sql.Timestamp;

import java.util.ArrayList;

import java.util.List;

import model.Registration;



public class RegistrationDAO extends DBContext {

    public List<Registration> getRegistrationsByRace(

            int raceId,

            int organizerId,

            String status,

            Integer distanceId) {



        List<Registration> registrations =

                new ArrayList<>();



        StringBuilder sql = new StringBuilder("""

            SELECT

                reg.registrationId,

                reg.runnerId,



                runner.fullName AS runnerName,

                runner.email AS runnerEmail,

                runner.phone AS runnerPhone,



                reg.distanceId,

                d.distanceName,



                d.raceId,

                r.raceName,



                reg.approvedByUserId,

                approver.fullName AS approverName,



                reg.bibNumber,

                reg.status,

                reg.registerDate,

                reg.approvedDate



            FROM Registration reg



            JOIN Users runner

                ON reg.runnerId = runner.userId



            JOIN DistanceKM d

                ON reg.distanceId = d.distanceId



            JOIN Race r

                ON d.raceId = r.raceId



            LEFT JOIN Users approver

                ON reg.approvedByUserId = approver.userId



            WHERE r.raceId = ?

              AND r.createdByUserId = ?

            """);



        boolean filterStatus =

                status != null

                && !status.isBlank();



        boolean filterDistance =

                distanceId != null

                && distanceId > 0;



        if (filterStatus) {

            sql.append("""

                  AND reg.status = ?

                """);

        }



        if (filterDistance) {

            sql.append("""

                  AND reg.distanceId = ?

                """);

        }



        sql.append("""

            ORDER BY

                CASE reg.status

                    WHEN 'PENDING' THEN 1

                    WHEN 'APPROVED' THEN 2

                    WHEN 'REJECTED' THEN 3

                    WHEN 'CANCELLED' THEN 4

                    ELSE 5

                END,

                reg.registerDate DESC,

                reg.registrationId DESC

            """);



        try (PreparedStatement statement =

                connection.prepareStatement(sql.toString())) {



            int parameterIndex = 1;



            statement.setInt(parameterIndex++, raceId);

            statement.setInt(parameterIndex++, organizerId);



            if (filterStatus) {

                statement.setString(

                        parameterIndex++,

                        status

                );

            }



            if (filterDistance) {

                statement.setInt(

                        parameterIndex,

                        distanceId

                );

            }



            try (ResultSet resultSet =

                    statement.executeQuery()) {



                while (resultSet.next()) {

                    registrations.add(

                            mapRegistration(resultSet)

                    );

                }

            }



        } catch (SQLException exception) {

            throw new RuntimeException(

                    "Không thể lấy danh sách runner đăng ký.",

                    exception

            );

        }



        return registrations;

    }



    private Registration mapRegistration(

            ResultSet resultSet)

            throws SQLException {



        Registration registration =

                new Registration();



        registration.setRegistrationId(

                resultSet.getInt("registrationId")

        );



        registration.setRunnerId(

                resultSet.getInt("runnerId")

        );



        registration.setRunnerName(

                resultSet.getString("runnerName")

        );



        registration.setRunnerEmail(

                resultSet.getString("runnerEmail")

        );



        registration.setRunnerPhone(

                resultSet.getString("runnerPhone")

        );



        registration.setDistanceId(

                resultSet.getInt("distanceId")

        );



        registration.setDistanceName(

                resultSet.getString("distanceName")

        );



        registration.setRaceId(

                resultSet.getInt("raceId")

        );



        registration.setRaceName(

                resultSet.getString("raceName")

        );



        int approvedByUserId =

                resultSet.getInt("approvedByUserId");



        if (resultSet.wasNull()) {

            registration.setApprovedByUserId(null);

        } else {

            registration.setApprovedByUserId(

                    approvedByUserId

            );

        }



        registration.setApproverName(

                resultSet.getString("approverName")

        );



        int bibNumber =

                resultSet.getInt("bibNumber");



        if (resultSet.wasNull()) {

            registration.setBibNumber(null);

        } else {

            registration.setBibNumber(bibNumber);

        }



        registration.setStatus(

                resultSet.getString("status")

        );



        Timestamp registerTimestamp =

                resultSet.getTimestamp("registerDate");



        if (registerTimestamp != null) {

            registration.setRegisterDate(

                    registerTimestamp.toLocalDateTime()

            );

        }



        Timestamp approvedTimestamp =

                resultSet.getTimestamp("approvedDate");



        if (approvedTimestamp != null) {

            registration.setApprovedDate(

                    approvedTimestamp.toLocalDateTime()

            );

        }



        return registration;

    }
    
    public boolean rejectRegistration(
        int registrationId,
        int organizerId) {

    String sql = """
        UPDATE reg
        SET
            reg.status = 'REJECTED',
            reg.approvedByUserId = ?,
            reg.approvedDate = GETDATE()

        FROM Registration reg

        INNER JOIN DistanceKM d
            ON reg.distanceId = d.distanceId

        INNER JOIN Race r
            ON d.raceId = r.raceId

        WHERE reg.registrationId = ?
          AND r.createdByUserId = ?
          AND reg.status = 'PENDING'
        """;

    try (PreparedStatement statement =
            connection.prepareStatement(sql)) {

        statement.setInt(1, organizerId);
        statement.setInt(2, registrationId);
        statement.setInt(3, organizerId);

        return statement.executeUpdate() > 0;

    } catch (SQLException exception) {

        throw new RuntimeException(
                "Không thể từ chối đăng ký.",
                exception
        );
    }
}
    
    public Registration getRegistrationByIdAndOrganizer(
        int registrationId,
        int organizerId) {

    String sql = """
        SELECT
            reg.registrationId,
            reg.runnerId,
            runner.fullName AS runnerName,
            runner.email AS runnerEmail,
            runner.phone AS runnerPhone,

            reg.distanceId,
            d.distanceName,

            d.raceId,
            r.raceName,

            reg.approvedByUserId,
            approver.fullName AS approverName,

            reg.bibNumber,
            reg.status,
            reg.registerDate,
            reg.approvedDate

        FROM Registration reg

        JOIN Users runner
            ON reg.runnerId = runner.userId

        JOIN DistanceKM d
            ON reg.distanceId = d.distanceId

        JOIN Race r
            ON d.raceId = r.raceId

        LEFT JOIN Users approver
            ON reg.approvedByUserId = approver.userId

        WHERE reg.registrationId = ?
          AND r.createdByUserId = ?
        """;

    try (PreparedStatement statement =
            connection.prepareStatement(sql)) {

        statement.setInt(1, registrationId);
        statement.setInt(2, organizerId);

        try (ResultSet resultSet =
                statement.executeQuery()) {

            if (resultSet.next()) {
                return mapRegistration(resultSet);
            }
        }

    } catch (SQLException exception) {
        throw new RuntimeException(
                "Không thể lấy thông tin đăng ký.",
                exception
        );
    }

    return null;
}
    
    public boolean approveRegistration(
        int registrationId,
        int organizerId) {

    String getRegistrationSql = """
        SELECT
            reg.registrationId,
            reg.status,
            d.raceId
        FROM Registration reg WITH (UPDLOCK, HOLDLOCK)
        JOIN DistanceKM d
            ON reg.distanceId = d.distanceId
        JOIN Race r
            ON d.raceId = r.raceId
        WHERE reg.registrationId = ?
          AND r.createdByUserId = ?
          AND reg.status = 'PENDING'
        """;

    String getNextBibSql = """
        SELECT
            ISNULL(MAX(reg2.bibNumber), 0) + 1 AS nextBib
        FROM Registration reg2 WITH (UPDLOCK, HOLDLOCK)
        JOIN DistanceKM d2
            ON reg2.distanceId = d2.distanceId
        WHERE d2.raceId = ?
        """;

    String updateRegistrationSql = """
        UPDATE Registration
        SET
            status = 'APPROVED',
            bibNumber = ?,
            approvedByUserId = ?,
            approvedDate = GETDATE()
        WHERE registrationId = ?
          AND status = 'PENDING'
        """;

    try {
        connection.setAutoCommit(false);

        int raceId;

        try (PreparedStatement statement =
                connection.prepareStatement(getRegistrationSql)) {

            statement.setInt(1, registrationId);
            statement.setInt(2, organizerId);

            try (ResultSet resultSet =
                    statement.executeQuery()) {

                if (!resultSet.next()) {
                    connection.rollback();
                    return false;
                }

                raceId = resultSet.getInt("raceId");
            }
        }

        int nextBib;

        try (PreparedStatement statement =
                connection.prepareStatement(getNextBibSql)) {

            statement.setInt(1, raceId);

            try (ResultSet resultSet =
                    statement.executeQuery()) {

                if (!resultSet.next()) {
                    connection.rollback();
                    return false;
                }

                nextBib = resultSet.getInt("nextBib");
            }
        }

        try (PreparedStatement statement =
                connection.prepareStatement(updateRegistrationSql)) {

            statement.setInt(1, nextBib);
            statement.setInt(2, organizerId);
            statement.setInt(3, registrationId);

            int affectedRows =
                    statement.executeUpdate();

            if (affectedRows == 0) {
                connection.rollback();
                return false;
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
                "Không thể duyệt đăng ký.",
                exception
        );

    } finally {

        try {
            connection.setAutoCommit(true);
        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Không thể khôi phục chế độ transaction.",
                    exception
            );
        }
    }
}

    //cac method Runner Registration do PhucNTHE173021 bo sung

    //kiem tra Runner da tung dang ky cu ly hay chua
    public boolean hasRegistration(int runnerId, int distanceId) {
        String sql = """
            SELECT 1
            FROM Registration
            WHERE runnerId = ?
              AND distanceId = ?
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, runnerId);
            statement.setInt(2, distanceId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Khong the kiem tra dang ky trung.", exception);
        }
    }

    //tao don PENDING va kiem tra lai cac dieu kien tai cau SQL
    public boolean createRegistration(int runnerId, int distanceId) {
        String sql = """
            INSERT INTO Registration (
                runnerId,
                distanceId,
                status,
                registerDate
            )
            SELECT
                ?,
                d.distanceId,
                'PENDING',
                GETDATE()
            FROM DistanceKM d
            JOIN Race r
                ON d.raceId = r.raceId
            WHERE d.distanceId = ?
              AND r.status IN ('APPROVED', 'OPEN')
              AND GETDATE() <= r.registrationDeadline
              AND GETDATE() < r.startDate
              AND (
                    SELECT COUNT(*)
                    FROM Registration approvedRegistration
                    WHERE approvedRegistration.distanceId = d.distanceId
                      AND approvedRegistration.status = 'APPROVED'
                  ) < d.maxParticipant
              AND NOT EXISTS (
                    SELECT 1
                    FROM Registration currentRegistration
                    WHERE currentRegistration.runnerId = ?
                      AND currentRegistration.distanceId = d.distanceId
                  )
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, runnerId);
            statement.setInt(2, distanceId);
            statement.setInt(3, runnerId);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            //ma loi 2601 va 2627 cua SQL Server deu la vi pham unique
            if (exception.getErrorCode() == 2601
                    || exception.getErrorCode() == 2627) {
                return false;
            }

            throw new RuntimeException(
                    "Khong the tao don dang ky chay.", exception);
        }
    }

    //lay toan bo don cua Runner de hien thi lich su dang ky
    public List<Registration> getRegistrationsByRunner(int runnerId) {
        List<Registration> registrations = new ArrayList<>();

        String sql = """
            SELECT
                reg.registrationId,
                reg.runnerId,
                runner.fullName AS runnerName,
                runner.email AS runnerEmail,
                runner.phone AS runnerPhone,
                reg.distanceId,
                d.distanceName,
                d.raceId,
                r.raceName,
                reg.approvedByUserId,
                approver.fullName AS approverName,
                reg.bibNumber,
                reg.status,
                reg.registerDate,
                reg.approvedDate,
                result.finishTime,
                result.status AS resultStatus,
                result.ranking
            FROM Registration reg
            JOIN Users runner
                ON reg.runnerId = runner.userId
            JOIN DistanceKM d
                ON reg.distanceId = d.distanceId
            JOIN Race r
                ON d.raceId = r.raceId
            LEFT JOIN Users approver
                ON reg.approvedByUserId = approver.userId
            LEFT JOIN RaceResult result
                ON reg.registrationId = result.registrationId
            WHERE reg.runnerId = ?
            ORDER BY reg.registerDate DESC
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, runnerId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    registrations.add(mapRunnerRegistration(resultSet));
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Khong the lay danh sach dang ky cua Runner.", exception);
        }

        return registrations;
    }

    //chuyen mot dong ResultSet thanh Registration cho trang Runner
    private Registration mapRunnerRegistration(ResultSet resultSet)
            throws SQLException {
        Registration registration = new Registration();
        registration.setRegistrationId(
                resultSet.getInt("registrationId"));
        registration.setRunnerId(resultSet.getInt("runnerId"));
        registration.setRunnerName(resultSet.getString("runnerName"));
        registration.setRunnerEmail(resultSet.getString("runnerEmail"));
        registration.setRunnerPhone(resultSet.getString("runnerPhone"));
        registration.setDistanceId(resultSet.getInt("distanceId"));
        registration.setDistanceName(resultSet.getString("distanceName"));
        registration.setRaceId(resultSet.getInt("raceId"));
        registration.setRaceName(resultSet.getString("raceName"));

        int approvedByUserId = resultSet.getInt("approvedByUserId");
        registration.setApprovedByUserId(
                resultSet.wasNull() ? null : approvedByUserId);
        registration.setApproverName(resultSet.getString("approverName"));

        int bibNumber = resultSet.getInt("bibNumber");
        registration.setBibNumber(resultSet.wasNull() ? null : bibNumber);
        registration.setStatus(resultSet.getString("status"));

        Timestamp registerDate = resultSet.getTimestamp("registerDate");
        if (registerDate != null) {
            registration.setRegisterDate(registerDate.toLocalDateTime());
        }

        Timestamp approvedDate = resultSet.getTimestamp("approvedDate");
        if (approvedDate != null) {
            registration.setApprovedDate(approvedDate.toLocalDateTime());
        }

        java.sql.Time finishTime = resultSet.getTime("finishTime");
        if (finishTime != null) {
            registration.setFinishTime(finishTime.toLocalTime());
        }

        registration.setResultStatus(resultSet.getString("resultStatus"));
        int ranking = resultSet.getInt("ranking");
        registration.setRanking(resultSet.wasNull() ? null : ranking);
        return registration;
    }
}
