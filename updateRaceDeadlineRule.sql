IF DB_ID(N'MarathonDB') IS NULL
BEGIN
    RAISERROR(N'Can tao database MarathonDB truoc khi chay script nay.',
        16, 1);
END
GO

USE MarathonDB;
GO

SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;

IF EXISTS (
    SELECT 1
    FROM sys.check_constraints
    WHERE name = N'CK_Race_RegistrationDeadline'
      AND parent_object_id = OBJECT_ID(N'dbo.Race')
)
BEGIN
    ALTER TABLE dbo.Race
    DROP CONSTRAINT CK_Race_RegistrationDeadline;
END
GO

ALTER TABLE dbo.Race
ADD CONSTRAINT CK_Race_RegistrationDeadline
    CHECK (registrationDeadline <= endDate);
GO

PRINT N'Da cap nhat rule: han dang ky khong duoc sau ngay ket thuc.';
GO
