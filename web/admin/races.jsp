<%-- 
    Document   : races
    Created on : Jul 15, 2026, 5:04:51 PM
    Author     : anhdu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
       <title>Phê duyệt giải chạy</title>
    <link class="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    </head>
    <body class="container mt-5">
    <h3 class="mb-4">Giải chạy đang chờ Admin phê duyệt</h3>

    <table class="table table-striped align-middle">
        <thead class="table-primary">
            <tr>
                <th>Mã Giải (raceId)</th>
                <th>Tên Giải Chạy</th>
                <th>Địa điểm</th>
                <th>Trạng thái</th>
                <th>Thao tác</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>101</td>
                <td>FPT Race 2026</td>
                <td>Hà Nội, Việt Nam</td>
                <td><span class="badge bg-warning text-dark">PENDING</span></td>
                <td>
                    <form action="${pageContext.request.contextPath}/admin/approve-race" method="POST" style="display:inline;">
                        <input type="hidden" name="raceId" value="101">
                        <input type="hidden" name="action" value="APPROVED">
                        <button type="submit" class="btn btn-primary btn-sm me-2">Phê duyệt</button>
                    </form>

                    <form action="${pageContext.request.contextPath}/admin/approve-race" method="POST" style="display:inline;">
                        <input type="hidden" name="raceId" value="101">
                        <input type="hidden" name="action" value="REJECTED">
                        <button type="submit" class="btn btn-secondary btn-sm">Từ chối</button>
                    </form>
                </td>
            </tr>
        </tbody>
    </table>
</body>
</html>
