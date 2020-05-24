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
            <th>Дата</th>
            <th>Ресторан</th>
            <th>Название</th>
            <th>Цена</th>
        </tr>
        </thead>
        <c:forEach items="${dishes}" var="dish">
            <jsp:useBean id="dish" scope="page" type="ru.cherniak.menuvotingsystem.model.Dish"/>
            <tr>
                    <%--                <td><fmt:formatDate value="${dish.date}" pattern="dd-MM-yyyy"/></td>--%>
                <td><c:out value="${dish.date}"/></td>
                <td><c:out value="${dish.restaurant.name}"/></td>
                <td><c:out value="${dish.name}"/></td>
                <td><c:out value="${dish.price}"/></td>
            </tr>
        </c:forEach>
    </table>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
