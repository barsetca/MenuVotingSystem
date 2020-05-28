<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<section>
    <h3>Рестораны</h3>

    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Название</th>
            <th>Тип</th>
            <th>Адрес</th>
            <th>Телефон</th>
            <th>Сайт</th>
            <th>Зарегистрирован</th>
        </tr>
        </thead>
        <c:forEach items="${restaurants}" var="restaurant">
            <jsp:useBean id="restaurant" scope="page" type="ru.cherniak.menuvotingsystem.model.Restaurant"/>
            <tr>
                <td><c:out value="${restaurant.name}"/></td>
                <td><c:out value="${restaurant.type}"/></td>
                <td><c:out value="${restaurant.address}"/></td>
                <td><c:out value="${restaurant.phone}"/></td>
                <td><c:out value="${restaurant.url}"/></td>
                <td><fmt:formatDate value="${restaurant.registered}" pattern="yyyy-MM-dd"/></td>
            </tr>
        </c:forEach>
    </table>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>