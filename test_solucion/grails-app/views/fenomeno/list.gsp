<%--
  Created by IntelliJ IDEA.
  User: Francisco
  Date: 27/12/2016
  Time: 11:26 AM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Listado de Períodos climáticos del sistema solar</title>
</head>

<body>

<g:each in="${lFenomenos}" var="fen">
    <p> ${fen.id} - ${fen.fecha} - ${fen.periodo}</p>
</g:each>


</body>
</html>