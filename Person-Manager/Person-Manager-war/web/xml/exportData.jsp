<%-- 
    Document   : exportData
    Created on : 24.04.2016, 14:37:50
    Author     : Генндий
--%>

<%@page import="java.util.List"%>
<%@page import="server.PersonDTO"%>
<%@page import="controller.WebHelper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="style.css">
        <title>Export Data</title>
    </head>
    <body>
        <% String find = WebHelper.getFindedText(request);
           int sort = WebHelper.getSort(request);
           String value = WebHelper.getValue(request);
        %>
        <form>
            <h1>Export data:</h1>
            Search on 
            <select name="find">
                <option>id</option>
                <option>parent_id</option>
                <option>name</option>
            </select>
            
             there value - <input type="text" name="value" id="findPerson" value="<%=value%>">
             <input type="submit" name="operation" value="Find">
             <br><br>
             And sorted by 
            <select name="sort">
                <option value="1" onclick="location.href='exportData.jsp?find=<%=find%>&value=<%=value%>&sort=1'">Id</option>
                <option value="2" onclick="location.href='exportData.jsp?find=<%=find%>&value=<%=value%>&sort=2'">Parent id</option>
                <option value="3" onclick="location.href='exportData.jsp?find=<%=find%>&value=<%=value%>&sort=3'">Name</option>
            </select>
            <br>
             <%  
                List<PersonDTO> pers = WebHelper.getPersons(find, value, sort);
                if(pers!=null){
                    for(PersonDTO person : pers){
            %>
            <br>
            ID : <%=person.getId()%>; Parent_Id : <%=person.getParentId()%>; Name : <a href="information.jsp?id=<%=person.getId()%>"><%=person.getName()%></a> <a href="EditServlet?operation=Delete&id=<%=person.getId()%>"> <img  id="del-button" src="cross-button.png"></a>
            <%      }  
                }
                %>
            <br><br>
            <input type="button" name="export" value="Export" onclick="location.href='Download?find=<%=find%>&value=<%=value%>'">
             <input type="button" name="operation" value="BACK" onclick="location.href='index.jsp'">
        </form>
        
    </body>
</html>
