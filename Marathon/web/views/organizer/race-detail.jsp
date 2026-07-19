<%-- 
    Document   : race-detail
    Created on : Jul 18, 2026, 2:17:44 PM
    Author     : MinhTQHE190232
--%>

<%@ page contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết giải chạy</title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            padding: 30px;
            font-family: Arial, sans-serif;
            background: #f4f6f9;
        }

        .container {
            max-width: 950px;
            margin: auto;
            padding: 30px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 2px 10px
                        rgba(0, 0, 0, 0.08);
        }

        h1 {
            margin-top: 0;
        }

        .detail-grid {
            display: grid;
            grid-template-columns: 220px 1fr;
            gap: 12px;
            margin-top: 25px;
        }

        .label {
            font-weight: bold;
            color: #444;
        }

        .value {
            color: #222;
            word-break: break-word;
        }

        .description {
            white-space: pre-wrap;
        }

        .status {
            display: inline-block;
            padding: 5px 10px;
            border-radius: 15px;
            font-weight: bold;
            background: #fff3cd;
            color: #664d03;
        }

        .actions {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            margin-top: 30px;
        }

        .button {
            display: inline-block;
            padding: 10px 16px;
            border-radius: 5px;
            color: white;
            text-decoration: none;
            background: #1976d2;
        }

        .button-edit {
            background: #f39c12;
        }

        .button-distance {
            background: #6f42c1;
        }

        .button-runner {
            background: #0d6efd;
        }

        .button-back {
            background: #6c757d;
        }
        
        .success-message {
            margin: 15px 0;
            padding: 12px;
            color: #0f5132;
            background: #d1e7dd;
            border: 1px solid #badbcc;
            border-radius: 5px;
        }

    </style>
</head>

<body>

<div class="container">

    <h1>
        <c:out value="${race.raceName}" />
    </h1>
    
    <c:if test="${not empty sessionScope.successMessage}">
    <div class="success-message">
        <c:out value="${sessionScope.successMessage}" />
    </div>

    <c:remove
        var="successMessage"
        scope="session" />
</c:if>

    <div class="detail-grid">

        <div class="label">Mã giải</div>
        <div class="value">
            <c:out value="${race.raceId}" />
        </div>

        <div class="label">Người tạo</div>
        <div class="value">
            <c:out value="${sessionScope.user.fullName}" />
        </div>

        <div class="label">Địa điểm</div>
        <div class="value">
            <c:out value="${race.location}" />
        </div>

        <div class="label">Ngày tạo</div>
        <div class="value">
            <c:out value="${race.createdDate}" />
        </div>

        <div class="label">Hạn đăng ký</div>
        <div class="value">
            <c:out value="${race.registrationDeadline}" />
        </div>

        <div class="label">Ngày bắt đầu</div>
        <div class="value">
            <c:out value="${race.startDate}" />
        </div>

        <div class="label">Ngày kết thúc</div>
        <div class="value">
            <c:out value="${race.endDate}" />
        </div>

        <div class="label">Trạng thái</div>
        <div class="value">
            <span class="status">
                <c:out value="${race.status}" />
            </span>
        </div>

        <div class="label">Số cự ly</div>
        <div class="value">
            <c:out value="${race.distanceCount}" />
        </div>

        <div class="label">Số runner đăng ký</div>
        <div class="value">
            <c:out value="${race.registrationCount}" />
        </div>

        <div class="label">Mô tả</div>
        <div class="value description">
            <c:choose>
                <c:when test="${empty race.description}">
                    Chưa có mô tả.
                </c:when>

                <c:otherwise>
                    <c:out value="${race.description}" />
                </c:otherwise>
            </c:choose>
        </div>

    </div>

    <div class="actions">

        <a class="button button-back"
           href="${pageContext.request.contextPath}/organizer/races">
            Quay lại
        </a>

        <c:if test="${race.status == 'PENDING'
                    || race.status == 'REJECTED'}">

            <a class="button button-edit"
               href="${pageContext.request.contextPath}/organizer/race/edit?raceId=${race.raceId}">
                Sửa giải
            </a>

        </c:if>

        <a class="button button-distance"
           href="${pageContext.request.contextPath}/organizer/distances?raceId=${race.raceId}">
            Quản lý cự ly
        </a>

        <a class="button button-runner"
           href="${pageContext.request.contextPath}/organizer/registrations?raceId=${race.raceId}">
            Danh sách runner
        </a>

    </div>

</div>

</body>
</html>
