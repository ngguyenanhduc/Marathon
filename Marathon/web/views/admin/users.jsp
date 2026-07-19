<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <jsp:include page="/views/common/header.jsp">
            <jsp:param name="title" value="Quan ly user" />
        </jsp:include>
    </head>
    <body>
        <jsp:include page="/views/common/topnav.jsp" />

        <main class="page-main">
            <div class="container">
                <div class="page-header">
                    <h1>Quan ly user</h1>
                    <p>Theo doi role va cap nhat trang thai tai khoan.</p>
                </div>

                <div class="toolbar admin-tabs">
                    <a class="button"
                       href="${pageContext.request.contextPath}/admin/users">
                        User
                    </a>
                    <a class="button button-secondary"
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

                <section class="panel">
                    <form class="admin-filter-form"
                          action="${pageContext.request.contextPath}/admin/users"
                          method="get">
                        <input class="form-control"
                               type="text"
                               name="keyword"
                               value="${keyword}"
                               placeholder="Tim username, ho ten, email">

                        <select class="form-control" name="roleName">
                            <option value="">Tat ca role</option>
                            <option value="ADMIN"
                                    <c:if test="${roleName == 'ADMIN'}">selected</c:if>>
                                ADMIN
                            </option>
                            <option value="ORGANIZER"
                                    <c:if test="${roleName == 'ORGANIZER'}">selected</c:if>>
                                ORGANIZER
                            </option>
                            <option value="RUNNER"
                                    <c:if test="${roleName == 'RUNNER'}">selected</c:if>>
                                RUNNER
                            </option>
                        </select>

                        <select class="form-control" name="status">
                            <option value="">Tat ca trang thai</option>
                            <option value="ACTIVE"
                                    <c:if test="${status == 'ACTIVE'}">selected</c:if>>
                                ACTIVE
                            </option>
                            <option value="INACTIVE"
                                    <c:if test="${status == 'INACTIVE'}">selected</c:if>>
                                INACTIVE
                            </option>
                            <option value="BANNED"
                                    <c:if test="${status == 'BANNED'}">selected</c:if>>
                                BANNED
                            </option>
                        </select>

                        <button class="button" type="submit">Loc</button>
                    </form>
                </section>

                <c:choose>
                    <c:when test="${empty users}">
                        <div class="empty-state">
                            Khong co user phu hop.
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="panel table-wrapper admin-table">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Tai khoan</th>
                                        <th>Ho ten</th>
                                        <th>Email</th>
                                        <th>Role</th>
                                        <th>Trang thai</th>
                                        <th>Cap nhat</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${users}">
                                        <tr>
                                            <td>
                                                <c:out value="${item.userId}" />
                                            </td>
                                            <td>
                                                <strong>
                                                    <c:out value="${item.userName}" />
                                                </strong>
                                            </td>
                                            <td>
                                                <c:out value="${item.fullName}" />
                                            </td>
                                            <td>
                                                <c:out value="${item.email}" />
                                            </td>
                                            <td>
                                                <span class="status">
                                                    <c:out value="${item.roleName}" />
                                                </span>
                                            </td>
                                            <td>
                                                <span class="status status-${item.status}">
                                                    <c:out value="${item.status}" />
                                                </span>
                                            </td>
                                            <td>
                                                <form class="inline-form"
                                                      action="${pageContext.request.contextPath}/admin/users"
                                                      method="post">
                                                    <input type="hidden"
                                                           name="userId"
                                                           value="${item.userId}">
                                                    <select class="form-control compact-control"
                                                            name="status">
                                                        <option value="ACTIVE"
                                                                <c:if test="${item.status == 'ACTIVE'}">selected</c:if>>
                                                            ACTIVE
                                                        </option>
                                                        <option value="INACTIVE"
                                                                <c:if test="${item.status == 'INACTIVE'}">selected</c:if>>
                                                            INACTIVE
                                                        </option>
                                                        <option value="BANNED"
                                                                <c:if test="${item.status == 'BANNED'}">selected</c:if>>
                                                            BANNED
                                                        </option>
                                                    </select>
                                                    <button class="button"
                                                            type="submit">
                                                        Luu
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
