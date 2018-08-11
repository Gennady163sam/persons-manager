<%-- 
    Document   : index
    Created on : 28.03.2016, 9:17:34
    Author     : Генндий
--%>

<%@page import="server.PersonDTO"%>
<%@page import="orm.entity.Person"%>
<%@page import="controller.WebHelper"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page errorPage="errorPage.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="style.css">
        <title>Person manager</title>
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"> </script>
        <script>
            
            function open(){
                $("#start").slideUp(1000);
                find();
                $("#main").slideDown(1000);
            }
            
            function get(){
                $("#main").slideUp(1000);
            }
            
            function funcBefore(){
                
            }
            
            function funcAfter(data){
                $("div").empty();
                var result = JSON.parse(data);
                for(var i = 0; i<result.length;i++){
                    $("div").append("<br> ID : " + result[i].id + "; Parent_Id : " 
                            + result[i].parentId + "; Name : <a class='item' href='information.jsp?id=" 
                            + result[i].id +"'>" + result[i].name + "</a> <a href='EditServlet?operation=Delete&id=" 
                            + result[i].id + "'> <img  id='del-button' src='cross-button.png'></a>");
                }
                $("div").show(1000);
            }
            
            function find(){
                $("div").hide(1000);
                $.ajax({
                    url:"GetPersonServlet",
                    type:"GET",
                    data:({find: $("input[name='find']").val(),
                           sort: $("#sort").val()
                    }),
                    dataType: "html",
                    beforeSend: funcBefore,
                    success: funcAfter
                });
            }
            
            $(document).ready(function(){
                $('#main').hide();
                $("input[name='OpenEmp'").bind("click",open);
                $(".item").bind("click",get);
                $("input[name='operationFind']").bind("click",find);
                $("#sort").change(find);
            });
        </script>
    </head>
    <body>
        <% String find = WebHelper.getFindedText(request);
           int sort = WebHelper.getSort(request);
        %>
        
        <form id="start" name="start_form">
            <h1>Person - Manager</h1>
            <p>Hello user, welcome to Person-Manager. There, you may find employers, see , add, delete and set their information.
                Also you may import and export information in XML - file. 
                <br/>
                For more information - <a href="infoApplication.jsp">click me</a>
                <br/><br/>
                For see employers click <input type="button" name="OpenEmp" value="this"/> </p>
        </form>
        
        <form id="main" action="index.jsp">
            <h1>Persons:</h1>
            Find: <input type="text" name="find" id="findPerson" value="<%=find%>">
            <input type="button" name="operationFind" value="Find" >
            <input type="button" name="Add" value="Create" onclick="location.href='updateInformation.jsp?operation=Create&id=-1'">
            <a href="change/changeGroup.jsp"><input type="button" name="changeGroup" value="Change group"></a>
            <a href="Download" download><input type="button" name="download" value="Download XML"></a>
            <a href="exportData.jsp"><input type="button" name="export" value="Export Data"></a>
            <a href="importData.jsp"><input type="button" name="import" value="Import Data"></a>
            <br><br>
            Sorted by - 
            <select id="sort">
                <option value="1">Id</option>
                <option value="2">Parent id</option>
                <option value="3">Name</option>
            </select>
            <br>
            <div>

            </div>
        </form>
    </body>
</html>
