<%-- 
    Document   : users
    Created on : Jul 15, 2026, 5:05:07 PM
    Author     : anhdu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Quản lý Thành Viên</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    </head>
    <body class="container mt-5">
    <h3 class="mb-4">Hệ thống quản lý trạng thái tài khoản</h3>

    <table class="table table-hover align-middle">
        <thead class="table-dark">
            <tr>
                <th>Mã User (userId)</th>
                <th>Tài khoản</th>
                <th>Email</th>
                <th>Trạng thái hiện tại</th>
                <th>Thay đổi trạng thái</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>9</td>
                <td>runner_pro</td>
                <td>runnerpro@gmail.com</td>
                <td><span class="badge bg-success">ACTIVE</span></td>
                <td>
                    <form action="${pageContext.request.contextPath}/admin/manage-user" method="POST" class="d-flex gap-2">
                        <input type="hidden" name="userId" value="9">
                        
                        <select name="status" class="form-select form-select-sm" style="width: 160px;">
                            <option value="ACTIVE">ACTIVE (Mở khóa)</option>
                            <option value="INACTIVE">INACTIVE (Chưa kích hoạt)</option>
                            <option value="BANNED">BANNED (Khóa tài khoản)</option>
                        </select>
                        
                        <button type="submit" class="btn btn-dark btn-sm">Cập nhật</button>
                    </form>
                </td>
            </tr>
        </tbody>
    </table>
</body>
</html>
