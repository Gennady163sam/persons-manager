package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import loader.XMLLoader;
import loader.XMLLoaderHome;
import server.PersonDAO;
import server.PersonDTO;

public class FileDownloadServlet extends HttpServlet {
    
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    private XMLLoader getLoader(){
        XMLLoader loader = null;
        try{
            InitialContext ic = new InitialContext();
            Object objRef = ic.lookup("ejb/XMLLoader");
            XMLLoaderHome home = (XMLLoaderHome) PortableRemoteObject.narrow(objRef, XMLLoaderHome.class);
            loader = home.create();
            return loader;
        }catch(NamingException | CreateException | RemoteException ex){
            System.err.println(ex.getMessage());
        }  
        return null;
    }
    
    protected File getXMLFile()throws RemoteException{  
        try {
            PersonDAO dao = new PersonDAO();
            List<PersonDTO> persons = dao.getAllPerson();
            return getLoader().marshal(persons);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    protected File getXMLFile(int id)throws RemoteException{  
        try {
            PersonDAO dao = new PersonDAO();
            PersonDTO pers = dao.getPerson(id);
            List<PersonDTO> persons = new ArrayList<PersonDTO>();
            persons.add(pers);
            return getLoader().marshal(persons);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    
    protected File getXMLFile(List<PersonDTO> pers)throws RemoteException{  
        try {
            PersonDAO dao = new PersonDAO();
            List<PersonDTO> persons = new ArrayList<PersonDTO>();
            for(PersonDTO person: pers){
                PersonDTO bufPerson = dao.getPerson(person.getId());
                persons.add(bufPerson);
            }
            return getLoader().marshal(persons);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        File fileXML = getFile(request);
        writeFile(fileXML, response);
   }
    
    private File getFile(HttpServletRequest request) throws RemoteException{
        /*if(request.getParameter("find")!=null && request.getParameter("value")!=null)
            return getXMLFile(WebHelper.getPersons(request.getParameter("find"), request.getParameter("value"), 0));
        else{
            if(request.getParameter("id")!=null)
                return getXMLFile(Integer.parseInt(request.getParameter("id")));
            else
                return getXMLFile();
        }*/
        return null;
    }
    
    private void writeFile(File fileXML, HttpServletResponse response) throws UnsupportedEncodingException, IOException{
        String length = String.valueOf(fileXML.length());
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileXML.getName(), "UTF-8"));
        response.addHeader("Content-Length", length);
        ServletOutputStream out = response.getOutputStream();
        FileInputStream fileInputStream = new FileInputStream(fileXML);
        int i;
        while((i=fileInputStream.read())!=-1){
                out.write(i);
        }
        fileInputStream.close();
        out.close();
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
