<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <jsp:include page="/views/common/header.jsp">
            <jsp:param name="title" value="Hồ sơ Runner" />
        </jsp:include>
    </head>
    <body>
        <jsp:include page="/views/common/topnav.jsp" />

        <main class="page-main">
            <div class="container">
                <section class="form-panel">
                    <div class="page-header">
                        <h1>Hồ sơ Runner</h1>
                        <p>Cập nhật thông tin liên hệ của bạn.</p>
                    </div>

                    <jsp:include page="/views/common/message.jsp" />

                    <form action="${pageContext.request.contextPath}/runner/profile"
                          method="post">
                        <div class="form-group">
                            <label for="username">Username</label>
                            <input class="form-control"
                                   type="text"
                                   id="username"
                                   value="<c:out value='${profileUser.userName}' />"
                                   disabled>
                        </div>

                        <div class="form-group">
                            <label for="fullName">Họ và tên</label>
                            <input class="form-control"
                                   type="text"
                                   id="fullName"
                                   name="fullName"
                                   maxlength="100"
                                   value="<c:out value='${profileUser.fullName}' />"
                                   required>
                        </div>

                        <div class="form-group">
                            <label for="email">Email</label>
                            <input class="form-control"
                                   type="email"
                                   id="email"
                                   name="email"
                                   maxlength="100"
                                   value="<c:out value='${profileUser.email}' />"
                                   required>
                        </div>

                        <div class="form-group">
                            <label for="phone">Số điện thoại</label>
                            <input class="form-control"
                                   type="text"
                                   id="phone"
                                   name="phone"
                                   maxlength="20"
                                   value="<c:out value='${profileUser.phone}' />">
                        </div>

                        <div class="form-actions">
                            <button class="button" type="submit">
                                Lưu thay đổi
                            </button>
                            <a class="button button-secondary"
                               href="${pageContext.request.contextPath}/races">
                                Xem giải chạy
                            </a>
                        </div>
                    </form>
                </section>
            </div>
        </main>

        <jsp:include page="/views/common/footer.jsp" />
    </body>
</html>
