<%-- 
    Document   : race-create
    Created on : Jul 18, 2026, 1:47:53 PM
    Author     : Admin
--%>

<%@ page contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    
    
<head>
    <meta charset="UTF-8">

    <title>Tạo giải chạy mới</title>

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
            max-width: 850px;
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

        .form-group {
            margin-bottom: 18px;
        }

        label {
            display: block;
            margin-bottom: 7px;
            font-weight: bold;
        }

        input,
        textarea {
            width: 100%;
            padding: 11px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 15px;
        }

        textarea {
            min-height: 130px;
            resize: vertical;
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
            border: none;
            padding: 11px 18px;
            border-radius: 5px;
            font-size: 15px;
            cursor: pointer;
            text-decoration: none;
        }

        .button-submit {
            color: white;
            background: #198754;
        }

        .button-back {
            color: white;
            background: #6c757d;
        }
    </style>
</head>

<body>

<div class="container">

    <h1>Tạo giải chạy mới</h1>

    <p>
        Người tổ chức:
        <strong>
            <c:out
                value="${sessionScope.user.fullName}" />
        </strong>
    </p>

    <c:if test="${not empty error}">
        <div class="error">
            <c:out value="${error}" />
        </div>
    </c:if>

    <form
        action="${pageContext.request.contextPath}/organizer/race/create"
        method="post">

        <div class="form-group">
            <label for="raceName">
                Tên giải
                <span class="required">*</span>
            </label>

            <input
                type="text"
                id="raceName"
                name="raceName"
                maxlength="100"
                value="<c:out value='${raceNameValue}'/>"
                required>
        </div>

        <div class="form-group">
            <label for="description">
                Mô tả
            </label>

            <textarea
                id="description"
                name="description"
                maxlength="3000"><c:out value="${descriptionValue}" /></textarea>
        </div>

        <div class="form-group">
            <label for="location">
                Địa điểm
                <span class="required">*</span>
            </label>

            <input
                type="text"
                id="location"
                name="location"
                maxlength="255"
                value="<c:out value='${locationValue}'/>"
                required>
        </div>

        <div class="form-group">
            <label for="registrationDeadline">
                Hạn đăng ký
                <span class="required">*</span>
            </label>

            <input
                type="datetime-local"
                id="registrationDeadline"
                name="registrationDeadline"
                value="<c:out value='${registrationDeadlineValue}'/>"
                required>
        </div>

        <div class="form-group">
            <label for="startDate">
                Ngày bắt đầu
                <span class="required">*</span>
            </label>

            <input
                type="datetime-local"
                id="startDate"
                name="startDate"
                value="<c:out value='${startDateValue}'/>"
                required>
        </div>

        <div class="form-group">
            <label for="endDate">
                Ngày kết thúc
                <span class="required">*</span>
            </label>

            <input
                type="datetime-local"
                id="endDate"
                name="endDate"
                value="<c:out value='${endDateValue}'/>"
                required>
        </div>

        <div class="actions">

            <button
                class="button button-submit"
                type="submit">
                Tạo giải
            </button>

            <a
                class="button button-back"
                href="${pageContext.request.contextPath}/organizer/races">
                Quay lại
            </a>

        </div>

    </form>

</div>

</body>
</html>
