<%-- 
    Document   : importData
    Created on : 24.04.2016, 14:39:16
    Author     : Генндий
--%>

<%@page import="controller.WebHelper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="style.css">
        <title>Import Data</title>
    </head>
    <body>
        <form enctype="multipart/form-data" action="Upload" method="post">
            <h1>Import data:</h1>
            <input type="file" name="file_send">
            <input type="submit" name="import" value="Import">
            <br><br>
        </form>
        <div id="xslt">
            <!--Здесь будет выводится результат импорта -->
                <% if(WebHelper.getPreparedFile(request)!=""){
                %>
                    <jsp:include page="Transform" />
                <%}
                %>
        </div>
        <form action="Upload">
            In case of coincidence data:
            <br><br>
            <input type="radio" name="option" value="delete" />  Wipe old data
            <br>
            <input type="radio" name="option" value="ignore" />  Ignore import data
            <br>
            <input type="radio" name="option" value="alert" />  View alert
            <br><br>
            <% if(WebHelper.getPreparedFile(request)!=""){%>
                <input type="submit" name="operation" value="Save">
            <%}else{
            %>
                <input disabled type="submit" name="operation" value="Save">
            <%}
            %>
            <input type="button" name="operation" value="BACK" onclick="location.href='index.jsp'">
        </form>
        <script>

        </script>
    </body>
</html>
