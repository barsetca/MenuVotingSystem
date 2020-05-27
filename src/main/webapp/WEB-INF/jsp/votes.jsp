<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<section>
    <h3>Голоса на дату</h3>

    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Дата</th>
            <th>Ресторан</th>
            <th>Пользователь</th>
        </tr>
        </thead>
        <c:forEach items="${votes}" var="vote">
            <jsp:useBean id="vote" scope="page" type="ru.cherniak.menuvotingsystem.model.Vote"/>
            <tr>
                <td><c:out value="${vote.date}"/></td>
                <td><c:out value="${vote.restaurant.name}"/></td>
                <td><a href="mailto:${vote.user.email}">${vote.user.email}</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>