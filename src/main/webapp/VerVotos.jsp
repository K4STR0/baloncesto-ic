<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="model.Jugador" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Resultados votaciones</title>
</head>
<body>
<h1>Resultados votaciones</h1>
<p>El resultado de las votaciones es:</p>
<ul>
<c:forEach items="${votaciones}" var="votacion">
    <li>${votacion.nombre}: ${votacion.votos}</li>
</c:forEach>
</ul>
<br> <a href="index.html"> Ir al comienzo</a>
</body>
</html>
