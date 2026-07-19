<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <jsp:include page="/views/common/header.jsp">
            <jsp:param name="title" value="Duyet Organizer" />
        </jsp:include>
    </head>
    <body>
        <jsp:include page="/views/common/topnav.jsp" />

        <main class="page-main">
            <div class="container">
                <div class="page-header">
                    <h1>Duyet Organizer</h1>
                    <p>Cac yeu cau tro thanh Organizer dang cho Admin xu ly.</p>
                </div>

                <div class="toolbar admin-tabs">
                    <a class="button button-secondary"
                       href="${pageContext.request.contextPath}/admin/users">
                        User
                    </a>
                    <a class="button button-secondary"
                       href="${pageContext.request.contextPath}/admin/race-requests">
                        Duyet giai
                    </a>
                    <a class="button"
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
                    <c:when test="${empty requests}">
                        <div class="empty-state">
                            Khong co yeu cau Organizer dang cho duyet.
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="panel table-wrapper admin-table">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Runner</th>
                                        <th>Email</th>
                                        <th>Ly do</th>
                                        <th>Ngay gui</th>
                                        <th>Xu ly</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${requests}">
                                        <tr>
                                            <td>
                                                <c:out value="${item.requestId}" />
                                            </td>
                                            <td>
                                                <strong>
                                                    <c:out value="${item.requesterName}" />
                                                </strong>
                                                <div class="help-text">
                                                    ID:
                                                    <c:out value="${item.requesterId}" />
                                                </div>
                                            </td>
                                            <td>
                                                <c:out value="${item.requesterEmail}" />
                                            </td>
                                            <td>
                                                <c:out value="${item.reason}" />
                                            </td>
                                            <td>
                                                <c:out value="${item.requestDate}" />
                                            </td>
                                            <td>
                                                <form class="inline-form"
                                                      action="${pageContext.request.contextPath}/admin/organizer-requests"
                                                      method="post">
                                                    <input type="hidden"
                                                           name="requestId"
                                                           value="${item.requestId}">
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
