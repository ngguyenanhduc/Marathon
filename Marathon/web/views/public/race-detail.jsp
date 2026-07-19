<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <jsp:include page="/views/common/header.jsp">
            <jsp:param name="title" value="Chi tiết giải chạy" />
        </jsp:include>
    </head>
    <body>
        <jsp:include page="/views/common/topnav.jsp" />

        <main class="page-main">
            <div class="container">
                <div class="page-header">
                    <span class="status">
                        <c:out value="${race.status}" />
                    </span>
                    <h1><c:out value="${race.raceName}" /></h1>
                    <p><c:out value="${race.description}" /></p>
                </div>

                <jsp:include page="/views/common/message.jsp" />

                <section class="panel">
                    <div class="detail-grid">
                        <div class="detail-item">
                            <span class="detail-label">Nhà tổ chức</span>
                            <strong><c:out value="${race.creatorName}" /></strong>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">Địa điểm</span>
                            <strong><c:out value="${race.location}" /></strong>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">Thời gian bắt đầu</span>
                            <strong><c:out value="${race.startDate}" /></strong>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">Thời gian kết thúc</span>
                            <strong><c:out value="${race.endDate}" /></strong>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">Hạn đăng ký</span>
                            <strong>
                                <c:out value="${race.registrationDeadline}" />
                            </strong>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">Tổng lượt đăng ký</span>
                            <strong>
                                <c:out value="${race.registrationCount}" />
                            </strong>
                        </div>
                    </div>
                </section>

                <div class="page-header" style="margin-top: 30px;">
                    <h2>Các cự ly</h2>
                    <p>Chọn một cự ly phù hợp với mục tiêu của bạn.</p>
                </div>

                <c:choose>
                    <c:when test="${empty distances}">
                        <div class="empty-state">
                            Giải chạy chưa có cự ly.
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="distance-list">
                            <c:forEach var="distance" items="${distances}">
                                <article class="distance-item">
                                    <h3>
                                        <c:out value="${distance.distanceName}" />
                                    </h3>
                                    <p>
                                        <strong>Quãng đường:</strong>
                                        <c:out value="${distance.distanceKm}" /> km
                                    </p>
                                    <p>
                                        <strong>Phí đăng ký:</strong>
                                        <c:out value="${distance.registrationFee}" /> VNĐ
                                    </p>
                                    <p>
                                        <strong>Đã duyệt:</strong>
                                        <c:out value="${distance.approvedRegistrationCount}" />
                                        /
                                        <c:out value="${distance.maxParticipant}" />
                                    </p>

                                    <c:choose>
                                        <c:when test="${not registrationOpen}">
                                            <button class="button" disabled>
                                                Đã đóng đăng ký
                                            </button>
                                        </c:when>
                                        <c:when test="${distance.approvedRegistrationCount >= distance.maxParticipant}">
                                            <button class="button" disabled>
                                                Đã đủ người
                                            </button>
                                        </c:when>
                                        <c:when test="${empty sessionScope.user}">
                                            <a class="button"
                                               href="${pageContext.request.contextPath}/login">
                                                Đăng nhập để đăng ký
                                            </a>
                                        </c:when>
                                        <c:when test="${sessionScope.user.roleName == 'RUNNER'}">
                                            <form action="${pageContext.request.contextPath}/runner/register-race"
                                                  method="post">
                                                <input type="hidden"
                                                       name="distanceId"
                                                       value="${distance.distanceId}">
                                                <button class="button" type="submit">
                                                    Đăng ký cự ly
                                                </button>
                                            </form>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="help-text">
                                                Chỉ tài khoản Runner được đăng ký.
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </article>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>

                <div class="form-actions" style="margin-top: 24px;">
                    <a class="button button-secondary"
                       href="${pageContext.request.contextPath}/races">
                        Quay lại danh sách
                    </a>
                </div>
            </div>
        </main>

        <jsp:include page="/views/common/footer.jsp" />
    </body>
</html>
