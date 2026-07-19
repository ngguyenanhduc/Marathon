<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <jsp:include page="/views/common/header.jsp">
            <jsp:param name="title" value="Danh sách giải chạy" />
        </jsp:include>
    </head>
    <body>
        <jsp:include page="/views/common/topnav.jsp" />

        <main class="page-main">
            <div class="container">
                <div class="page-header">
                    <h1>Danh sách giải chạy</h1>
                    <p>Tìm giải phù hợp và xem các cự ly đang mở.</p>
                </div>

                <jsp:include page="/views/common/message.jsp" />

                <form class="search-form"
                      action="${pageContext.request.contextPath}/races"
                      method="get">
                    <input class="form-control"
                           type="search"
                           name="keyword"
                           maxlength="100"
                           placeholder="Tìm theo tên giải hoặc địa điểm"
                           value="<c:out value='${keyword}' />">
                    <button class="button" type="submit">Tìm kiếm</button>
                </form>

                <c:choose>
                    <c:when test="${empty races}">
                        <div class="empty-state">
                            Không tìm thấy giải chạy phù hợp.
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="race-grid">
                            <c:forEach var="race" items="${races}">
                                <article class="race-card">
                                    <span class="status">
                                        <c:out value="${race.status}" />
                                    </span>
                                    <h2>
                                        <c:out value="${race.raceName}" />
                                    </h2>

                                    <p>
                                        <c:out value="${race.description}" />
                                    </p>

                                    <div class="race-meta">
                                        <div>
                                            <strong>Địa điểm:</strong>
                                            <c:out value="${race.location}" />
                                        </div>
                                        <div>
                                            <strong>Bắt đầu:</strong>
                                            <c:out value="${race.startDate}" />
                                        </div>
                                        <div>
                                            <strong>Cự ly:</strong>
                                            <c:out value="${race.distanceCount}" />
                                        </div>
                                        <div>
                                            <strong>Nhà tổ chức:</strong>
                                            <c:out value="${race.creatorName}" />
                                        </div>
                                    </div>

                                    <a class="button"
                                       href="${pageContext.request.contextPath}/race-detail?id=${race.raceId}">
                                        Xem chi tiết
                                    </a>
                                </article>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>

        <jsp:include page="/views/common/footer.jsp" />
    </body>
</html>
