package strategy;

import controller.Controller;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import orm.entity.Attribute;
import orm.entity.MetaModel;
import orm.entity.Params;
import orm.entity.Person;

public class OperationEditStrategy implements AbstractOperationStrategy{

    @Override
    public void saveInDataBase(Person person, HttpServletRequest request, HttpServletResponse response) {
        try {
            fillPerson(person, request);
            fillPersonAttribute(person, request);
            Controller cntr = new Controller();
            cntr.setPerson(person);
            cntr.destroy();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        
    }
    
    private void fillPerson(Person person,HttpServletRequest request){
        if(request.getParameter("parentId")!=null)
            person.setParentId(Integer.parseInt(request.getParameter("parentId")));
        if(request.getParameter("name")!=null)
            person.setName(request.getParameter("name"));
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
