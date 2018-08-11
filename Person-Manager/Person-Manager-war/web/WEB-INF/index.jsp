<%-- 
    Document   : index
    Created on : 28.03.2016, 9:17:34
    Author     : Генндий
--%>

<%@page import="controller.WebHelper"%>
<%@page import="java.io.File"%>
<%@page import="java.util.List"%>
<%@page import="server.PersonDTO"%>
<%@page import="controller.Controller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page errorPage="errorPage.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="style.css">
        <title>Person manager</title>
    </head>
    <body>
        <% String find = WebHelper.getFindText(request);
           int sort = WebHelper.getSort(request);
        %>
        <form action="index.jsp">
            <h1>Persons:</h1>
            Find: <input type="text" name="find" id="findPerson" value="<%=find%>">
            <input type="submit" name="operation" value="Find" onclick="location.href='index.jsp?find=<%=find%>&sort=<%=sort%>'">
            <input type="button" name="Add" value="Create" onclick="location.href='updateInformation.jsp?operation=Create&id=-1'">
            <a href="Download" download><input type="button" name="download" value="Download XML"></a>
            <br><br>
            Sorted by - 
            <select>
                <option onclick="location.href='index.jsp?find=<%=find%>&sort=1'">Id</option>
                <option onclick="location.href='index.jsp?find=<%=find%>&sort=2'">Parent id</option>
                <option onclick="location.href='index.jsp?find=<%=find%>&sort=3'">Name</option>
            </select>
            <br>
            <%  
                List<PersonDTO> pers=WebHelper.getPersons(request, find, sort);
                if(pers!=null){
                    for(PersonDTO person : pers){
            %>
            <br>
            ID : <%=person.getId()%>; Parent_Id : <%=person.getParentId()%>; Name : <a href="information.jsp?id=<%=person.getId()%>"><%=person.getName()%></a> <a href="EditServlet?operation=Delete&id=<%=person.getId()%>"> <img  id="del-button" src="cross-button.png"></a>
            <%      }  
                }
                %>
        </form>
    </body>
</html>
