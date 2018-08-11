package loader;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface XMLLoaderHome extends EJBHome{
    
    XMLLoader create() throws RemoteException, CreateException;
    
}
