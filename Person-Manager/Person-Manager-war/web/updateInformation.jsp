<%-- 
    Document   : workToPerson
    Created on : 29.03.2016, 15:30:14
    Author     : Генндий
--%>

<%@page import="orm.entity.Person"%>
<%@page import="controller.WebHelper"%>
<%@page import="orm.entity.MetaModel"%>
<%@page import="orm.entity.Attribute"%>
<%@page import="controller.Controller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page errorPage="errorPage.jsp" %>
<!DOCTYPE html>
<html>
    <%String operation=WebHelper.getOperation(request); %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="style.css">
        <title><%=operation%></title>
         <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"> </script>
        <script>
            $(document).ready(function(){
                $('#stend').hide();
                $("#stend").slideDown(1000);
            });
        </script>
    </head>
    <body>
        <%Controller cntr = new Controller();
          String name = WebHelper.getPersonName(request);
          String title = "Create";
          if(name!=""){ 
              title = name;
          }
          int parentId=WebHelper.getInfoParentId(request);
            String parent="";
            if(Integer.parseInt(request.getParameter("id"))==parentId){
                parentId=-1;
            } 
            else{
                parent = cntr.getPersonInfo(parentId)[1];
            }
          String oper = WebHelper.getOperation(request);
        %>
        <form id="stend" action="EditServlet" onsubmit="return checkAll(this)">
            <h1><%=title%> :</h1>
            <input type="text" class="hide-field" name="id" value=<%=request.getParameter("id")%>>
            <input type="text" class="hide-field" name="parentId"  value=<%=parentId%>>
            <input type="text" class="hide-field" name="operation"  value=<%=oper%>>
            <br><br>
            <label for="number">Chief employers:    </label>
            <%=parent%><input type="text" class="hide-field" name="parentId" id="parentId" value=<%=parentId%> disabled>  
            <input type="button" name="operation" id="change" value="Change" onclick="location.href='checkParent.jsp?id=<%=request.getParameter("id")%>&parentId=<%=parentId%>&name=<%=name%>&oper=<%=oper%>'">
            <br><br>
            <label for="number">Name:</label>
            <input type="text" title="STRING" name="name" id="name" value="<%=name%>" onblur="check(this.title,this.value,this);" required="true">
            <%
                Person person = cntr.getPerson(Integer.parseInt(request.getParameter("id")));
                for(Attribute attr: MetaModel.attributes.values()){
            %>
            <br><br>
            <%      if(WebHelper.getValueOfAttribute(attr, person)!=null){%>
                        <%=attr.getName()%>:  <input type="text" title="<%=attr.getType()%>" name="<%=attr.getName()%>" id="<%=attr.getName()%>" value="<%=WebHelper.getValueOfAttribute(attr, person)%>" onblur="check(this.title,this.value,this);">
            <%      }
                    else{ %>
                         <%=attr.getName()%>:  <input type="text" title="<%=attr.getType()%>" name="<%=attr.getName()%>" id="<%=attr.getName()%>" value="" onblur="check(this.title,this.value,this);">
            <%      }  
               }
               cntr.destroy();%>
            <br><br>
            <input type="submit" id="Save" value="Save">
            <input type="button" name="operation" id="Back" value="BACK" onclick="location.href='information.jsp?id=<%=request.getParameter("id")%>'">        
        </form>
            <script>
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