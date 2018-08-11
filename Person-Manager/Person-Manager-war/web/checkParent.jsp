<%-- 
    Document   : checkParent
    Created on : 04.04.2016, 15:25:09
    Author     : Генндий
--%>

<%@page import="orm.entity.Person"%>
<%@page import="controller.WebHelper"%>
<%@page import="java.util.List"%>
<%@page import="controller.Controller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page errorPage="errorPage.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="style.css">
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"> </script>
        <title>Check parent</title>
         <script>  
            function funcBefore(){
                
            }
            
            function funcAfter(data){
                $("div").empty();
                var result = JSON.parse(data);
                for(var i = 0; i<result.length;i++){
                    $("div").append("<br> <h6 id='" + result[i].id 
                            +"' class='parentId' onclick='setParent(this.id)' >"+result[i].name +"</h6>");
                }
                $("div").show(1000);
            }
            
            function find(){
                $("div").hide(1000);
                $.ajax({
                    url:"GetPersonServlet",
                    type:"GET",
                    data:({name: $("input[name='find']").val(),
                           sort: 1
                    }),
                    dataType: "html",
                    beforeSend: funcBefore,
                    success: funcAfter
                });
            }
            
            $(document).ready(function(){
                $('#main').hide();
                $("#main").slideDown(1000);
                find();
                $("input[name='operation']").bind("click",find);
            });
        </script>
    </head>
    <body>
        <%String find=WebHelper.getFindedText(request);
        String params = WebHelper.getParams(request);
        %>
        <form id="main">
            <h1>Change parent:</h1> 
            <input type="text" name="parentId" id="parentId" value="<%=request.getParameter("parentId")%>" disabled>
            <input type="text" class="hide-field" name="parentId" id="id" value="<%=request.getParameter("parentId")%>">
            <hr> 
            Find: <input type="text" id="find" name="find" value=""/><input type="button" name="operation" value="Find"><br>
            <div>
            </div>
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
