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
<%
    List<Jugador> votaciones = (List<Jugador>) request.getAttribute("votaciones");
    if(votaciones!=null){
        for(Jugador votacion: votaciones){
            out.println("<li>" + votacion.getNombre() + ": " + votacion.getVotos() +" votos</li>");
        }
    }
%>
</ul>
<br> <a href="index.html"> Ir al comienzo</a>
</body>
</html>
