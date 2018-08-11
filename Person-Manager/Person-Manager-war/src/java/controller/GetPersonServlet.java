package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import orm.entity.Person;

public class GetPersonServlet extends HttpServlet {
    
    private static int SORT_BY_ID = 1;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        List<Person> persons = null;
        String map = request.getParameter("map");
        List<FindCondition> conditions = new ArrayList<FindCondition>();
        if(map!=null)
           conditions = WebHelper.getMap(map);
        
        if(conditions.size()>0){
            persons = WebHelper.getPersonsByFindAttributes(conditions); 
        }else{
            persons = WebHelper.getPersonsByFindAttribute(request); 
        }
            
        if(persons!=null){
            returnResult(persons, response);
        }
    }
    
    private void returnResult(List<Person> persons, HttpServletResponse response) throws IOException{
        try (PrintWriter out = response.getWriter()) {
                JSONWriter jw = new JSONWriter(out);
                jw.array();
                for(Person pers:persons){
                    jw.object();
                    jw.key("id");
                    jw.value(pers.getId());
                    jw.key("parentId");
                    jw.value(pers.getParentId());
                    jw.key("name");
                    jw.value(pers.getName());
                    jw.endObject();
                }
                jw.endArray();
            }
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
        processRequest(request, response);
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
        processRequest(request, response);
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
