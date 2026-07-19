/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 * @author Admin
 */
package dal;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.DistanceKM;

public class DistanceDAO extends DBContext {

    //cac method Public Distance do PhucNTHE173021 bo sung

    //lay cac cu ly thuoc mot giai da duoc cong khai
    public List<DistanceKM> getPublicDistancesByRace(int raceId) {
        List<DistanceKM> distances = new ArrayList<>();

        String sql = """
            SELECT
                d.distanceId,
                d.raceId,
                r.raceName,
                r.status AS raceStatus,
                d.distanceName,
                d.distanceKm,
                d.registrationFee,
                d.maxParticipant,
                SUM(CASE WHEN reg.status = 'APPROVED' THEN 1 ELSE 0 END)
                    AS approvedRegistrationCount,
                SUM(CASE WHEN reg.status = 'PENDING' THEN 1 ELSE 0 END)
                    AS pendingRegistrationCount
            FROM DistanceKM d
            JOIN Race r
                ON d.raceId = r.raceId
            LEFT JOIN Registration reg
                ON d.distanceId = reg.distanceId
            WHERE d.raceId = ?
              AND r.status NOT IN ('PENDING', 'REJECTED')
            GROUP BY
                d.distanceId,
                d.raceId,
                r.raceName,
                r.status,
                d.distanceName,
                d.distanceKm,
                d.registrationFee,
                d.maxParticipant
            ORDER BY d.distanceKm ASC
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, raceId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    distances.add(mapPublicDistance(resultSet));
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Khong the lay danh sach cu ly cong khai.", exception);
        }

        return distances;
    }

    //lay mot cu ly cong khai de kiem tra truoc khi Runner dang ky
    public DistanceKM getPublicDistanceById(int distanceId) {
        String sql = """
            SELECT
                d.distanceId,
                d.raceId,
                r.raceName,
                r.status AS raceStatus,
                d.distanceName,
                d.distanceKm,
                d.registrationFee,
                d.maxParticipant,
                SUM(CASE WHEN reg.status = 'APPROVED' THEN 1 ELSE 0 END)
                    AS approvedRegistrationCount,
                SUM(CASE WHEN reg.status = 'PENDING' THEN 1 ELSE 0 END)
                    AS pendingRegistrationCount
            FROM DistanceKM d
            JOIN Race r
                ON d.raceId = r.raceId
            LEFT JOIN Registration reg
                ON d.distanceId = reg.distanceId
            WHERE d.distanceId = ?
              AND r.status NOT IN ('PENDING', 'REJECTED')
            GROUP BY
                d.distanceId,
                d.raceId,
                r.raceName,
                r.status,
                d.distanceName,
                d.distanceKm,
                d.registrationFee,
                d.maxParticipant
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, distanceId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapPublicDistance(resultSet);
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Khong the lay thong tin cu ly cong khai.", exception);
        }

        return null;
    }

    //chuyen mot dong ResultSet thanh DistanceKM cho trang public
    private DistanceKM mapPublicDistance(ResultSet resultSet)
            throws SQLException {
        DistanceKM distance = new DistanceKM();
        distance.setDistanceId(resultSet.getInt("distanceId"));
        distance.setRaceId(resultSet.getInt("raceId"));
        distance.setRaceName(resultSet.getString("raceName"));
        distance.setRaceStatus(resultSet.getString("raceStatus"));
        distance.setDistanceName(resultSet.getString("distanceName"));
        distance.setDistanceKm(resultSet.getBigDecimal("distanceKm"));
        distance.setRegistrationFee(
                resultSet.getBigDecimal("registrationFee"));
        distance.setMaxParticipant(resultSet.getInt("maxParticipant"));
        distance.setApprovedRegistrationCount(
                resultSet.getInt("approvedRegistrationCount"));
        distance.setPendingRegistrationCount(
                resultSet.getInt("pendingRegistrationCount"));
        return distance;
    }

    public List<DistanceKM> getDistancesByRace(
            int raceId,
            int organizerId) {

        List<DistanceKM> distances = new ArrayList<>();

            String sql = """
               SELECT
                   d.distanceId,
                   d.raceId,
                   r.raceName,
                   r.status AS raceStatus,
                   d.distanceName,
                   d.distanceKm,
                   d.maxParticipant,

                   SUM(
                       CASE
                           WHEN reg.status = 'APPROVED'
                           THEN 1
                           ELSE 0
                       END
                   ) AS approvedRegistrationCount,

                   SUM(
                       CASE
                           WHEN reg.status = 'PENDING'
                           THEN 1
                           ELSE 0
                       END
                   ) AS pendingRegistrationCount

               FROM DistanceKM d

               JOIN Race r
                   ON d.raceId = r.raceId

               LEFT JOIN Registration reg
                   ON d.distanceId = reg.distanceId

               WHERE d.raceId = ?
                 AND r.createdByUserId = ?

               GROUP BY
                   d.distanceId,
                   d.raceId,
                   r.raceName,
                   r.status,
                   d.distanceName,
                   d.distanceKm,
                   d.maxParticipant

               ORDER BY d.distanceKm ASC
               """;
 
 
        try (PreparedStatement statement =
                connection.prepareStatement(sql)) {

            statement.setInt(1, raceId);
            statement.setInt(2, organizerId);

            try (ResultSet resultSet =
                    statement.executeQuery()) {

                while (resultSet.next()) {
                    distances.add(mapDistance(resultSet));
                }
            }

        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Không thể lấy danh sách cự ly.",
                    exception
            );
        }

        return distances;
    }

    public boolean existsDistanceName(
            int raceId,
            String distanceName) {

        String sql = """
            SELECT 1
            FROM DistanceKM
            WHERE raceId = ?
              AND LOWER(LTRIM(RTRIM(distanceName)))
                  = LOWER(LTRIM(RTRIM(?)))
            """;

        try (PreparedStatement statement =
                connection.prepareStatement(sql)) {

            statement.setInt(1, raceId);
            statement.setString(2, distanceName);

            try (ResultSet resultSet =
                    statement.executeQuery()) {

                return resultSet.next();
            }

        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Không thể kiểm tra tên cự ly.",
                    exception
            );
        }
    }

    public boolean existsDistanceKm(
            int raceId,
            BigDecimal distanceKm) {

        String sql = """
            SELECT 1
            FROM DistanceKM
            WHERE raceId = ?
              AND distanceKm = ?
            """;

        try (PreparedStatement statement =
                connection.prepareStatement(sql)) {

            statement.setInt(1, raceId);
            statement.setBigDecimal(2, distanceKm);

            try (ResultSet resultSet =
                    statement.executeQuery()) {

                return resultSet.next();
            }

        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Không thể kiểm tra số kilomet.",
                    exception
            );
        }
    }
    
    public DistanceKM getDistanceByIdAndOrganizer(
        int distanceId,
        int organizerId) {

    String sql = """
        SELECT
            d.distanceId,
            d.raceId,
            r.raceName,
            r.status AS raceStatus,
            d.distanceName,
            d.distanceKm,
            d.maxParticipant,

            0 AS approvedRegistrationCount,
            0 AS pendingRegistrationCount

        FROM DistanceKM d

        JOIN Race r
            ON d.raceId = r.raceId

        WHERE d.distanceId = ?
          AND r.createdByUserId = ?
        """;

    try (PreparedStatement statement =
            connection.prepareStatement(sql)) {

        statement.setInt(1, distanceId);
        statement.setInt(2, organizerId);

        try (ResultSet resultSet =
                statement.executeQuery()) {

            if (resultSet.next()) {
                return mapDistance(resultSet);
            }
        }

    } catch (SQLException exception) {
        throw new RuntimeException(
                "Không thể lấy thông tin cự ly.",
                exception
        );
    }

    return null;
    }

    public boolean createDistance(
        DistanceKM distance,
        int organizerId) {

    String sql = """
        INSERT INTO DistanceKM (
            raceId,
            distanceName,
            distanceKm,
            maxParticipant
        )
        SELECT
            ?,
            ?,
            ?,
            ?
        WHERE EXISTS (
            SELECT 1
            FROM Race
            WHERE raceId = ?
              AND createdByUserId = ?
              AND status IN ('PENDING', 'REJECTED')
        )
        """;

    try (PreparedStatement statement =
            connection.prepareStatement(sql)) {

        statement.setInt(
                1,
                distance.getRaceId()
        );

        statement.setString(
                2,
                distance.getDistanceName()
        );

        statement.setBigDecimal(
                3,
                distance.getDistanceKm()
        );

        statement.setInt(
                4,
                distance.getMaxParticipant()
        );

        statement.setInt(
                5,
                distance.getRaceId()
        );

        statement.setInt(
                6,
                organizerId
        );

        return statement.executeUpdate() > 0;

    } catch (SQLException exception) {
        throw new RuntimeException(
                "Không thể thêm cự ly.",
                exception
        );
    }
}
    
    public boolean deleteDistance(
        int distanceId,
        int organizerId) {

    String sql = """
        DELETE d
        FROM DistanceKM d

        JOIN Race r
            ON d.raceId = r.raceId

        WHERE d.distanceId = ?
          AND r.createdByUserId = ?
          AND r.status IN ('PENDING', 'REJECTED')

          AND NOT EXISTS (
              SELECT 1
              FROM Registration reg
              WHERE reg.distanceId = d.distanceId
          )
        """;

    try (PreparedStatement statement =
            connection.prepareStatement(sql)) {

        statement.setInt(1, distanceId);
        statement.setInt(2, organizerId);

        return statement.executeUpdate() > 0;

    } catch (SQLException exception) {
        throw new RuntimeException(
                "Không thể xóa cự ly.",
                exception
        );
    }
}

    private DistanceKM mapDistance(
            ResultSet resultSet)
            throws SQLException {

        DistanceKM distance = new DistanceKM();

        distance.setDistanceId(
                resultSet.getInt("distanceId")
        );

        distance.setRaceId(
                resultSet.getInt("raceId")
        );

        distance.setRaceName(
                resultSet.getString("raceName")
        );

        distance.setRaceStatus(
                resultSet.getString("raceStatus")
        );

        distance.setDistanceName(
                resultSet.getString("distanceName")
        );

        distance.setDistanceKm(
                resultSet.getBigDecimal("distanceKm")
        );

        distance.setMaxParticipant(
                resultSet.getInt("maxParticipant")
        );

        distance.setApprovedRegistrationCount(
                resultSet.getInt(
                        "approvedRegistrationCount"
                )
        );

        distance.setPendingRegistrationCount(
                resultSet.getInt(
                        "pendingRegistrationCount"
                )
        );

        return distance;
    }
    
    public boolean existsDistanceNameForUpdate(
        int raceId,
        String distanceName,
        int excludeDistanceId) {

    String sql = """
        SELECT 1
        FROM DistanceKM
        WHERE raceId = ?
          AND distanceId <> ?
          AND LOWER(LTRIM(RTRIM(distanceName)))
              = LOWER(LTRIM(RTRIM(?)))
        """;

    try (PreparedStatement statement =
            connection.prepareStatement(sql)) {

        statement.setInt(1, raceId);
        statement.setInt(2, excludeDistanceId);
        statement.setString(3, distanceName);

        try (ResultSet resultSet =
                statement.executeQuery()) {

            return resultSet.next();
        }

    } catch (SQLException exception) {
        throw new RuntimeException(
                "Không thể kiểm tra tên cự ly.",
                exception
        );
    }
}
    public boolean existsDistanceKmForUpdate(
        int raceId,
        BigDecimal distanceKm,
        int excludeDistanceId) {

    String sql = """
        SELECT 1
        FROM DistanceKM
        WHERE raceId = ?
          AND distanceId <> ?
          AND distanceKm = ?
        """;

    try (PreparedStatement statement =
            connection.prepareStatement(sql)) {

        statement.setInt(1, raceId);
        statement.setInt(2, excludeDistanceId);
        statement.setBigDecimal(3, distanceKm);

        try (ResultSet resultSet =
                statement.executeQuery()) {

            return resultSet.next();
        }

    } catch (SQLException exception) {
        throw new RuntimeException(
                "Không thể kiểm tra số kilomet.",
                exception
        );
    }
}
    
    public boolean updateDistance(
        DistanceKM distance,
        int organizerId) {

    String sql = """
        UPDATE d
        SET
            d.distanceName = ?,
            d.distanceKm = ?,
            d.maxParticipant = ?

        FROM DistanceKM d

        JOIN Race r
            ON d.raceId = r.raceId

        WHERE d.distanceId = ?
          AND d.raceId = ?
          AND r.createdByUserId = ?
          AND r.status IN ('PENDING', 'REJECTED')
        """;

    try (PreparedStatement statement =
            connection.prepareStatement(sql)) {

        statement.setString(
                1,
                distance.getDistanceName()
        );

        statement.setBigDecimal(
                2,
                distance.getDistanceKm()
        );

        statement.setInt(
                3,
                distance.getMaxParticipant()
        );

        statement.setInt(
                4,
                distance.getDistanceId()
        );

        statement.setInt(
                5,
                distance.getRaceId()
        );

        statement.setInt(
                6,
                organizerId
        );

        return statement.executeUpdate() > 0;

    } catch (SQLException exception) {
        throw new RuntimeException(
                "Không thể cập nhật cự ly.",
                exception
        );
    }
}
}
