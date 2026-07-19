<%@ page contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    .organizer-nav {
        max-width: 1350px;
        margin: 0 auto 18px auto;
        padding: 12px 18px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        gap: 16px;
        background: #ffffff;
        border: 1px solid #dfe3e8;
        border-radius: 8px;
        box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
    }

    .organizer-nav-brand {
        font-size: 20px;
        font-weight: bold;
        color: #1f2937;
        text-decoration: none;
    }

    .organizer-nav-links {
        display: flex;
        align-items: center;
        flex-wrap: wrap;
        gap: 10px;
    }

    .organizer-nav-link {
        display: inline-block;
        padding: 8px 12px;
        color: #1f2937;
        background: #f3f4f6;
        border-radius: 5px;
        text-decoration: none;
        font-size: 14px;
    }

    .organizer-nav-link:hover {
        background: #e5e7eb;
    }

    .organizer-nav-primary {
        color: white;
        background: #1976d2;
    }

    .organizer-nav-primary:hover {
        background: #145ea8;
    }

    .organizer-nav-logout {
        color: white;
        background: #dc3545;
    }

    .organizer-nav-logout:hover {
        background: #b02a37;
    }

    .organizer-nav-user {
        font-weight: bold;
        color: #374151;
    }

    @media (max-width: 700px) {
        .organizer-nav {
            align-items: flex-start;
            flex-direction: column;
        }
    }
</style>

<nav class="organizer-nav">
    <a class="organizer-nav-brand"
       href="${pageContext.request.contextPath}/races">
        Marathon
    </a>

    <div class="organizer-nav-links">
        <a class="organizer-nav-link"
           href="${pageContext.request.contextPath}/races">
            Xem giải chạy
        </a>

        <a class="organizer-nav-link organizer-nav-primary"
           href="${pageContext.request.contextPath}/organizer/races">
            Quản lý giải
        </a>

        <span class="organizer-nav-user">
            <c:out value="${sessionScope.user.fullName}" />
        </span>

        <a class="organizer-nav-link organizer-nav-logout"
           href="${pageContext.request.contextPath}/logout">
            Đăng xuất
        </a>
    </div>
</nav>
