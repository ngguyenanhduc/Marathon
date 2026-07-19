<%-- 
    Document   : requests
    Created on : Jul 15, 2026, 5:04:32 PM
    Author     : anhdu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
       <title>Duyệt yêu cầu Organizer</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    </head>
    <body class="container mt-5">
    <h3 class="mb-4">Danh sách đơn đăng ký làm Nhà Tổ Chức (Organizer)</h3>

    <table class="table table-bordered align-middle">
        <thead class="table-dark">
            <tr>
                <th>Mã Đơn (requestId)</th>
                <th>Mã User (requesterId)</th>
                <th>Lý do đăng ký</th>
                <th>Trạng thái</th>
                <th>Hành động phê duyệt</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>1</td>
                <td>14</td>
                <td>Tôi muốn tổ chức giải chạy Marathon FPT University 2026</td>
                <td><span class="badge bg-warning text-dark">PENDING</span></td>
                <td>
                    <form action="${pageContext.request.contextPath}/admin/approve-organizer" method="POST" style="display:inline;">
                        <input type="hidden" name="requestId" value="1"> 
                        <input type="hidden" name="action" value="APPROVED">
                        <button type="submit" class="btn btn-success btn-sm me-2">Duyệt cấp quyền</button>
                    </form>

                    <form action="${pageContext.request.contextPath}/admin/approve-organizer" method="POST" style="display:inline;">
                        <input type="hidden" name="requestId" value="1">
                        <input type="hidden" name="action" value="REJECTED">
                        <button type="submit" class="btn btn-danger btn-sm">Từ chối đơn</button>
                    </form>
                </td>
            </tr>
        </tbody>
    </table>
</body>
</html>
