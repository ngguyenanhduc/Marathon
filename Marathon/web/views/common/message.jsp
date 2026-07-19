<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${not empty requestScope.error}">
    <div class="alert alert-error">
        <c:out value="${requestScope.error}" />
    </div>
</c:if>

<c:if test="${not empty sessionScope.errorMessage}">
    <div class="alert alert-error">
        <c:out value="${sessionScope.errorMessage}" />
    </div>
    <c:remove var="errorMessage" scope="session" />
</c:if>

<c:if test="${not empty sessionScope.successMessage}">
    <div class="alert alert-success">
        <c:out value="${sessionScope.successMessage}" />
    </div>
    <c:remove var="successMessage" scope="session" />
</c:if>
