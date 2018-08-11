<%-- 
    Document   : errorPage
    Created on : 28.03.2016, 9:17:44
    Author     : Генндий
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page isErrorPage="true" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%String error = "";
            error = request.getParameter("error");%>
        <h1>Exception!</h1>
        <h2><%=error%></h2>
        <h1><a href="index.jsp">Back to main page</a></h1>
    </body>
</html>
