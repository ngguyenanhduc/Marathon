<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <jsp:include page="/views/common/header.jsp">
            <jsp:param name="title" value="Đăng ký Runner" />
        </jsp:include>
    </head>
    <body>
        <jsp:include page="/views/common/topnav.jsp" />

        <main class="page-main">
            <div class="container">
                <section class="form-panel">
                    <div class="page-header">
                        <h1>Đăng ký tài khoản Runner</h1>
                        <p>Điền thông tin để bắt đầu tham gia các giải chạy.</p>
                    </div>

                    <jsp:include page="/views/common/message.jsp" />

                    <form action="${pageContext.request.contextPath}/register"
                          method="post">
                        <div class="detail-grid">
                            <div class="form-group">
                                <label for="username">Username</label>
                                <input class="form-control"
                                       type="text"
                                       id="username"
                                       name="username"
                                       minlength="4"
                                       maxlength="50"
                                       value="<c:out value='${formUser.userName}' />"
                                       required>
                                <div class="help-text">
                                    Dùng chữ, số hoặc dấu gạch dưới.
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="fullName">Họ và tên</label>
                                <input class="form-control"
                                       type="text"
                                       id="fullName"
                                       name="fullName"
                                       maxlength="100"
                                       value="<c:out value='${formUser.fullName}' />"
                                       required>
                            </div>

                            <div class="form-group">
                                <label for="email">Email</label>
                                <input class="form-control"
                                       type="email"
                                       id="email"
                                       name="email"
                                       maxlength="100"
                                       value="<c:out value='${formUser.email}' />"
                                       required>
                            </div>

                            <div class="form-group">
                                <label for="phone">Số điện thoại</label>
                                <input class="form-control"
                                       type="text"
                                       id="phone"
                                       name="phone"
                                       maxlength="20"
                                       value="<c:out value='${formUser.phone}' />">
                            </div>

                            <div class="form-group">
                                <label for="password">Mật khẩu</label>
                                <input class="form-control"
                                       type="password"
                                       id="password"
                                       name="password"
                                       minlength="6"
                                       maxlength="255"
                                       required>
                            </div>

                            <div class="form-group">
                                <label for="confirmPassword">
                                    Xác nhận mật khẩu
                                </label>
                                <input class="form-control"
                                       type="password"
                                       id="confirmPassword"
                                       name="confirmPassword"
                                       minlength="6"
                                       maxlength="255"
                                       required>
                            </div>
                        </div>

                        <div class="form-actions">
                            <button class="button" type="submit">
                                Tạo tài khoản
                            </button>
                            <a class="button button-secondary"
                               href="${pageContext.request.contextPath}/login">
                                Quay lại đăng nhập
                            </a>
                        </div>
                    </form>
                </section>
            </div>
        </main>

        <jsp:include page="/views/common/footer.jsp" />
    </body>
</html>
