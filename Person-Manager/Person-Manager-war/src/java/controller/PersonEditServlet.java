package controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import orm.entity.Person;

public class PersonEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    public PersonEditServlet(){
        super();
    }
    
    private Person getPerson(HttpServletRequest request){
        Controller cntr = new Controller();
        Person person = cntr.getPerson(Integer.parseInt(request.getParameter("id")));
        cntr.destroy();
        return person;
    }
    
    private void processOperation(HttpServletRequest request, HttpServletResponse response, String operation) throws ServletException{
        Person person = getPerson(request);
        if(person==null){
            person = new Person();
        }      
        CommandContext.getStrategy(operation).saveInDataBase(person, request, response);
    }
    


    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.getParameter("operation")!=null){
            processOperation(request, response,request.getParameter("operation"));
            if(!request.getParameter("operation").equals("Delete")){
                RequestDispatcher view = request.getRequestDispatcher("updateInformation.jsp");
                view.forward(request, response);
            }
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
