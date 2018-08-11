package loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import server.PersonDAO;
import server.PersonDTO;

public class XMLLoaderBean implements SessionBean{
    
    private PersonDAO dao;
    private static final String PATH = "C:\\Users\\Генндий\\Documents\\NetBeansProjects\\Person-Manager\\Person-Manager-ejb\\src\\java\\resources\\config.property";
    
    public File marshal(List<PersonDTO> pers){
        FileInputStream fis;
        Properties prop = new Properties();
        try {
            fis = new FileInputStream(PATH);
            prop.load(fis);
            String type = prop.getProperty("levelDischarge");
            File file = ExportContext.getStrategy(type).export(pers);
            return file;
            
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }
    
    public void ejbCreate() throws SQLException{dao = new PersonDAO();}
    @Override
    public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException { }
    @Override
    public void ejbRemove() throws EJBException, RemoteException {dao.destroy();}
    @Override
    public void ejbActivate() throws EJBException, RemoteException {}
    @Override
    public void ejbPassivate() throws EJBException, RemoteException {}
    
}
