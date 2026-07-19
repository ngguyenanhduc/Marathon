<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <jsp:include page="/views/common/header.jsp">
            <jsp:param name="title" value="Duyet giai chay" />
        </jsp:include>
    </head>
    <body>
        <jsp:include page="/views/common/topnav.jsp" />

        <main class="page-main">
            <div class="container">
                <div class="page-header">
                    <h1>Duyet giai chay</h1>
                    <p>Cac giai co trang thai PENDING dang cho Admin xu ly.</p>
                </div>

                <div class="toolbar admin-tabs">
                    <a class="button button-secondary"
                       href="${pageContext.request.contextPath}/admin/users">
                        User
                    </a>
                    <a class="button"
                       href="${pageContext.request.contextPath}/admin/race-requests">
                        Duyet giai
                    </a>
                    <a class="button button-secondary"
                       href="${pageContext.request.contextPath}/admin/organizer-requests">
                        Duyet Organizer
                    </a>
                    <a class="button button-secondary"
                       href="${pageContext.request.contextPath}/admin/input-result">
                        Nhap ket qua
                    </a>
                </div>

                <jsp:include page="/views/common/message.jsp" />

                <c:choose>
                    <c:when test="${empty races}">
                        <div class="empty-state">
                            Khong co giai nao dang cho duyet.
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="panel table-wrapper admin-table">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Ten giai</th>
                                        <th>Organizer</th>
                                        <th>Thoi gian</th>
                                        <th>Dia diem</th>
                                        <th>Cu ly</th>
                                        <th>Xu ly</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="race" items="${races}">
                                        <tr>
                                            <td>
                                                <c:out value="${race.raceId}" />
                                            </td>
                                            <td>
                                                <strong>
                                                    <c:out value="${race.raceName}" />
                                                </strong>
                                                <div class="help-text">
                                                    <c:out value="${race.description}" />
                                                </div>
                                            </td>
                                            <td>
                                                <c:out value="${race.creatorName}" />
                                            </td>
                                            <td>
                                                <c:out value="${race.startDate}" />
                                                <br>
                                                <span class="help-text">
                                                    Han:
                                                    <c:out value="${race.registrationDeadline}" />
                                                </span>
                                            </td>
                                            <td>
                                                <c:out value="${race.location}" />
                                            </td>
                                            <td>
                                                <c:out value="${race.distanceCount}" />
                                            </td>
                                            <td>
                                                <form class="inline-form"
                                                      action="${pageContext.request.contextPath}/admin/race-requests"
                                                      method="post">
                                                    <input type="hidden"
                                                           name="raceId"
                                                           value="${race.raceId}">
                                                    <button class="button"
                                                            type="submit"
                                                            name="action"
                                                            value="APPROVED">
                                                        Duyet
                                                    </button>
                                                    <button class="button button-danger"
                                                            type="submit"
                                                            name="action"
                                                            value="REJECTED">
                                                        Tu choi
                                                    </button>
                                                </form>
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
