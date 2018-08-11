<%-- 
    Document   : information
    Created on : 28.03.2016, 16:07:11
    Author     : Генндий
--%>

<%@page import="controller.WebHelper"%>
<%@page import="java.io.File"%>
<%@page import="java.util.List"%>
<%@page import="server.source.Attribute"%>
<%@page import="server.PersonDTO"%>
<%@page import="controller.Controller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
if(request.getParameter("id").equals("-1")){
    RequestDispatcher view = request.getRequestDispatcher("index.jsp");
    view.forward(request, response);
}
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="style.css">
        <title>Information</title>
    </head>
    <body>
        <%  Controller cntr = new Controller();
            String name=WebHelper.getInfoPerson(request);
            int parentId=WebHelper.getInfoParentId(request);
            String parent="";
            if(Integer.parseInt(request.getParameter("id"))==parentId){
                parentId=-1;
            } 
            else{
                parent = cntr.getPersonInfo(parentId)[1];
            } 
                      
        %>
        <form action="updateInformation.jsp">
            
            <h1><%=name%> :</h1>
            <input type="button" name="operation" id="Edit" value="Edit" onclick="location.href='updateInformation.jsp?operation=Edit&id=<%=request.getParameter("id")%>&name=<%=name%>&parentId=<%=parentId%>'">
            <input type="button" name="operation" id="Copy" value="Copy" onclick="location.href='updateInformation.jsp?operation=Copy&id=<%=request.getParameter("id")%>&name=<%=name%>&parentId=<%=parentId%>'">
            <input type="button" name="operation" id="Copy" value="Delete" onclick="location.href='EditServlet?operation=Delete&id=<%=request.getParameter("id")%>'">
            <a href="Download?id=<%=request.getParameter("id")%>"><input type="button" name="operation" id="Download" value="Download XML"></a>
            <br><br>
            <label for="number">№ employers: <%=request.getParameter("id")%></label>
            <br><br>
            <label for="number">№ Chief employers: <a href="information.jsp?id=<%=parentId%>"><%=parent%></a></label>
            <br><br>
            <label for="number">Name: <%=name%></label>
            <%
                PersonDTO person = cntr.getPerson(Integer.parseInt(request.getParameter("id")));
                for(Attribute attr: person.getAttributes().keySet()){
            %>
            <br><br>
                <%=attr.getName()%>:  <%=person.getAttributes().get(attr).getValue().toString()%>
            <%  }%>
                <hr>       
            <div id="childBox">
                <% 
                    List<PersonDTO> persons = cntr.findChild(Integer.parseInt(request.getParameter("id")));
                    cntr.destroy();
                    if(persons!=null){%>
                        Child:
                <%
                    for(PersonDTO pers: persons){
                %>
                <br><br>
                id: <%=pers.getId()%> Name: <a href="information.jsp?id=<%=pers.getId()%>"><%=pers.getName()%></a>
                <%      }
                    
                    }
                %>
            </div>
            <br><br>
            <input type="button" name="operation" id="back" value="BACK" onclick="location.href='index.jsp'">
        </form> 
    </body>
</html>
