<%-- 
    Document   : race-list
    Created on : Jul 18, 2026, 12:57:10 PM
    Author     : MinhTQHE190232
--%>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:if test="${not empty sessionScope.successMessage}">
    <div class="success">
        <c:out
            value="${sessionScope.successMessage}" />
    </div>

    <c:remove
        var="successMessage"
        scope="session" />
</c:if>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Giải chạy của tôi</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 30px;
            background: #f5f6fa;
        }

        .container {
            max-width: 1200px;
            margin: auto;
            background: white;
            padding: 24px;
            border-radius: 8px;
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th,
        td {
            border: 1px solid #ddd;
            padding: 10px;
            text-align: left;
        }

        th {
            background: #f0f0f0;
        }

        .button {
            display: inline-block;
            padding: 8px 12px;
            text-decoration: none;
            border-radius: 4px;
            background: #1976d2;
            color: white;
            margin-right: 4px;
        }

        .button-edit {
            background: #f39c12;
        }

        .button-create {
            background: #27ae60;
        }

        .status {
            font-weight: bold;
        }

        .empty {
            padding: 20px;
            text-align: center;
            color: #666;
        }
        
        .success {
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

<jsp:include page="/views/organizer/organizer-nav.jsp" />

<div class="container">

    <div class="header">
        <div>
            <h1>Giải chạy của tôi</h1>

            <p>
                Xin chào,
                <strong>
                    <c:out value="${sessionScope.user.fullName}" />
                </strong>
            </p>
        </div>

        <a class="button button-create"
           href="${pageContext.request.contextPath}/organizer/race/create">
            Tạo giải mới
        </a>
    </div>

    <c:choose>

        <c:when test="${empty races}">
            <div class="empty">
                Bạn chưa tạo giải chạy nào.
            </div>
        </c:when>

        <c:otherwise>

            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Tên giải</th>
                    <th>Địa điểm</th>
                    <th>Ngày bắt đầu</th>
                    <th>Hạn đăng ký</th>
                    <th>Trạng thái</th>
                    <th>Số cự ly</th>
                    <th>Số đăng ký</th>
                    <th>Thao tác</th>
                </tr>
                </thead>

                <tbody>

                <c:forEach var="race" items="${races}">
                    <tr>
                        <td>
                            <c:out value="${race.raceId}" />
                        </td>

                        <td>
                            <c:out value="${race.raceName}" />
                        </td>

                        <td>
                            <c:out value="${race.location}" />
                        </td>

                        <td>
                            <c:out value="${race.startDate}" />
                        </td>

                        <td>
                            <c:out value="${race.registrationDeadline}" />
                        </td>

                        <td class="status">
                            <c:out value="${race.status}" />
                        </td>

                        <td>
                            <c:out value="${race.distanceCount}" />
                        </td>

                        <td>
                            <c:out value="${race.registrationCount}" />
                        </td>

                        <td>
                            <a class="button"
                               href="${pageContext.request.contextPath}/organizer/race/detail?raceId=${race.raceId}">
                                Chi tiết
                            </a>

                            <c:if test="${race.status == 'PENDING'
                                           || race.status == 'REJECTED'}">
                                <a class="button button-edit"
                                   href="${pageContext.request.contextPath}/organizer/race/edit?raceId=${race.raceId}">
                                    Sửa
                                </a>
                            </c:if>

                            <a class="button"
                               href="${pageContext.request.contextPath}/organizer/distances?raceId=${race.raceId}">
                                Cự ly
                            </a>

                            <a class="button"
                               href="${pageContext.request.contextPath}/organizer/registrations?raceId=${race.raceId}">
                                Runner
                            </a>
                        </td>
                    </tr>
                </c:forEach>

                </tbody>
            </table>

        </c:otherwise>

    </c:choose>

</div>

</body>
</html>
