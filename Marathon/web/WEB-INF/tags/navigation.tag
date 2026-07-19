<%@tag body-content="empty" pageEncoding="UTF-8"%>
<%@attribute name="user" required="false" type="model.User"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<nav class="site-nav">
    <div class="container nav-content">
        <a class="brand"
           href="${pageContext.request.contextPath}/races">
            Marathon
        </a>

        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/races">Giải chạy</a>

            <c:choose>
                <c:when test="${empty user}">
                    <a href="${pageContext.request.contextPath}/login">
                        Đăng nhập
                    </a>
                    <a class="nav-primary"
                       href="${pageContext.request.contextPath}/register">
                        Đăng ký
                    </a>
                </c:when>

                <c:when test="${user.roleName == 'RUNNER'}">
                    <a href="${pageContext.request.contextPath}/runner/registrations">
                        Các đơn đăng ký chạy của tôi
                    </a>
                    <a href="${pageContext.request.contextPath}/runner/organizer-request">
                        Đăng ký trở thành Organizer
                    </a>
                    <a href="${pageContext.request.contextPath}/runner/profile">
                        Hồ sơ
                    </a>
                    <span class="nav-user">
                        <c:out value="${user.fullName}" />
                    </span>
                    <a href="${pageContext.request.contextPath}/logout">
                        Đăng xuất
                    </a>
                </c:when>

                <c:when test="${user.roleName == 'ORGANIZER'}">
                    <a href="${pageContext.request.contextPath}/organizer/races">
                        Quản lý giải
                    </a>
                    <span class="nav-user">
                        <c:out value="${user.fullName}" />
                    </span>
                    <a href="${pageContext.request.contextPath}/logout">
                        Đăng xuất
                    </a>
                </c:when>

                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/admin/users">
                        Quản trị
                    </a>
                    <span class="nav-user">
                        <c:out value="${user.fullName}" />
                    </span>
                    <a href="${pageContext.request.contextPath}/logout">
                        Đăng xuất
                    </a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</nav>
