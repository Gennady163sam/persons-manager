<%-- 
    Document   : changeGroup
    Created on : 26.05.2016, 12:41:13
    Author     : Генндий
--%>

<%@page import="controller.WebHelper"%>
<%@page import="controller.Controller"%>
<%@page import="orm.entity.MetaModel"%>
<%@page import="orm.entity.Attribute"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page errorPage="errorPage.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="style.css">
        <title>Change group</title>
        <link rel="stylesheet" href="css/reset.css" type="text/css" charset="utf-8">
        <link rel="stylesheet" href="css/core.css" type="text/css" charset="utf-8">
        <link rel="stylesheet" href="css/accordion.core.css" type="text/css" charset="utf-8">
        <link rel="stylesheet" href="css/page.css" type="text/css" charset="utf-8">
        <script type="text/javascript" src="js/jquery-1.4.2.min.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/jquery.accordion.2.0.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/actionPage.js" charset="utf-8"></script>

    </head>
    <body>
        <form id="stend" action="../PersonGroupEdit" onsubmit="return checkAll(this)">
            <h1>Change group person:</h1>
            <p>Here you can edit the Person in a certain group.</p> 
            <div class="accord">
                <ul id="tool" class="accordion">
                    <li>
                        <h3>Select person:</h3>
                        <div name="find" class="panel loading">
                            <input type="button" id="add" value="Add Criteria">
                            <select name="type"  class="type" id='type'>
                                <option value="id">Id</option>
                                <option value="parentId">Parent_id</option>
                                <option value="name">Name</option>
                                <% for(Attribute attr:MetaModel.attributes.values()){ %>
                                <option value="<%=attr.getId()%>"><%=attr.getName()%></option>
                                <%}%>
                            </select>
                            <input type="text" id="find" name="find" value=""/>
                            <input type="button" name="operation" value="Find"><br>
                            <div id="result">
                            </div>
                        </div>
                    </li>
                    <li>
                        <h3>Update their information:</h3>
                        <div class="panel loading">
                            <%Controller cntr = new Controller();
                                for(Attribute attr: MetaModel.attributes.values()){
                            %>
                            <br><br/>
                                    <%=attr.getName()%>:  <input type="text" title="<%=attr.getType()%>" name="<%=attr.getName()%>" id="<%=attr.getName()%>" value="" onblur="check(this.title,this.value,this);">
                            <% }
                               cntr.destroy();%>
                        </div>
                    </li>
                </ul>
            </div>
            <br/><hr/><br/> 
            <input type="submit" id="Save" value="Save">
            <input type="button" name="operation" id="back" value="BACK">
        </form>
        
        <script type="text/javascript">
            $('#tool').accordion();
            $(".loading").removeClass("loading");
            function check(type,value,object){
                if(type == 'NUMBER'){
                   if(/[^[0-9]/.test(value) || value<0){
                        alert('Введены некорректные данные!');
                        object.style.backgroundColor = 'red';
                    }
                    else{
                        object.style.backgroundColor = 'white';
                    }
                }
                else{
                    if(type == 'STRING'){
                        if(/[*[0-9]/.test(value)){
                            alert('Введены некорректные данные!');
                            object.style.backgroundColor = 'red';
                        }
                        else{
                            object.style.backgroundColor = 'white';
                        }  
                    }
                }
            }

            function checkAll(form){
                for (var i = 0; i < form.elements.length; i++){
                    if (form.elements[i].style.backgroundColor=='red'){
                        alert ('Найдены некорректные данные в полях!');
                        return false;
                    }
                }     
            } 
            </script>
    </body>
</html>
