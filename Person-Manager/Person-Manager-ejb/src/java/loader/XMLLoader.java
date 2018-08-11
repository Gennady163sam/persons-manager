package loader;

import java.io.File;
import java.rmi.RemoteException;
import java.util.List;
import javax.ejb.EJBObject;
import server.PersonDTO;

public interface XMLLoader extends EJBObject{
    
    public File marshal(List<PersonDTO> persons) throws RemoteException;
    
}
