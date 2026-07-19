<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <jsp:include page="/views/common/header.jsp">
            <jsp:param name="title" value="Đơn đăng ký của tôi" />
        </jsp:include>
    </head>
    <body>
        <jsp:include page="/views/common/topnav.jsp" />

        <main class="page-main">
            <div class="container">
                <div class="page-header">
                    <h1>Đơn đăng ký của tôi</h1>
                    <p>Theo dõi trạng thái duyệt và số BIB của từng cự ly.</p>
                </div>

                <jsp:include page="/views/common/message.jsp" />

                <c:choose>
                    <c:when test="${empty registrations}">
                        <div class="empty-state">
                            <p>Bạn chưa đăng ký cự ly nào.</p>
                            <a class="button"
                               href="${pageContext.request.contextPath}/races">
                                Xem danh sách giải
                            </a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="panel table-wrapper">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>Giải chạy</th>
                                        <th>Cự ly</th>
                                        <th>Ngày đăng ký</th>
                                        <th>Trạng thái</th>
                                        <th>BIB</th>
                                        <th>Kết quả</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="registration"
                                               items="${registrations}">
                                        <tr>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/race-detail?id=${registration.raceId}">
                                                    <c:out value="${registration.raceName}" />
                                                </a>
                                            </td>
                                            <td>
                                                <c:out value="${registration.distanceName}" />
                                            </td>
                                            <td>
                                                <c:out value="${registration.registerDate}" />
                                            </td>
                                            <td>
                                                <span class="status">
                                                    <c:out value="${registration.status}" />
                                                </span>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${empty registration.bibNumber}">
                                                        Chưa có
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:out value="${registration.bibNumber}" />
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${empty registration.resultStatus}">
                                                        Chưa cập nhật
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:out value="${registration.resultStatus}" />
                                                        <c:if test="${not empty registration.finishTime}">
                                                            - <c:out value="${registration.finishTime}" />
                                                        </c:if>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>

        <jsp:include page="/views/common/footer.jsp" />
    </body>
</html>
