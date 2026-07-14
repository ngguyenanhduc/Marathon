CREATE DATABASE MarathonDB;
GO

USE MarathonDB;
GO

/* =========================
   ROLE
========================= */

CREATE TABLE Roles (
    roleId INT IDENTITY(1,1) PRIMARY KEY,
    roleName NVARCHAR(20) NOT NULL UNIQUE,

    CONSTRAINT CK_Roles_RoleName
        CHECK (roleName IN ('ADMIN', 'ORGANIZER', 'RUNNER'))
);

INSERT INTO Roles(roleName)
VALUES 
('ADMIN'),
('ORGANIZER'),
('RUNNER');


/* =========================
   USERS
========================= */

CREATE TABLE Users (
    userId INT IDENTITY(1,1) PRIMARY KEY,

    userName NVARCHAR(50) NOT NULL UNIQUE,
    password NVARCHAR(255) NOT NULL,

    roleId INT NOT NULL,

    fullName NVARCHAR(100) NOT NULL,
    email NVARCHAR(100) NOT NULL UNIQUE,
    phone NVARCHAR(20),

    status NVARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    createdAt DATETIME DEFAULT GETDATE(),

    CONSTRAINT FK_Users_Roles
        FOREIGN KEY (roleId)
        REFERENCES Roles(roleId),

    CONSTRAINT CK_Users_Status
        CHECK (status IN ('ACTIVE', 'INACTIVE', 'BANNED'))
);


/* =========================
   RACE / GIẢI CHẠY
========================= */

CREATE TABLE Race (
    raceId INT IDENTITY(1,1) PRIMARY KEY,

    createdByUserId INT NOT NULL,
    approvedByUserId INT NULL,

    raceName NVARCHAR(100) NOT NULL,
    description NVARCHAR(MAX),

    createdDate DATETIME DEFAULT GETDATE(),
    approvedDate DATETIME NULL,

    startDate DATETIME NOT NULL,
    endDate DATETIME NOT NULL,
    registrationDeadline DATETIME NOT NULL,

    location NVARCHAR(255),

    status NVARCHAR(30) NOT NULL DEFAULT 'PENDING',

    CONSTRAINT FK_Race_Creator
        FOREIGN KEY (createdByUserId)
        REFERENCES Users(userId),

    CONSTRAINT FK_Race_Approver
        FOREIGN KEY (approvedByUserId)
        REFERENCES Users(userId),

    CONSTRAINT CK_Race_Status
        CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED', 'OPEN', 'CLOSED', 'FINISHED')),

    CONSTRAINT CK_Race_Date
        CHECK (endDate >= startDate),

    CONSTRAINT CK_Race_RegistrationDeadline
        CHECK (registrationDeadline <= startDate)
);


/* =========================
   DISTANCE / CỰ LY
========================= */

CREATE TABLE DistanceKM (
    distanceId INT IDENTITY(1,1) PRIMARY KEY,

    raceId INT NOT NULL,

    distanceName NVARCHAR(50) NOT NULL,
    distanceKm DECIMAL(5,2) NOT NULL,

    maxParticipant INT NOT NULL,

    registrationFee DECIMAL(12,2) NOT NULL DEFAULT 0,

    CONSTRAINT FK_DistanceKM_Race
        FOREIGN KEY (raceId)
        REFERENCES Race(raceId),

    CONSTRAINT CK_DistanceKM_Distance
        CHECK (distanceKm > 0),

    CONSTRAINT CK_DistanceKM_MaxParticipant
        CHECK (maxParticipant > 0),

    CONSTRAINT CK_DistanceKM_RegistrationFee
        CHECK (registrationFee >= 0)
);


/* =========================
   REGISTRATION / ĐĂNG KÝ CHẠY
========================= */

CREATE TABLE Registration (
    registrationId INT IDENTITY(1,1) PRIMARY KEY,

    runnerId INT NOT NULL,
    distanceId INT NOT NULL,

    approvedByUserId INT NULL,

    bibNumber INT NULL,

    status NVARCHAR(30) NOT NULL DEFAULT 'PENDING',

    registerDate DATETIME DEFAULT GETDATE(),
    approvedDate DATETIME NULL,

    CONSTRAINT FK_Registration_Runner
        FOREIGN KEY (runnerId)
        REFERENCES Users(userId),

    CONSTRAINT FK_Registration_Distance
        FOREIGN KEY (distanceId)
        REFERENCES DistanceKM(distanceId),

    CONSTRAINT FK_Registration_Approver
        FOREIGN KEY (approvedByUserId)
        REFERENCES Users(userId),

    CONSTRAINT CK_Registration_Status
        CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED')),

    CONSTRAINT UQ_Registration_Runner_Distance
        UNIQUE (runnerId, distanceId)
);

-- Không cho trùng số BIB trong cùng một cự ly
-- Cho phép bibNumber NULL khi đơn chưa được duyệt
CREATE UNIQUE INDEX UQ_Registration_Distance_Bib
ON Registration(distanceId, bibNumber)
WHERE bibNumber IS NOT NULL;


/* =========================
   RESULT / KẾT QUẢ
========================= */

CREATE TABLE RaceResult (
    resultId INT IDENTITY(1,1) PRIMARY KEY,

    registrationId INT NOT NULL UNIQUE,

    ranking INT NULL,

    status NVARCHAR(30) NOT NULL DEFAULT 'NOT_UPDATED',

    finishTime TIME NULL,

    CONSTRAINT FK_RaceResult_Registration
        FOREIGN KEY (registrationId)
        REFERENCES Registration(registrationId),

    CONSTRAINT CK_RaceResult_Ranking
        CHECK (ranking IS NULL OR ranking > 0),

    CONSTRAINT CK_RaceResult_Status
        CHECK (status IN ('NOT_UPDATED', 'FINISHED', 'DNF', 'DNS', 'DISQUALIFIED'))
);


/* =========================
   ORGANIZER REQUEST
   YÊU CẦU LÀM NHÀ TỔ CHỨC
========================= */

CREATE TABLE OrganizerRequest (
    requestId INT IDENTITY(1,1) PRIMARY KEY,

    requesterId INT NOT NULL,
    approverId INT NULL,

    reason NVARCHAR(MAX) NULL,

    status NVARCHAR(30) NOT NULL DEFAULT 'PENDING',

    requestDate DATETIME DEFAULT GETDATE(),
    approvedDate DATETIME NULL,

    CONSTRAINT FK_OrganizerRequest_Requester
        FOREIGN KEY (requesterId)
        REFERENCES Users(userId),

    CONSTRAINT FK_OrganizerRequest_Approver
        FOREIGN KEY (approverId)
        REFERENCES Users(userId),

    CONSTRAINT CK_OrganizerRequest_Status
        CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED'))
);
