<%-- 
    Document   : workToPerson
    Created on : 29.03.2016, 15:30:14
    Author     : Генндий
--%>

<%@page import="controller.WebHelper"%>
<%@page import="server.MetaModel"%>
<%@page import="server.source.Attribute"%>
<%@page import="server.PersonDTO"%>
<%@page import="controller.Controller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <%String operation=WebHelper.getOperation(request); %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="style.css">
        <title><%=operation%></title>
    </head>
    <body>
        <%String name = WebHelper.getPersonName(request);
          String title = "Create";
          if(name!=""){ 
              title = name;
          }
          int parentId = WebHelper.getInfoParentId(request);
          String oper = WebHelper.getOperation(request);
        %>
        <form action="EditServlet" onsubmit="return checkAll(this)">
            <h1><%=title%> :</h1>
            <input type="text" class="hide-field" name="id" value=<%=request.getParameter("id")%>>
            <input type="text" class="hide-field" name="parentId"  value=<%=parentId%>>
            <input type="text" class="hide-field" name="operation"  value=<%=oper%>>
            <br><br>
            <label for="number">№ Chief employers</label>
            <input type="text" name="parentId" id="parentId" value=<%=parentId%> disabled>  
            <input type="button" name="operation" id="change" value="Change" onclick="location.href='checkParent.jsp?id=<%=request.getParameter("id")%>&parentId=<%=parentId%>&name=<%=name%>&oper=<%=oper%>'">
            <br><br>
            <label for="number">Name:</label>
            <input type="text" title="STRING" name="name" id="name" value="<%=name%>" onblur="check(this.title,this.value,this);" required="true">
            <%Controller cntr = new Controller();
                PersonDTO person = cntr.getPerson(Integer.parseInt(request.getParameter("id")));
                for(Attribute attr: MetaModel.attributes.values()){
            %>
            <br><br>
            <%      if(person.getAttributes().get(attr)!=null){%>
                        <%=attr.getName()%>:  <input type="text" title="<%=attr.getType()%>" name="<%=attr.getName()%>" id="<%=attr.getName()%>" value="<%=person.getAttributes().get(attr).getValue()%>" onblur="check(this.title,this.value,this);">
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