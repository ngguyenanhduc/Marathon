<%-- 
    Document   : distance-create
    Created on : Jul 18, 2026, 3:44:06 PM
    Author     : MinhTQHE19032
--%>

<%@ page contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thêm cự ly</title>

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
            max-width: 750px;
            margin: auto;
            padding: 30px;
            background: white;
            border-radius: 10px;
        }

        .form-group {
            margin-bottom: 18px;
        }

        label {
            display: block;
            margin-bottom: 7px;
            font-weight: bold;
        }

        input {
            width: 100%;
            padding: 11px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        .error {
            margin-bottom: 20px;
            padding: 12px;
            color: #842029;
            background: #f8d7da;
            border: 1px solid #f5c2c7;
            border-radius: 5px;
        }

        .actions {
            display: flex;
            gap: 10px;
            margin-top: 25px;
        }

        .button {
            display: inline-block;
            padding: 11px 18px;
            border: none;
            border-radius: 5px;
            color: white;
            text-decoration: none;
            cursor: pointer;
        }

        .button-submit {
            background: #198754;
        }

        .button-back {
            background: #6c757d;
        }

        .required {
            color: red;
        }
    </style>
</head>

<body>

<jsp:include page="/views/organizer/organizer-nav.jsp" />

<div class="container">

    <h1>Thêm cự ly</h1>

    <p>
        Giải:
        <strong>
            <c:out value="${race.raceName}" />
        </strong>
    </p>

    <c:if test="${not empty error}">
        <div class="error">
            <c:out value="${error}" />
        </div>
    </c:if>

    <form
        action="${pageContext.request.contextPath}/organizer/distance/create"
        method="post">

        <input
            type="hidden"
            name="raceId"
            value="${race.raceId}">

        <div class="form-group">
            <label for="distanceName">
                Tên cự ly
                <span class="required">*</span>
            </label>

            <input
                type="text"
                id="distanceName"
                name="distanceName"
                maxlength="50"
                placeholder="Ví dụ: 5K"
                value="<c:out value='${distance.distanceName}'/>"
                required>
        </div>

        <div class="form-group">
            <label for="distanceKm">
                Số kilomet
                <span class="required">*</span>
            </label>

            <input
                type="number"
                id="distanceKm"
                name="distanceKm"
                min="0.01"
                step="0.01"
                placeholder="Ví dụ: 5"
                value="<c:out value='${distanceKmValue}'/>"
                required>
        </div>

        <div class="form-group">
            <label for="maxParticipant">
                Số người tối đa
                <span class="required">*</span>
            </label>

            <input
                type="number"
                id="maxParticipant"
                name="maxParticipant"
                min="1"
                step="1"
                value="<c:out value='${maxParticipantValue}'/>"
                required>
        </div>

        <div class="actions">

            <button
                class="button button-submit"
                type="submit">
                Thêm cự ly
            </button>

            <a class="button button-back"
               href="${pageContext.request.contextPath}/organizer/distances?raceId=${race.raceId}">
                Hủy
            </a>

        </div>

    </form>

</div>

</body>
</html>
