package controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.ibm.useful.http.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import upload.XMLUploader;
import upload.XMLUploaderHome;
import upload.strategy.DuplicatePersonException;

public class FileUploadServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    private XMLUploader getUploader(){
        
        XMLUploader uploader = null;
        try{
            InitialContext ic = new InitialContext();
            Object objRef = ic.lookup("ejb/XMLUploader");
            XMLUploaderHome home = (XMLUploaderHome) PortableRemoteObject.narrow(objRef, XMLUploaderHome.class);
            uploader = home.create();
            return uploader;
        }catch(NamingException | CreateException | RemoteException ex){
            System.err.println(ex.getMessage());
        }  
        return null;
    }
    
    private void importData(File file, String option) throws RemoteException, DuplicatePersonException{
        getUploader().unmarshal(file, option);
    }
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        if(request.getParameter("operation")==null)
            processMultipartFormat(request);
        else
            processImportData(request,response);
        
        RequestDispatcher view = request.getRequestDispatcher("importData.jsp?file=yes");
        view.forward(request, response);
    }
    
    private void processImportData(HttpServletRequest request,HttpServletResponse response) throws ServletException{
        try{
            if(request.getParameter("option")!=null){
                File f = new File("tempFile"+ request.getSession().getId() +".xml");
                if(!f.exists()) throw new FileNotFoundException();
                importData(f, request.getParameter("option"));
                f.delete();
            }
        }catch(IOException ex){
            RequestDispatcher view = request.getRequestDispatcher("errorPage.jsp?error=Please, check import file");
            try {
                view.forward(request, response);
            } catch (IOException ex1) {
                System.err.println(ex1.getMessage());
            }
        }catch(DuplicatePersonException e){
            RequestDispatcher view = request.getRequestDispatcher("errorPage.jsp?error="+e.getMessage());
            try {
                view.forward(request, response);
            } catch (IOException e1) {
                System.err.println(e1.getMessage());
            }
        }
    }
    
    private void processMultipartFormat(HttpServletRequest request) throws ServletException, IOException{
        if(isMultipartFormat(request)){
            PostData multidata=new PostData(request);
            FileData tempFile=multidata.getFileData("file_send");
            if(tempFile!=null) {
                File f = new File("tempFile"+ request.getSession().getId() +".xml");
                f.createNewFile();
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(tempFile.getByteData());
                fos.close();
            }
        }
    }
    
    private boolean isMultipartFormat(HttpServletRequest req) throws javax.servlet.ServletException, java.io.IOException{
        String temptype=req.getContentType();
        if(temptype.indexOf("multipart/form-data")!=-1) return true;
        else return false;
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
