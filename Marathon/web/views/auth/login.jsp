<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <jsp:include page="/views/common/header.jsp">
            <jsp:param name="title" value="Đăng nhập" />
        </jsp:include>
    </head>
    <body>
        <jsp:include page="/views/common/topnav.jsp" />

        <main class="page-main">
            <div class="container">
                <section class="form-panel compact">
                    <div class="page-header">
                        <h1>Đăng nhập</h1>
                        <p>Truy cập tài khoản Marathon của bạn.</p>
                    </div>

                    <jsp:include page="/views/common/message.jsp" />

                    <form action="${pageContext.request.contextPath}/login"
                          method="post">
                        <div class="form-group">
                            <label for="username">Username</label>
                            <input class="form-control"
                                   type="text"
                                   id="username"
                                   name="username"
                                   maxlength="50"
                                   value="<c:out value='${requestScope.username}' />"
                                   required
                                   autofocus>
                        </div>

                        <div class="form-group">
                            <label for="password">Mật khẩu</label>
                            <input class="form-control"
                                   type="password"
                                   id="password"
                                   name="password"
                                   maxlength="255"
                                   required>
                        </div>

                        <div class="form-actions">
                            <button class="button" type="submit">
                                Đăng nhập
                            </button>
                            <a class="button button-secondary"
                               href="${pageContext.request.contextPath}/register">
                                Tạo tài khoản Runner
                            </a>
                        </div>
                    </form>
                </section>
            </div>
        </main>

        <jsp:include page="/views/common/footer.jsp" />
    </body>
</html>
