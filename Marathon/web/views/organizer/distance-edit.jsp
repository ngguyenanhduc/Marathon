<%-- 
    Document   : distance-edit
    Created on : Jul 18, 2026, 4:36:13 PM
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
    <title>Sửa cự ly</title>

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
            max-width: 700px;
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

        .race-info {
            margin-bottom: 25px;
            padding: 15px;
            background: #f8f9fa;
            border-radius: 5px;
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
            font-size: 15px;
        }

        .required {
            color: red;
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
            font-size: 15px;
            text-decoration: none;
            cursor: pointer;
        }

        .button-submit {
            color: white;
            background: #f39c12;
        }

        .button-back {
            color: white;
            background: #6c757d;
        }
    </style>
</head>

<body>

<jsp:include page="/views/organizer/organizer-nav.jsp" />

<div class="container">

    <h1>Sửa cự ly</h1>

    <div class="race-info">
        Giải:
        <strong>
            <c:out value="${distance.raceName}" />
        </strong>
    </div>

    <c:if test="${not empty error}">
        <div class="error">
            <c:out value="${error}" />
        </div>
    </c:if>

    <form
        action="${pageContext.request.contextPath}/organizer/distance/edit"
        method="post">

        <input
            type="hidden"
            name="distanceId"
            value="<c:out value='${distance.distanceId}'/>">

        <div class="form-group">
            <label for="distanceName">
                Tên cự ly
                <span class="required">*</span>
            </label>

            <input
                type="text"
                id="distanceName"
                name="distanceName"
                maxlength="100"
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
                value="<c:out value='${not empty distanceKmValue
                        ? distanceKmValue
                        : distance.distanceKm}'/>"
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
                value="<c:out value='${not empty maxParticipantValue
                        ? maxParticipantValue
                        : distance.maxParticipant}'/>"
                required>
        </div>

        <div class="actions">

            <button
                class="button button-submit"
                type="submit">
                Lưu thay đổi
            </button>

            <a class="button button-back"
               href="${pageContext.request.contextPath}/organizer/distances?raceId=${distance.raceId}">
                Hủy
            </a>

        </div>

    </form>

</div>

</body>
</html>
