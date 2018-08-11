package strategy;

import controller.Controller;
import java.io.IOException;
import java.util.HashSet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import orm.entity.Attribute;
import orm.entity.MetaModel;
import orm.entity.Params;
import orm.entity.Person;

public class OperationCreateStrategy implements AbstractOperationStrategy{

    @Override
    public void saveInDataBase(Person person, HttpServletRequest request, HttpServletResponse response) {
        try {
            fillPerson(person, request);
            Controller cntr = new Controller();
            int newId = cntr.createPerson(person);
            cntr.destroy();
            fillPersonAttribute(person, request);
            RequestDispatcher view = request.getRequestDispatcher("updateInformation.jsp?operation=Edit&id="+newId+"&name=" + person.getName()+ "&parentId="+person.getParentId());
            view.forward(request, response);
        } catch (ServletException | IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    private void fillPerson(Person person,HttpServletRequest request){
        person.setParentId(Integer.parseInt(request.getParameter("parentId")));
        person.setName(request.getParameter("name"));
        person.setParams(new HashSet<Params>());
    }
    
    private void fillPersonAttribute(Person person,HttpServletRequest request){
        for(Attribute attr:MetaModel.attributes.values())
            if(request.getParameter(attr.getName())!=null){
                Controller cntr = new Controller();
                Params p = cntr.createParams(person, attr, request.getParameter(attr.getName()));
                cntr.destroy();
                if(p!=null){
                    person.getParams().add(p);
                    p.setPerson(person);
                }
            }
    }
    
}
