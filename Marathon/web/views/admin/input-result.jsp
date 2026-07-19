<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <jsp:include page="/views/common/header.jsp">
            <jsp:param name="title" value="Nhap ket qua" />
        </jsp:include>
    </head>
    <body>
        <jsp:include page="/views/common/topnav.jsp" />

        <main class="page-main">
            <div class="container">
                <div class="page-header">
                    <h1>Nhap ket qua chay</h1>
                    <p>Cap nhat finish time cho runner da duoc duyet BIB.</p>
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
                    <a class="button button-secondary"
                       href="${pageContext.request.contextPath}/admin/organizer-requests">
                        Duyet Organizer
                    </a>
                    <a class="button"
                       href="${pageContext.request.contextPath}/admin/input-result">
                        Nhap ket qua
                    </a>
                </div>

                <jsp:include page="/views/common/message.jsp" />

                <section class="panel">
                    <form class="form-stack"
                          action="${pageContext.request.contextPath}/admin/input-result"
                          method="post">
                        <div class="form-group">
                            <label for="registrationId">
                                Ma dang ky
                                <span class="required">*</span>
                            </label>
                            <input class="form-control"
                                   type="number"
                                   id="registrationId"
                                   name="registrationId"
                                   min="1"
                                   required>
                        </div>

                        <div class="form-group">
                            <label for="finishTime">
                                Thoi gian hoan thanh
                                <span class="required">*</span>
                            </label>
                            <input class="form-control"
                                   type="time"
                                   id="finishTime"
                                   name="finishTime"
                                   step="1"
                                   required>
                        </div>

                        <div class="toolbar">
                            <button class="button" type="submit">
                                Luu ket qua
                            </button>
                        </div>
                    </form>
                </section>
            </div>
        </main>

        <jsp:include page="/views/common/footer.jsp" />
    </body>
</html>
