<%-- 
    Document   : information
    Created on : 28.03.2016, 16:07:11
    Author     : Генндий
--%>

<%@page import="orm.entity.Params"%>
<%@page import="orm.entity.Person"%>
<%@page import="server.source.Entry"%>
<%@page import="controller.WebHelper"%>
<%@page import="java.io.File"%>
<%@page import="java.util.List"%>
<%@page import="server.source.Attribute"%>
<%@page import="controller.Controller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page errorPage="errorPage.jsp" %>
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
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"> </script>
        <script>
            
            function back(){
                $("#stend").slideUp(1000);
                location.href='index.jsp';
            }
            
            $(document).ready(function(){
                $('#stend').hide();
                $("#stend").slideDown(1000);
                $("#back").bind("click",back);
            });
        </script>
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
            }%>
        <form id="stend" action="updateInformation.jsp">
            
            <h1><%=name%> :</h1>
            <input type="button" name="operation" id="Edit" value="Edit" onclick="location.href='updateInformation.jsp?operation=Edit&id=<%=request.getParameter("id")%>&name=<%=name%>&parentId=<%=parentId%>'">
            <input type="button" name="operation" id="Copy" value="Copy" onclick="location.href='updateInformation.jsp?operation=Copy&id=<%=request.getParameter("id")%>&name=<%=name%>&parentId=<%=parentId%>'">
            <input type="button" name="operation" id="Copy" value="Delete" onclick="location.href='EditServlet?operation=Delete&id=<%=request.getParameter("id")%>'">
            <a href="Download?id=<%=request.getParameter("id")%>"><input type="button" name="operation" id="Download" value="Download XML"></a>
            <br><br>
            <label for="number">№ employers: <%=request.getParameter("id")%></label>
            <br><br>
            <label for="number">Chief employers: <a href="information.jsp?id=<%=parentId%>"><%=parent%></a></label>
            <br><br>
            <label for="number">Name: <%=name%></label>
            <%
                Person person = cntr.getPerson(Integer.parseInt(request.getParameter("id")));
                for(Params param : person.getParams()){
            %>
            <br><br>
                <%=param.getAttribute().getName()%>:  <%=WebHelper.getValue(param)%>
            <%  }%>
                <hr>       
            <div id="childBox">
                <% 
                    List<Person> persons = cntr.findChild(Integer.parseInt(request.getParameter("id")));
                    cntr.destroy();
                    if(persons!=null){%>
                        Child:
                <%
                    for(Person pers: persons){
                %>
                <br><br>
                id: <%=pers.getId()%> Name: <a href="information.jsp?id=<%=pers.getId()%>"><%=pers.getName()%></a>
                <%      }
                    
                    }
                %>
            </div>
            <br><br>
            <input type="button" name="operation" id="back" value="BACK">
        </form> 
    </body>
</html>
