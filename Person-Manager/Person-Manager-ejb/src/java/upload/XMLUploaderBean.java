package upload;

import java.io.File;
import java.rmi.RemoteException;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import server.PersonDTO;
import upload.strategy.DuplicatePersonException;

public class XMLUploaderBean implements SessionBean{
    
    public List<PersonDTO> unmarshal(File file, String option) throws DuplicatePersonException{
        ImportContext.getStrategy(option).importData(file);
        return null;
    }
    
    public void ejbCreate(){}
    @Override
    public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException { }
    @Override
    public void ejbRemove() throws EJBException, RemoteException {}
    @Override
    public void ejbActivate() throws EJBException, RemoteException {}
    @Override
    public void ejbPassivate() throws EJBException, RemoteException {}
    
}
