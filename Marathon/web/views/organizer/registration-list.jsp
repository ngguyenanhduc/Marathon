<%-- 
    Document   : registration-list
    Created on : Jul 18, 2026, 5:27:52 PM
    Author     : MinhTQHE190232
--%>

<%@ page contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ taglib prefix="c"
           uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">

    <title>
        Runner đăng ký -
        <c:out value="${race.raceName}" />
    </title>

    <style>
        body {
            margin: 0;
            padding: 30px;
            font-family: Arial, sans-serif;
            background: #f5f6fa;
            color: #2c3e50;
        }

        .container {
            max-width: 1350px;
            margin: auto;
            padding: 24px;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 8px
                rgba(0, 0, 0, 0.08);
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            gap: 20px;
        }

        .header h1 {
            margin-top: 0;
            margin-bottom: 8px;
        }

        .sub-title {
            margin-top: 0;
            color: #666;
        }

        .filter-form {
            display: flex;
            align-items: flex-end;
            flex-wrap: wrap;
            gap: 14px;
            margin-top: 24px;
            padding: 18px;
            background: #f8f9fa;
            border: 1px solid #ddd;
            border-radius: 6px;
        }

        .form-group {
            display: flex;
            flex-direction: column;
            min-width: 200px;
        }

        .form-group label {
            margin-bottom: 6px;
            font-weight: bold;
        }

        select {
            padding: 9px 10px;
            border: 1px solid #bbb;
            border-radius: 4px;
            background: white;
        }

        table {
            width: 100%;
            margin-top: 22px;
            border-collapse: collapse;
        }

        th,
        td {
            padding: 11px;
            border: 1px solid #ddd;
            text-align: left;
            vertical-align: middle;
        }

        th {
            background: #f0f0f0;
        }

        .button {
            display: inline-block;
            padding: 9px 13px;
            border: none;
            border-radius: 4px;
            color: white;
            background: #1976d2;
            text-decoration: none;
            cursor: pointer;
        }

        .button-secondary {
            background: #6c757d;
        }

        .button-reset {
            background: #7f8c8d;
        }

        .button-approve {
            background: #198754;
        }

        .button-reject {
            background: #dc3545;
        }

        .status-badge {
            display: inline-block;
            padding: 5px 9px;
            border-radius: 12px;
            font-size: 13px;
            font-weight: bold;
        }

        .status-pending {
            color: #664d03;
            background: #fff3cd;
        }

        .status-approved {
            color: #0f5132;
            background: #d1e7dd;
        }

        .status-rejected {
            color: #842029;
            background: #f8d7da;
        }

        .status-cancelled {
            color: #41464b;
            background: #e2e3e5;
        }

        .empty {
            margin-top: 22px;
            padding: 28px;
            text-align: center;
            color: #666;
            background: #f8f9fa;
            border: 1px solid #ddd;
            border-radius: 6px;
        }

        .null-value {
            color: #888;
        }

        .action-cell {
            min-width: 160px;
        }

        .summary {
            margin-top: 18px;
            font-weight: bold;
        }
        
        .message {
            margin-top: 18px;
            padding: 12px 14px;
            border-radius: 5px;
            font-weight: bold;
        }

        .message-success {
            color: #0f5132;
            background: #d1e7dd;
            border: 1px solid #badbcc;
        }

        .message-error {
            color: #842029;
            background: #f8d7da;
            border: 1px solid #f5c2c7;
        }
        
        .button-approve {
            background-color: #198754;
            color: white;
            border: none;
        }

        .button-approve:hover {
            background-color: #157347;
        }
    </style>
</head>

<body>

<div class="container">

    <div class="header">
        
        <c:if test="${not empty sessionScope.successMessage}">

            <div class="message message-success">
                <c:out value="${sessionScope.successMessage}" />
            </div>

            <c:remove var="successMessage"
                      scope="session" />

        </c:if>

        <c:if test="${not empty sessionScope.errorMessage}">

            <div class="message message-error">
                <c:out value="${sessionScope.errorMessage}" />
            </div>

            <c:remove var="errorMessage"
                      scope="session" />

        </c:if>
        
        <div>
            <h1>Danh sách runner đăng ký</h1>

            <p class="sub-title">
                Giải:
                <strong>
                    <c:out value="${race.raceName}" />
                </strong>
            </p>
        </div>

        <a class="button button-secondary"
           href="${pageContext.request.contextPath}/organizer/races">
            Quay lại danh sách giải
        </a>

    </div>

    <%-- Form lọc --%>
    <form class="filter-form"
          method="get"
          action="${pageContext.request.contextPath}/organizer/registrations">

        <input type="hidden"
               name="raceId"
               value="${race.raceId}">

        <div class="form-group">
            <label for="distanceId">Cự ly</label>

            <select id="distanceId"
                    name="distanceId">

                <option value="">
                    Tất cả cự ly
                </option>

                <c:forEach var="distance"
                           items="${distances}">

                    <option
                        value="${distance.distanceId}"
                        <c:if test="${selectedDistanceId == distance.distanceId}">
                            selected
                        </c:if>>

                        <c:out
                            value="${distance.distanceName}" />

                        -
                        <c:out
                            value="${distance.distanceKm}" /> km
                    </option>

                </c:forEach>

            </select>
        </div>

        <div class="form-group">
            <label for="status">Trạng thái</label>

            <select id="status"
                    name="status">

                <option value=""
                    ${empty selectedStatus
                        ? 'selected' : ''}>
                    Tất cả trạng thái
                </option>

                <option value="PENDING"
                    ${selectedStatus == 'PENDING'
                        ? 'selected' : ''}>
                    Chờ duyệt
                </option>

                <option value="APPROVED"
                    ${selectedStatus == 'APPROVED'
                        ? 'selected' : ''}>
                    Đã duyệt
                </option>

                <option value="REJECTED"
                    ${selectedStatus == 'REJECTED'
                        ? 'selected' : ''}>
                    Đã từ chối
                </option>

                <option value="CANCELLED"
                    ${selectedStatus == 'CANCELLED'
                        ? 'selected' : ''}>
                    Đã hủy
                </option>

            </select>
        </div>

        <button type="submit"
                class="button">
            Lọc
        </button>

        <a class="button button-reset"
           href="${pageContext.request.contextPath}/organizer/registrations?raceId=${race.raceId}">
            Xóa lọc
        </a>

    </form>

    <div class="summary">
        Số kết quả:
        <c:out value="${registrations.size()}" />
    </div>

    <c:choose>

        <c:when test="${empty registrations}">

            <div class="empty">
                Không có runner nào phù hợp.
            </div>

        </c:when>

        <c:otherwise>

            <table>

                <thead>
                <tr>
                    <th>STT</th>
                    <th>Runner</th>
                    <th>Email</th>
                    <th>Số điện thoại</th>
                    <th>Cự ly</th>
                    <th>Ngày đăng ký</th>
                    <th>Trạng thái</th>
                    <th>BIB</th>
                    <th>Thao tác</th>
                </tr>
                </thead>

                <tbody>

                <c:forEach
                    var="registration"
                    items="${registrations}"
                    varStatus="loop">

                    <tr>

                        <td>
                            <c:out value="${loop.count}" />
                        </td>

                        <td>
                            <c:out
                                value="${registration.runnerName}" />
                        </td>

                        <td>
                            <c:choose>
                                <c:when test="${not empty registration.runnerEmail}">
                                    <c:out
                                        value="${registration.runnerEmail}" />
                                </c:when>

                                <c:otherwise>
                                    <span class="null-value">
                                        Chưa cập nhật
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </td>

                        <td>
                            <c:choose>
                                <c:when test="${not empty registration.runnerPhone}">
                                    <c:out
                                        value="${registration.runnerPhone}" />
                                </c:when>

                                <c:otherwise>
                                    <span class="null-value">
                                        Chưa cập nhật
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </td>

                        <td>
                            <c:out
                                value="${registration.distanceName}" />
                        </td>

                        <td>
                            <c:out
                                value="${registration.registerDate}" />
                        </td>

                        <td>
                            <c:choose>

                                <c:when test="${registration.status == 'PENDING'}">
                                    <span class="status-badge status-pending">
                                        Chờ duyệt
                                    </span>
                                </c:when>

                                <c:when test="${registration.status == 'APPROVED'}">
                                    <span class="status-badge status-approved">
                                        Đã duyệt
                                    </span>
                                </c:when>

                                <c:when test="${registration.status == 'REJECTED'}">
                                    <span class="status-badge status-rejected">
                                        Đã từ chối
                                    </span>
                                </c:when>

                                <c:when test="${registration.status == 'CANCELLED'}">
                                    <span class="status-badge status-cancelled">
                                        Đã hủy
                                    </span>
                                </c:when>

                                <c:otherwise>
                                    <c:out
                                        value="${registration.status}" />
                                </c:otherwise>

                            </c:choose>
                        </td>

                        <td>
                            <c:choose>

                                <c:when test="${not empty registration.bibNumber}">
                                    <c:out
                                        value="${registration.bibNumber}" />
                                </c:when>

                                <c:otherwise>
                                    <span class="null-value">
                                        Chưa cấp
                                    </span>
                                </c:otherwise>

                            </c:choose>
                        </td>

                        <td class="action-cell">

                            <c:choose>

                            <c:when test="${registration.status == 'PENDING'}">

                                <form method="post"
                                      action="${pageContext.request.contextPath}/organizer/registration/reject"
                                      style="display: inline;">

                                    <input type="hidden"
                                           name="registrationId"
                                           value="${registration.registrationId}">

                                    <button type="submit"
                                            class="button button-reject"
                                            onclick="return confirm(
                                                'Bạn có chắc muốn từ chối đăng ký của runner này?'
                                            );">

                                        Reject

                                    </button>

                                </form>
                                           
                                           <form method="post"
                                        action="${pageContext.request.contextPath}/organizer/registration/approve"
                                        style="display: inline;">

                                      <input type="hidden"
                                             name="registrationId"
                                             value="${registration.registrationId}">

                                      <button type="submit"
                                              class="button button-approve"
                                              onclick="return confirm('Bạn có chắc muốn duyệt đăng ký này? Hệ thống sẽ tự động cấp số BIB.');">
                                          Approve
                                      </button>

                                  </form>

                                </c:when>

                                <c:otherwise>
                                    <span class="null-value">
                                        Đã xử lý
                                    </span>
                                </c:otherwise>

                            </c:choose>

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