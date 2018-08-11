package strategy;

import controller.Controller;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import orm.entity.Person;

public class OperationDeleteStrategy implements AbstractOperationStrategy{

    @Override
    public void saveInDataBase(Person person, HttpServletRequest request, HttpServletResponse response) {
        try {
            Controller cntr = new Controller();
            cntr.deletePerson(person.getId());
            cntr.destroy();
            RequestDispatcher view = request.getRequestDispatcher("index.jsp");
            view.forward(request, response);
        } catch (SQLException | ServletException | IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
}
