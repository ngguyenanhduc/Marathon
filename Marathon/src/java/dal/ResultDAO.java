package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;

/**
 * Author: anhdu
 *
 * Version: 1
 *
 * Last Update Date: 17/7/2026
 *
 * Purpose: luu ket qua chay va cap nhat thu hang
 */
public class ResultDAO extends DBContext {

    //luu ket qua FINISHED va tinh lai ranking trong cung cu ly
    public boolean saveFinishedResult(int registrationId, LocalTime finishTime) {
        String getDistanceSql = """
            SELECT distanceId
            FROM Registration
            WHERE registrationId = ?
              AND status = 'APPROVED'
            """;

        String saveResultSql = """
            MERGE RaceResult AS target
            USING (
                SELECT
                    ? AS registrationId,
                    ? AS finishTime
            ) AS source
                ON target.registrationId = source.registrationId
            WHEN MATCHED THEN
                UPDATE SET
                    status = 'FINISHED',
                    finishTime = source.finishTime
            WHEN NOT MATCHED THEN
                INSERT (
                    registrationId,
                    ranking,
                    status,
                    finishTime
                )
                VALUES (
                    source.registrationId,
                    NULL,
                    'FINISHED',
                    source.finishTime
                );
            """;

        String clearRankingSql = """
            UPDATE result
            SET ranking = NULL
            FROM RaceResult result
            JOIN Registration registration
                ON result.registrationId = registration.registrationId
            WHERE registration.distanceId = ?
            """;

        String updateRankingSql = """
            WITH RankedResult AS (
                SELECT
                    result.resultId,
                    ROW_NUMBER() OVER (
                        ORDER BY
                            result.finishTime ASC,
                            registration.bibNumber ASC,
                            result.registrationId ASC
                    ) AS newRanking
                FROM RaceResult result
                JOIN Registration registration
                    ON result.registrationId = registration.registrationId
                WHERE registration.distanceId = ?
                  AND result.status = 'FINISHED'
                  AND result.finishTime IS NOT NULL
            )
            UPDATE result
            SET ranking = ranked.newRanking
            FROM RaceResult result
            JOIN RankedResult ranked
                ON result.resultId = ranked.resultId
            """;

        try {
            connection.setAutoCommit(false);

            int distanceId;
            try (PreparedStatement statement =
                    connection.prepareStatement(getDistanceSql)) {
                statement.setInt(1, registrationId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        connection.rollback();
                        return false;
                    }

                    distanceId = resultSet.getInt("distanceId");
                }
            }

            try (PreparedStatement statement =
                    connection.prepareStatement(saveResultSql)) {
                statement.setInt(1, registrationId);
                statement.setTime(2, Time.valueOf(finishTime));
                statement.executeUpdate();
            }

            try (PreparedStatement statement =
                    connection.prepareStatement(clearRankingSql)) {
                statement.setInt(1, distanceId);
                statement.executeUpdate();
            }

            try (PreparedStatement statement =
                    connection.prepareStatement(updateRankingSql)) {
                statement.setInt(1, distanceId);
                statement.executeUpdate();
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
                    "Khong the luu ket qua chay.", exception);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException exception) {
                throw new RuntimeException(
                        "Khong the khoi phuc transaction.", exception);
            }
        }
    }
}
