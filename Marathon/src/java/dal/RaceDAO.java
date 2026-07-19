/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

/**
 *
 * @author MinhTQHE190232
 * 
 */
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Race;

public class RaceDAO extends DBContext {

    private int raceId;

    public List<Race> getRacesByOrganizerId(int organizerId) {
        List<Race> races = new ArrayList<>();

        String sql = """
            SELECT
                r.raceId,
                r.createdByUserId,
                r.approvedByUserId,
                r.raceName,
                r.description,
                r.createdDate,
                r.approvedDate,
                r.startDate,
                r.endDate,
                r.registrationDeadline,
                r.location,
                r.status,
                COUNT(DISTINCT d.distanceId) AS distanceCount,
                COUNT(DISTINCT reg.registrationId) AS registrationCount
            FROM Race r
            LEFT JOIN DistanceKM d
                ON r.raceId = d.raceId
            LEFT JOIN Registration reg
                ON d.distanceId = reg.distanceId
            WHERE r.createdByUserId = ?
            GROUP BY
                r.raceId,
                r.createdByUserId,
                r.approvedByUserId,
                r.raceName,
                r.description,
                r.createdDate,
                r.approvedDate,
                r.startDate,
                r.endDate,
                r.registrationDeadline,
                r.location,
                r.status
            ORDER BY r.createdDate DESC
            """;

        try (PreparedStatement statement =
                connection.prepareStatement(sql)) {

            statement.setInt(1, organizerId);

            try (ResultSet resultSet =
                    statement.executeQuery()) {

                while (resultSet.next()) {
                    races.add(mapRace(resultSet));
                }
            }

        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Không thể lấy danh sách giải của Organizer.",
                    exception
            );
        }

        return races;
    }

    public Race getRaceByIdAndOrganizer(
            int raceId,
            int organizerId) {

        String sql = """
            SELECT
                r.raceId,
                r.createdByUserId,
                r.approvedByUserId,
                r.raceName,
                r.description,
                r.createdDate,
                r.approvedDate,
                r.startDate,
                r.endDate,
                r.registrationDeadline,
                r.location,
                r.status,
                COUNT(DISTINCT d.distanceId) AS distanceCount,
                COUNT(DISTINCT reg.registrationId) AS registrationCount
            FROM Race r
            LEFT JOIN DistanceKM d
                ON r.raceId = d.raceId
            LEFT JOIN Registration reg
                ON d.distanceId = reg.distanceId
            WHERE r.raceId = ?
              AND r.createdByUserId = ?
            GROUP BY
                r.raceId,
                r.createdByUserId,
                r.approvedByUserId,
                r.raceName,
                r.description,
                r.createdDate,
                r.approvedDate,
                r.startDate,
                r.endDate,
                r.registrationDeadline,
                r.location,
                r.status
            """;

        try (PreparedStatement statement =
                connection.prepareStatement(sql)) {

            statement.setInt(1, raceId);
            statement.setInt(2, organizerId);

            try (ResultSet resultSet =
                    statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapRace(resultSet);
                }
            }

        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Không thể lấy thông tin giải.",
                    exception
            );
        }

        return null;
    }

    public boolean organizerOwnsRace(
            int raceId,
            int organizerId) {

        String sql = """
            SELECT 1
            FROM Race
            WHERE raceId = ?
              AND createdByUserId = ?
            """;

        try (PreparedStatement statement =
                connection.prepareStatement(sql)) {

            statement.setInt(1, raceId);
            statement.setInt(2, organizerId);

            try (ResultSet resultSet =
                    statement.executeQuery()) {

                return resultSet.next();
            }

        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Không thể kiểm tra quyền sở hữu giải.",
                    exception
            );
        }
    }

    private Race mapRace(ResultSet resultSet)
            throws SQLException {

        Race race = new Race();

        race.setRaceId(
                resultSet.getInt("raceId")
        );

        race.setCreatedByUserId(
                resultSet.getInt("createdByUserId")
        );

        int approvedByUserId =
                resultSet.getInt("approvedByUserId");

        if (resultSet.wasNull()) {
            race.setApprovedByUserId(null);
        } else {
            race.setApprovedByUserId(approvedByUserId);
        }

        race.setRaceName(
                resultSet.getString("raceName")
        );

        race.setDescription(
                resultSet.getString("description")
        );

        if (resultSet.getTimestamp("createdDate") != null) {
            race.setCreatedDate(
                    resultSet
                            .getTimestamp("createdDate")
                            .toLocalDateTime()
            );
        }

        if (resultSet.getTimestamp("approvedDate") != null) {
            race.setApprovedDate(
                    resultSet
                            .getTimestamp("approvedDate")
                            .toLocalDateTime()
            );
        }

        if (resultSet.getTimestamp("startDate") != null) {
            race.setStartDate(
                    resultSet
                            .getTimestamp("startDate")
                            .toLocalDateTime()
            );
        }

        if (resultSet.getTimestamp("endDate") != null) {
            race.setEndDate(
                    resultSet
                            .getTimestamp("endDate")
                            .toLocalDateTime()
            );
        }

        if (resultSet.getTimestamp("registrationDeadline") != null) {
            race.setRegistrationDeadline(
                    resultSet
                            .getTimestamp("registrationDeadline")
                            .toLocalDateTime()
            );
        }

        race.setLocation(
                resultSet.getString("location")
        );

        race.setStatus(
                resultSet.getString("status")
        );

        race.setDistanceCount(
                resultSet.getInt("distanceCount")
        );

        race.setRegistrationCount(
                resultSet.getInt("registrationCount")
        );

        return race;
    }
    
    public int createRace(Race race) {
    String sql = """
        INSERT INTO Race (
            createdByUserId,
            approvedByUserId,
            raceName,
            description,
            createdDate,
            approvedDate,
            startDate,
            endDate,
            registrationDeadline,
            location,
            status
        )
        VALUES (
            ?,
            NULL,
            ?,
            ?,
            GETDATE(),
            NULL,
            ?,
            ?,
            ?,
            ?,
            'PENDING'
        )
        """;

    try (
        PreparedStatement statement =
                connection.prepareStatement(
                        sql,
                        java.sql.Statement.RETURN_GENERATED_KEYS
                )
    ) {
        statement.setInt(
                1,
                race.getCreatedByUserId()
        );

        statement.setString(
                2,
                race.getRaceName()
        );

        statement.setString(
                3,
                race.getDescription()
        );

        statement.setTimestamp(
                4,
                java.sql.Timestamp.valueOf(
                        race.getStartDate()
                )
        );

        statement.setTimestamp(
                5,
                java.sql.Timestamp.valueOf(
                        race.getEndDate()
                )
        );

        statement.setTimestamp(
                6,
                java.sql.Timestamp.valueOf(
                        race.getRegistrationDeadline()
                )
        );

        statement.setString(
                7,
                race.getLocation()
        );

        int affectedRows =
                statement.executeUpdate();

        if (affectedRows == 0) {
            return -1;
        }

        try (
            ResultSet generatedKeys =
                    statement.getGeneratedKeys()
        ) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
        }

    } catch (SQLException exception) {
        throw new RuntimeException(
                "Không thể tạo giải chạy mới.",
                exception
        );
    }

    return -1;
}
    
    public boolean updateRace(
        Race race,
        int organizerId) {

    String sql = """
        UPDATE Race
        SET
            raceName = ?,
            description = ?,
            startDate = ?,
            endDate = ?,
            registrationDeadline = ?,
            location = ?,
            status = 'PENDING',
            approvedByUserId = NULL,
            approvedDate = NULL
        WHERE raceId = ?
          AND createdByUserId = ?
          AND status IN ('PENDING', 'REJECTED')
        """;

    try (PreparedStatement statement =
            connection.prepareStatement(sql)) {

        statement.setString(
                1,
                race.getRaceName()
        );

        statement.setString(
                2,
                race.getDescription()
        );

        statement.setTimestamp(
                3,
                java.sql.Timestamp.valueOf(
                        race.getStartDate()
                )
        );

        statement.setTimestamp(
                4,
                java.sql.Timestamp.valueOf(
                        race.getEndDate()
                )
        );

        statement.setTimestamp(
                5,
                java.sql.Timestamp.valueOf(
                        race.getRegistrationDeadline()
                )
        );

        statement.setString(
                6,
                race.getLocation()
        );

        statement.setInt(
                7,
                race.getRaceId()
        );

        statement.setInt(
                8,
                organizerId
        );

        return statement.executeUpdate() > 0;

    } catch (SQLException exception) {
        throw new RuntimeException(
                "Không thể cập nhật giải chạy.",
                exception
        );
    }
}
}