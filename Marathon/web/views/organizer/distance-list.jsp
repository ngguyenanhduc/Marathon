<%-- 
    Document   : distance-list
    Created on : Jul 18, 2026, 3:08:38 PM
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
    <title>Quản lý cự ly</title>

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
            max-width: 1150px;
            margin: auto;
            padding: 30px;
            background: white;
            border-radius: 10px;
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 20px;
        }

        .header-actions {
            display: flex;
            gap: 8px;
        }

        table {
            width: 100%;
            margin-top: 25px;
            border-collapse: collapse;
        }

        th,
        td {
            padding: 11px;
            border: 1px solid #ddd;
            text-align: left;
        }

        th {
            background: #f0f0f0;
        }

        .button {
            display: inline-block;
            padding: 9px 13px;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            border: none;
            cursor: pointer;
        }

        .button-create {
            background: #198754;
        }

        .button-edit {
            background: #f39c12;
        }

        .button-delete {
            background: #dc3545;
        }

        .button-back {
            background: #6c757d;
        }

        .empty {
            margin-top: 25px;
            padding: 25px;
            text-align: center;
            color: #666;
            background: #f8f9fa;
        }

        .success-message {
            margin-top: 15px;
            padding: 12px;
            color: #0f5132;
            background: #d1e7dd;
            border: 1px solid #badbcc;
            border-radius: 5px;
        }
        
        .error-message {
            margin-top: 15px;
            padding: 12px;
            color: #842029;
            background: #f8d7da;
            border: 1px solid #f5c2c7;
            border-radius: 5px;
        }
        
    </style>
</head>

<body>

<jsp:include page="/views/organizer/organizer-nav.jsp" />

<div class="container">

    <div class="header">

        <div>
            <h1>Quản lý cự ly</h1>

            <p>
                Giải:
                <strong>
                    <c:out value="${race.raceName}" />
                </strong>
            </p>

            <p>
                Trạng thái:
                <strong>
                    <c:out value="${race.status}" />
                </strong>
            </p>
        </div>

        <div class="header-actions">

            <c:if test="${race.status == 'PENDING'
                        || race.status == 'REJECTED'}">

                <a class="button button-create"
                   href="${pageContext.request.contextPath}/organizer/distance/create?raceId=${race.raceId}">
                    Thêm cự ly
                </a>

            </c:if>

            <a class="button button-back"
               href="${pageContext.request.contextPath}/organizer/race/detail?raceId=${race.raceId}">
                Quay lại
            </a>

        </div>

    </div>

    <c:if test="${not empty sessionScope.successMessage}">
        <div class="success-message">
            <c:out value="${sessionScope.successMessage}" />
        </div>

        <c:remove
            var="successMessage"
            scope="session" />
    </c:if>
               
    <c:if test="${not empty sessionScope.errorMessage}">
        <div class="error-message">
            <c:out value="${sessionScope.errorMessage}" />
        </div>

    <c:remove
        var="errorMessage"
        scope="session" />
    </c:if>

    <c:choose>

        <c:when test="${empty distances}">
            <div class="empty">
                Giải này chưa có cự ly nào.
            </div>
        </c:when>

        <c:otherwise>

            <table>
                <thead>
                <tr>
                    <th>STT</th>
                    <th>Tên cự ly</th>
                    <th>Kilomet</th>
                    <th>Tối đa</th>
                    <th>Đã duyệt</th>
                    <th>Đang chờ</th>
                    <th>Thao tác</th>
                </tr>
                </thead>

                <tbody>

                    <c:forEach
                        var="distance"
                        items="${distances}"
                        varStatus="status">

                    <tr>
                        <td>
                            <c:out value="${status.count}" />
                        </td>

                        <td>
                            <c:out value="${distance.distanceName}" />
                        </td>

                        <td>
                            <c:out value="${distance.distanceKm}" />
                            km
                        </td>

                        <td>
                            <c:out value="${distance.maxParticipant}" />
                        </td>

                        <td>
                            <c:out value="${distance.approvedRegistrationCount}" />
                        </td>

                        <td>
                            <c:out value="${distance.pendingRegistrationCount}" />
                        </td>

                        <td>
                            <c:if test="${race.status == 'PENDING'

                                        || race.status == 'REJECTED'}">



                                <a class="button button-edit"

                                   href="${pageContext.request.contextPath}/organizer/distance/edit?distanceId=${distance.distanceId}">

                                    Sửa

                                </a>



                                <form

                                    action="${pageContext.request.contextPath}/organizer/distance/delete"

                                    method="post"

                                    style="display: inline;"

                                    onsubmit="return confirm('Bạn có chắc muốn xóa cự ly này không?');">



                                    <input

                                        type="hidden"

                                        name="distanceId"

                                        value="${distance.distanceId}">

                                    <button
                                        type="submit"
                                        class="button button-delete">
                                        Xóa
                                    </button>
                                </form>
                            </c:if>
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
