<%-- 
    Document   : input-results
    Created on : Jul 19, 2026, 6:05:11 PM
    Author     : anhdu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
       <title>Quản Trị Viên - Nhập Kết Quả Chạy</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f4f7f6;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .container {
            background-color: #ffffff;
            padding: 40px;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 500px;
            box-sizing: border-box;
        }
        h2 {
            text-align: center;
            color: #007bff;
            margin-bottom: 30px;
        }
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            font-weight: 600;
            margin-bottom: 8px;
            color: #333;
        }
        input[type="text"] {
            width: 100%;
            padding: 12px;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 14px;
            box-sizing: border-box;
            transition: border-color 0.3s;
        }
        input[type="text"]:focus {
            border-color: #007bff;
            outline: none;
        }
        .btn-submit {
            width: 100%;
            padding: 14px;
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            transition: background-color 0.3s;
            margin-top: 10px;
        }
        .btn-submit:hover {
            background-color: #218838;
        }
        .alert {
            padding: 12px;
            border-radius: 4px;
            margin-bottom: 20px;
            text-align: center;
            font-weight: 500;
            font-size: 14px;
        }
        .alert-info {
            background-color: #d1ecf1;
            color: #0c5460;
            border: 1px solid #bee5eb;
        }
        .back-link {
            display: block;
            text-align: center;
            margin-top: 20px;
            color: #007bff;
            text-decoration: none;
            font-size: 14px;
        }
        .back-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<div class="container">
    <h2>Quản Trị Viên - Nhập Kết Quả</h2>

    <!-- Hiển thị thông báo phản hồi từ Servlet nếu có -->
    <% 
        String message = (String) request.getAttribute("message");
        if (message != null && !message.isEmpty()) {
    %>
        <div class="alert alert-info">
            <%= message %>
        </div>
    <% 
        } 
    %>

    <!-- Form gửi dữ liệu POST đến đúng đường dẫn Servlet đã map -->
    <form action="${pageContext.request.contextPath}/admin/input-result" method="POST">
        
        <div class="form-group">
            <label for="registrationId">Mã Đăng Ký (Registration ID):</label>
            <input type="text" 
                   id="registrationId" 
                   name="registrationId" 
                   placeholder="Nhập mã đăng ký hợp lệ (Ví dụ: 1, 2, 3...)" 
                   required />
        </div>

        <div class="form-group">
            <label for="finishTime">Thời gian về đích (hh:mm:ss):</label>
            <input type="text" 
                   id="finishTime" 
                   name="finishTime" 
                   placeholder="Ví dụ: 01:45:23" 
                   required />
        </div>

        <button type="submit" class="btn-submit">Lưu & Tự Động Xếp Hạng</button>
    </form>

    <a href="${pageContext.request.contextPath}/home" class="back-link">Quay lại Trang chủ</a>
</div>

</body>
</html>