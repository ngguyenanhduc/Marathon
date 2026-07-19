<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <jsp:include page="/views/common/header.jsp">
            <jsp:param name="title" value="Yêu cầu Organizer" />
        </jsp:include>
    </head>
    <body>
        <jsp:include page="/views/common/topnav.jsp" />

        <main class="page-main">
            <div class="container">
                <div class="page-header">
                    <h1>Yêu cầu trở thành Organizer</h1>
                    <p>Gửi yêu cầu để Admin xem xét quyền tạo giải chạy.</p>
                </div>

                <jsp:include page="/views/common/message.jsp" />

                <c:if test="${not empty organizerRequest}">
                    <section class="panel" style="margin-bottom: 20px;">
                        <h2>Yêu cầu gần nhất</h2>
                        <div class="detail-grid">
                            <div class="detail-item">
                                <span class="detail-label">Trạng thái</span>
                                <span class="status">
                                    <c:out value="${organizerRequest.status}" />
                                </span>
                            </div>
                            <div class="detail-item">
                                <span class="detail-label">Ngày gửi</span>
                                <strong>
                                    <c:out value="${organizerRequest.requestDate}" />
                                </strong>
                            </div>
                            <div class="detail-item">
                                <span class="detail-label">Lý do</span>
                                <strong>
                                    <c:out value="${organizerRequest.reason}" />
                                </strong>
                            </div>
                            <div class="detail-item">
                                <span class="detail-label">Người duyệt</span>
                                <strong>
                                    <c:out value="${organizerRequest.approverName}"
                                           default="Chưa có" />
                                </strong>
                            </div>
                        </div>
                    </section>
                </c:if>

                <c:choose>
                    <c:when test="${organizerRequest.status == 'PENDING'}">
                        <div class="empty-state">
                            Yêu cầu của bạn đang chờ Admin xử lý.
                        </div>
                    </c:when>
                    <c:when test="${organizerRequest.status == 'APPROVED'}">
                        <div class="alert alert-success">
                            Yêu cầu đã được duyệt. Hãy đăng xuất và đăng nhập lại
                            để nhận quyền Organizer.
                        </div>
                    </c:when>
                    <c:otherwise>
                        <section class="form-panel">
                            <form action="${pageContext.request.contextPath}/runner/organizer-request"
                                  method="post">
                                <div class="form-group">
                                    <label for="reason">Lý do</label>
                                    <textarea class="form-control"
                                              id="reason"
                                              name="reason"
                                              maxlength="1000"
                                              placeholder="Mô tả kế hoạch tổ chức giải của bạn"><c:out value="${reason}" /></textarea>
                                    <div class="help-text">
                                        Tối đa 1000 ký tự. Có thể để trống.
                                    </div>
                                </div>

                                <button class="button" type="submit">
                                    Gửi yêu cầu
                                </button>
                            </form>
                        </section>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>

        <jsp:include page="/views/common/footer.jsp" />
    </body>
</html>
