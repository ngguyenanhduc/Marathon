IF DB_ID(N'MarathonDB') IS NULL
BEGIN
    RAISERROR(N'Can chay MarathonDB.sql truoc khi chay insertData.sql.',
        16, 1);
END
GO

USE MarathonDB;
GO

SET ANSI_NULLS ON;
SET ANSI_PADDING ON;
SET ANSI_WARNINGS ON;
SET ARITHABORT ON;
SET CONCAT_NULL_YIELDS_NULL ON;
SET QUOTED_IDENTIFIER ON;
SET NUMERIC_ROUNDABORT OFF;
SET NOCOUNT ON;
SET XACT_ABORT ON;

BEGIN TRY
    BEGIN TRANSACTION;

    /* =========================
       ROLE
    ========================= */

    IF NOT EXISTS (SELECT 1 FROM Roles WHERE roleName = N'ADMIN')
    BEGIN
        INSERT INTO Roles(roleName) VALUES (N'ADMIN');
    END

    IF NOT EXISTS (SELECT 1 FROM Roles WHERE roleName = N'ORGANIZER')
    BEGIN
        INSERT INTO Roles(roleName) VALUES (N'ORGANIZER');
    END

    IF NOT EXISTS (SELECT 1 FROM Roles WHERE roleName = N'RUNNER')
    BEGIN
        INSERT INTO Roles(roleName) VALUES (N'RUNNER');
    END

    DECLARE @adminRoleId INT;
    DECLARE @organizerRoleId INT;
    DECLARE @runnerRoleId INT;

    SELECT @adminRoleId = roleId FROM Roles WHERE roleName = N'ADMIN';
    SELECT @organizerRoleId = roleId FROM Roles WHERE roleName = N'ORGANIZER';
    SELECT @runnerRoleId = roleId FROM Roles WHERE roleName = N'RUNNER';

    /* =========================
       USERS
       Mat khau demo: 123
    ========================= */

    DECLARE @DemoUsers TABLE (
        userName NVARCHAR(50) NOT NULL,
        password NVARCHAR(255) NOT NULL,
        roleId INT NOT NULL,
        fullName NVARCHAR(100) NOT NULL,
        email NVARCHAR(100) NOT NULL,
        phone NVARCHAR(20) NULL,
        status NVARCHAR(20) NOT NULL
    );

    INSERT INTO @DemoUsers (
        userName,
        password,
        roleId,
        fullName,
        email,
        phone,
        status
    )
    VALUES
        (N'admin01', N'123', @adminRoleId,
            N'Admin Demo', N'admin01@demo.local', N'0900000001', N'ACTIVE'),
        (N'admin_inactive', N'123', @adminRoleId,
            N'Admin Inactive', N'admin_inactive@demo.local', N'0900000002',
            N'INACTIVE'),
        (N'organizer01', N'123', @organizerRoleId,
            N'Organizer One', N'organizer01@demo.local', N'0911000001',
            N'ACTIVE'),
        (N'organizer02', N'123', @organizerRoleId,
            N'Organizer Two', N'organizer02@demo.local', N'0911000002',
            N'ACTIVE'),
        (N'organizer_from_request', N'123', @organizerRoleId,
            N'Organizer From Request', N'organizer_request@demo.local',
            N'0911000003', N'ACTIVE'),
        (N'organizer_inactive', N'123', @organizerRoleId,
            N'Organizer Inactive', N'organizer_inactive@demo.local',
            N'0911000004', N'INACTIVE'),
        (N'runner01', N'123', @runnerRoleId,
            N'Runner One', N'runner01@demo.local', N'0922000001',
            N'ACTIVE'),
        (N'runner02', N'123', @runnerRoleId,
            N'Runner Two', N'runner02@demo.local', N'0922000002',
            N'ACTIVE'),
        (N'runner03', N'123', @runnerRoleId,
            N'Runner Three', N'runner03@demo.local', N'0922000003',
            N'ACTIVE'),
        (N'runner04', N'123', @runnerRoleId,
            N'Runner Four', N'runner04@demo.local', N'0922000004',
            N'ACTIVE'),
        (N'runner_pending_org', N'123', @runnerRoleId,
            N'Runner Pending Organizer', N'runner_pending_org@demo.local',
            N'0922000005', N'ACTIVE'),
        (N'runner_rejected_org', N'123', @runnerRoleId,
            N'Runner Rejected Organizer', N'runner_rejected_org@demo.local',
            N'0922000006', N'ACTIVE'),
        (N'runner_banned', N'123', @runnerRoleId,
            N'Runner Banned', N'runner_banned@demo.local', N'0922000007',
            N'BANNED'),
        (N'runner_inactive', N'123', @runnerRoleId,
            N'Runner Inactive', N'runner_inactive@demo.local',
            N'0922000008', N'INACTIVE');

    MERGE Users AS target
    USING @DemoUsers AS source
        ON target.userName = source.userName
    WHEN MATCHED THEN
        UPDATE SET
            target.password = source.password,
            target.roleId = source.roleId,
            target.fullName = source.fullName,
            target.email = source.email,
            target.phone = source.phone,
            target.status = source.status
    WHEN NOT MATCHED THEN
        INSERT (
            userName,
            password,
            roleId,
            fullName,
            email,
            phone,
            status
        )
        VALUES (
            source.userName,
            source.password,
            source.roleId,
            source.fullName,
            source.email,
            source.phone,
            source.status
        );

    DECLARE @admin01 INT;
    DECLARE @organizer01 INT;
    DECLARE @organizer02 INT;

    SELECT @admin01 = userId FROM Users WHERE userName = N'admin01';
    SELECT @organizer01 = userId FROM Users WHERE userName = N'organizer01';
    SELECT @organizer02 = userId FROM Users WHERE userName = N'organizer02';

    /* =========================
       RACE
    ========================= */

    DECLARE @Today DATETIME;
    SET @Today = CONVERT(DATETIME, CONVERT(DATE, GETDATE()));

    DECLARE @DemoRaces TABLE (
        raceName NVARCHAR(100) NOT NULL,
        creatorUserName NVARCHAR(50) NOT NULL,
        approverUserName NVARCHAR(50) NULL,
        description NVARCHAR(MAX) NULL,
        createdOffset INT NOT NULL,
        approvedOffset INT NULL,
        startOffset INT NOT NULL,
        endOffset INT NOT NULL,
        deadlineOffset INT NOT NULL,
        location NVARCHAR(255) NULL,
        status NVARCHAR(30) NOT NULL
    );

    INSERT INTO @DemoRaces (
        raceName,
        creatorUserName,
        approverUserName,
        description,
        createdOffset,
        approvedOffset,
        startOffset,
        endOffset,
        deadlineOffset,
        location,
        status
    )
    VALUES
        (N'Demo Open City Marathon 2026', N'organizer01', N'admin01',
            N'Giai dang OPEN, han dang ky nam giua ngay bat dau va ngay ket thuc.',
            -7, -6, 30, 60, 45, N'Ha Noi', N'OPEN'),
        (N'Demo Approved Trail 2026', N'organizer02', N'admin01',
            N'Giai da duoc Admin duyet, con han dang ky.',
            -5, -4, 45, 45, 35, N'Da Nang', N'APPROVED'),
        (N'Demo Pending Admin Review 2026', N'organizer02', NULL,
            N'Giai PENDING de test man hinh Admin duyet giai.',
            -2, NULL, 25, 25, 15, N'Ho Chi Minh', N'PENDING'),
        (N'Demo Rejected Race 2026', N'organizer01', N'admin01',
            N'Giai bi tu choi de Organizer test sua lai thong tin.',
            -10, -9, 50, 50, 40, N'Can Tho', N'REJECTED'),
        (N'Demo Closed Race 2026', N'organizer01', N'admin01',
            N'Giai da dong dang ky, public xem duoc nhung Runner khong dang ky.',
            -40, -39, -10, -10, -15, N'Hai Phong', N'CLOSED'),
        (N'Demo Finished Ranking 2026', N'organizer02', N'admin01',
            N'Giai da ket thuc, co ket qua mau de test bang xep hang.',
            -70, -69, -30, -30, -40, N'Nha Trang', N'FINISHED');

    MERGE Race AS target
    USING (
        SELECT
            race.raceName,
            creator.userId AS createdByUserId,
            approver.userId AS approvedByUserId,
            race.description,
            DATEADD(DAY, race.createdOffset, GETDATE()) AS createdDate,
            CASE
                WHEN race.approvedOffset IS NULL THEN NULL
                ELSE DATEADD(DAY, race.approvedOffset, GETDATE())
            END AS approvedDate,
            DATEADD(HOUR, 6, DATEADD(DAY, race.startOffset, @Today))
                AS startDate,
            DATEADD(HOUR, 12, DATEADD(DAY, race.endOffset, @Today))
                AS endDate,
            DATEADD(HOUR, 23, DATEADD(DAY, race.deadlineOffset, @Today))
                AS registrationDeadline,
            race.location,
            race.status
        FROM @DemoRaces race
        JOIN Users creator
            ON race.creatorUserName = creator.userName
        LEFT JOIN Users approver
            ON race.approverUserName = approver.userName
    ) AS source
        ON target.raceName = source.raceName
    WHEN MATCHED THEN
        UPDATE SET
            target.createdByUserId = source.createdByUserId,
            target.approvedByUserId = source.approvedByUserId,
            target.description = source.description,
            target.createdDate = source.createdDate,
            target.approvedDate = source.approvedDate,
            target.startDate = source.startDate,
            target.endDate = source.endDate,
            target.registrationDeadline = source.registrationDeadline,
            target.location = source.location,
            target.status = source.status
    WHEN NOT MATCHED THEN
        INSERT (
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
            source.createdByUserId,
            source.approvedByUserId,
            source.raceName,
            source.description,
            source.createdDate,
            source.approvedDate,
            source.startDate,
            source.endDate,
            source.registrationDeadline,
            source.location,
            source.status
        );

    /* =========================
       DISTANCE
    ========================= */

    DECLARE @DemoDistances TABLE (
        raceName NVARCHAR(100) NOT NULL,
        distanceName NVARCHAR(50) NOT NULL,
        distanceKm DECIMAL(5, 2) NOT NULL,
        maxParticipant INT NOT NULL,
        registrationFee DECIMAL(12, 2) NOT NULL
    );

    INSERT INTO @DemoDistances (
        raceName,
        distanceName,
        distanceKm,
        maxParticipant,
        registrationFee
    )
    VALUES
        (N'Demo Open City Marathon 2026', N'5K Fun Run', 5.00, 5, 100000),
        (N'Demo Open City Marathon 2026', N'10K Challenge', 10.00, 3,
            150000),
        (N'Demo Open City Marathon 2026', N'21K Half Marathon', 21.10, 1,
            250000),
        (N'Demo Approved Trail 2026', N'15K Trail', 15.00, 2, 180000),
        (N'Demo Approved Trail 2026', N'30K Trail', 30.00, 3, 300000),
        (N'Demo Pending Admin Review 2026', N'5K Pending', 5.00, 100,
            80000),
        (N'Demo Pending Admin Review 2026', N'10K Pending', 10.00, 80,
            120000),
        (N'Demo Rejected Race 2026', N'5K Rejected', 5.00, 50, 50000),
        (N'Demo Closed Race 2026', N'5K Closed', 5.00, 20, 90000),
        (N'Demo Finished Ranking 2026', N'5K Ranking', 5.00, 10, 100000),
        (N'Demo Finished Ranking 2026', N'10K Ranking', 10.00, 10, 150000);

    MERGE DistanceKM AS target
    USING (
        SELECT
            race.raceId,
            distance.distanceName,
            distance.distanceKm,
            distance.maxParticipant,
            distance.registrationFee
        FROM @DemoDistances distance
        JOIN Race race
            ON distance.raceName = race.raceName
    ) AS source
        ON target.raceId = source.raceId
        AND target.distanceName = source.distanceName
    WHEN MATCHED THEN
        UPDATE SET
            target.distanceKm = source.distanceKm,
            target.maxParticipant = source.maxParticipant,
            target.registrationFee = source.registrationFee
    WHEN NOT MATCHED THEN
        INSERT (
            raceId,
            distanceName,
            distanceKm,
            maxParticipant,
            registrationFee
        )
        VALUES (
            source.raceId,
            source.distanceName,
            source.distanceKm,
            source.maxParticipant,
            source.registrationFee
        );

    /* =========================
       REGISTRATION
    ========================= */

    DECLARE @DemoRegistrations TABLE (
        runnerUserName NVARCHAR(50) NOT NULL,
        raceName NVARCHAR(100) NOT NULL,
        distanceName NVARCHAR(50) NOT NULL,
        approverUserName NVARCHAR(50) NULL,
        bibNumber INT NULL,
        status NVARCHAR(30) NOT NULL,
        registerOffset INT NOT NULL,
        approvedOffset INT NULL
    );

    INSERT INTO @DemoRegistrations (
        runnerUserName,
        raceName,
        distanceName,
        approverUserName,
        bibNumber,
        status,
        registerOffset,
        approvedOffset
    )
    VALUES
        (N'runner01', N'Demo Open City Marathon 2026', N'5K Fun Run',
            N'organizer01', 501, N'APPROVED', -3, -2),
        (N'runner02', N'Demo Open City Marathon 2026', N'5K Fun Run',
            NULL, NULL, N'PENDING', -1, NULL),
        (N'runner03', N'Demo Open City Marathon 2026', N'5K Fun Run',
            N'organizer01', NULL, N'REJECTED', -2, -1),
        (N'runner04', N'Demo Open City Marathon 2026', N'5K Fun Run',
            N'organizer01', 502, N'APPROVED', -2, -1),
        (N'runner01', N'Demo Open City Marathon 2026', N'10K Challenge',
            N'organizer01', 1001, N'APPROVED', -4, -3),
        (N'runner02', N'Demo Open City Marathon 2026', N'10K Challenge',
            N'organizer01', 1002, N'APPROVED', -4, -3),
        (N'runner01', N'Demo Open City Marathon 2026',
            N'21K Half Marathon', N'organizer01', 2101, N'APPROVED',
            -5, -4),
        (N'runner02', N'Demo Approved Trail 2026', N'15K Trail',
            N'organizer02', 1501, N'APPROVED', -3, -2),
        (N'runner04', N'Demo Approved Trail 2026', N'15K Trail',
            NULL, NULL, N'PENDING', -1, NULL),
        (N'runner01', N'Demo Finished Ranking 2026', N'5K Ranking',
            N'organizer02', 301, N'APPROVED', -35, -34),
        (N'runner02', N'Demo Finished Ranking 2026', N'5K Ranking',
            N'organizer02', 302, N'APPROVED', -35, -34),
        (N'runner03', N'Demo Finished Ranking 2026', N'5K Ranking',
            N'organizer02', 303, N'APPROVED', -35, -34),
        (N'runner04', N'Demo Finished Ranking 2026', N'5K Ranking',
            N'organizer02', 304, N'APPROVED', -35, -34),
        (N'runner01', N'Demo Finished Ranking 2026', N'10K Ranking',
            N'organizer02', 100, N'APPROVED', -35, -34),
        (N'runner02', N'Demo Finished Ranking 2026', N'10K Ranking',
            N'organizer02', 101, N'APPROVED', -35, -34);

    MERGE Registration AS target
    USING (
        SELECT
            runner.userId AS runnerId,
            distance.distanceId,
            approver.userId AS approvedByUserId,
            registration.bibNumber,
            registration.status,
            DATEADD(DAY, registration.registerOffset, GETDATE())
                AS registerDate,
            CASE
                WHEN registration.approvedOffset IS NULL THEN NULL
                ELSE DATEADD(DAY, registration.approvedOffset, GETDATE())
            END AS approvedDate
        FROM @DemoRegistrations registration
        JOIN Users runner
            ON registration.runnerUserName = runner.userName
        JOIN Race race
            ON registration.raceName = race.raceName
        JOIN DistanceKM distance
            ON race.raceId = distance.raceId
            AND registration.distanceName = distance.distanceName
        LEFT JOIN Users approver
            ON registration.approverUserName = approver.userName
    ) AS source
        ON target.runnerId = source.runnerId
        AND target.distanceId = source.distanceId
    WHEN MATCHED THEN
        UPDATE SET
            target.approvedByUserId = source.approvedByUserId,
            target.bibNumber = source.bibNumber,
            target.status = source.status,
            target.registerDate = source.registerDate,
            target.approvedDate = source.approvedDate
    WHEN NOT MATCHED THEN
        INSERT (
            runnerId,
            distanceId,
            approvedByUserId,
            bibNumber,
            status,
            registerDate,
            approvedDate
        )
        VALUES (
            source.runnerId,
            source.distanceId,
            source.approvedByUserId,
            source.bibNumber,
            source.status,
            source.registerDate,
            source.approvedDate
        );

    /* =========================
       RESULT
    ========================= */

    DECLARE @DemoResults TABLE (
        runnerUserName NVARCHAR(50) NOT NULL,
        raceName NVARCHAR(100) NOT NULL,
        distanceName NVARCHAR(50) NOT NULL,
        ranking INT NULL,
        status NVARCHAR(30) NOT NULL,
        finishTimeText VARCHAR(20) NULL
    );

    INSERT INTO @DemoResults (
        runnerUserName,
        raceName,
        distanceName,
        ranking,
        status,
        finishTimeText
    )
    VALUES
        (N'runner01', N'Demo Finished Ranking 2026', N'5K Ranking',
            1, N'FINISHED', '00:22:15'),
        (N'runner02', N'Demo Finished Ranking 2026', N'5K Ranking',
            2, N'FINISHED', '00:25:42'),
        (N'runner03', N'Demo Finished Ranking 2026', N'5K Ranking',
            NULL, N'DNF', NULL),
        (N'runner04', N'Demo Finished Ranking 2026', N'5K Ranking',
            NULL, N'DNS', NULL),
        (N'runner01', N'Demo Finished Ranking 2026', N'10K Ranking',
            1, N'FINISHED', '00:48:30'),
        (N'runner02', N'Demo Finished Ranking 2026', N'10K Ranking',
            NULL, N'DISQUALIFIED', NULL),
        (N'runner01', N'Demo Open City Marathon 2026', N'5K Fun Run',
            NULL, N'NOT_UPDATED', NULL);

    MERGE RaceResult AS target
    USING (
        SELECT
            registration.registrationId,
            result.ranking,
            result.status,
            CASE
                WHEN result.finishTimeText IS NULL THEN NULL
                ELSE CONVERT(TIME, result.finishTimeText)
            END AS finishTime
        FROM @DemoResults result
        JOIN Users runner
            ON result.runnerUserName = runner.userName
        JOIN Race race
            ON result.raceName = race.raceName
        JOIN DistanceKM distance
            ON race.raceId = distance.raceId
            AND result.distanceName = distance.distanceName
        JOIN Registration registration
            ON runner.userId = registration.runnerId
            AND distance.distanceId = registration.distanceId
    ) AS source
        ON target.registrationId = source.registrationId
    WHEN MATCHED THEN
        UPDATE SET
            target.ranking = source.ranking,
            target.status = source.status,
            target.finishTime = source.finishTime
    WHEN NOT MATCHED THEN
        INSERT (
            registrationId,
            ranking,
            status,
            finishTime
        )
        VALUES (
            source.registrationId,
            source.ranking,
            source.status,
            source.finishTime
        );

    /* =========================
       ORGANIZER REQUEST
    ========================= */

    DECLARE @DemoOrganizerRequests TABLE (
        requesterUserName NVARCHAR(50) NOT NULL,
        approverUserName NVARCHAR(50) NULL,
        reason NVARCHAR(MAX) NULL,
        status NVARCHAR(30) NOT NULL,
        requestOffset INT NOT NULL,
        approvedOffset INT NULL
    );

    INSERT INTO @DemoOrganizerRequests (
        requesterUserName,
        approverUserName,
        reason,
        status,
        requestOffset,
        approvedOffset
    )
    VALUES
        (N'runner_pending_org', NULL,
            N'Toi muon tao giai chay cong dong tai truong.',
            N'PENDING', -1, NULL),
        (N'runner_rejected_org', N'admin01',
            N'Toi chua co du thong tin ve ke hoach to chuc.',
            N'REJECTED', -7, -6),
        (N'organizer_from_request', N'admin01',
            N'Yeu cau mau da duoc duyet de test lich su.',
            N'APPROVED', -20, -19);

    MERGE OrganizerRequest AS target
    USING (
        SELECT
            requester.userId AS requesterId,
            approver.userId AS approverId,
            orgRequest.reason,
            orgRequest.status,
            DATEADD(DAY, orgRequest.requestOffset, GETDATE())
                AS requestDate,
            CASE
                WHEN orgRequest.approvedOffset IS NULL THEN NULL
                ELSE DATEADD(DAY, orgRequest.approvedOffset, GETDATE())
            END AS approvedDate
        FROM @DemoOrganizerRequests orgRequest
        JOIN Users requester
            ON orgRequest.requesterUserName = requester.userName
        LEFT JOIN Users approver
            ON orgRequest.approverUserName = approver.userName
    ) AS source
        ON target.requesterId = source.requesterId
        AND target.status = source.status
    WHEN MATCHED THEN
        UPDATE SET
            target.approverId = source.approverId,
            target.reason = source.reason,
            target.requestDate = source.requestDate,
            target.approvedDate = source.approvedDate
    WHEN NOT MATCHED THEN
        INSERT (
            requesterId,
            approverId,
            reason,
            status,
            requestDate,
            approvedDate
        )
        VALUES (
            source.requesterId,
            source.approverId,
            source.reason,
            source.status,
            source.requestDate,
            source.approvedDate
        );

    COMMIT TRANSACTION;

    PRINT N'Insert demo data thanh cong.';
    PRINT N'Tat ca tai khoan demo deu co mat khau: 123';

    SELECT
        users.userName,
        users.password,
        roles.roleName,
        users.status,
        users.fullName
    FROM Users users
    JOIN Roles roles
        ON users.roleId = roles.roleId
    WHERE users.userName IN (
        N'admin01',
        N'organizer01',
        N'organizer02',
        N'runner01',
        N'runner02',
        N'runner_pending_org',
        N'runner_banned',
        N'runner_inactive'
    )
    ORDER BY roles.roleName, users.userName;

    SELECT
        race.raceName,
        race.status,
        race.startDate,
        race.registrationDeadline,
        COUNT(distance.distanceId) AS distanceCount
    FROM Race race
    LEFT JOIN DistanceKM distance
        ON race.raceId = distance.raceId
    WHERE race.raceName LIKE N'Demo %'
    GROUP BY
        race.raceName,
        race.status,
        race.startDate,
        race.registrationDeadline
    ORDER BY race.startDate;
END TRY
BEGIN CATCH
    DECLARE @ErrorMessage NVARCHAR(4000);
    DECLARE @ErrorSeverity INT;
    DECLARE @ErrorState INT;

    IF @@TRANCOUNT > 0
    BEGIN
        ROLLBACK TRANSACTION;
    END

    SELECT
        @ErrorMessage = ERROR_MESSAGE(),
        @ErrorSeverity = ERROR_SEVERITY(),
        @ErrorState = ERROR_STATE();

    RAISERROR(@ErrorMessage, @ErrorSeverity, @ErrorState);
END CATCH;
GO
