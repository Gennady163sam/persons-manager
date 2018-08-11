package upload;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface XMLUploaderHome extends EJBHome{
    
    XMLUploader create() throws RemoteException, CreateException;
    
}
