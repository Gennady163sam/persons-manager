<%-- 
    Document   : checkParent
    Created on : 04.04.2016, 15:25:09
    Author     : Генндий
--%>

<%@page import="controller.WebHelper"%>
<%@page import="java.util.List"%>
<%@page import="server.PersonDTO"%>
<%@page import="controller.Controller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="style.css">
        <title>Check parent</title>
    </head>
    <body>
        <%String find=WebHelper.getFindText(request);
        %>
        <form>
            <h1>Change parent:</h1> 
            <input type="text" name="parentId" id="parentId" value="<%=request.getParameter("parentId")%>" disabled>
            <input type="text" class="hide-field" name="parentId" id="id" value="<%=request.getParameter("parentId")%>">
            <hr> 
            Find: <input type="text" name="find" value="<%=find%>"><input type="submit" name="operation" value="Find" onclick="location.href='checkParent.jsp?find=<%=find%>'"><br>
            <%
            List<PersonDTO> persons = WebHelper.getPersons(request, find, 2);
            for(PersonDTO pers:persons){
            %>
            <br>
            <input type="button" id ="<%=pers.getId()%>" class="parentId" value="<%=pers.getName()%>" onclick="setParent(this.id)">
            <%}%>
            <br><hr>
            <input type="button" name="operation" id="Save" value="Save" onclick="location.href='updateInformation.jsp?id=<%=request.getParameter("id")%>&name=<%=request.getParameter("name")%>&parentId=' + document.getElementById('id').value + '&operation=<%=request.getParameter("oper")%>'">
            <input type="button" name="operation" id="Back" value="BACK" onclick="location.href='updateInformation.jsp?id=<%=request.getParameter("id")%>&name=<%=request.getParameter("name")%>&parentId=<%=request.getParameter("parentId")%>&operation=<%=request.getParameter("oper")%>'">
        </form>
        <script>
            function setParent(id){
                document.getElementById("parentId").value = id;
                document.getElementById("id").value = id;
            }
        </script>
    </body>
</html>
