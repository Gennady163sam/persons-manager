package upload;

import java.io.File;
import java.rmi.RemoteException;
import java.util.List;
import javax.ejb.EJBObject;
import server.PersonDTO;
import upload.strategy.DuplicatePersonException;

public interface XMLUploader extends EJBObject{
    
    public List<PersonDTO>  unmarshal(File file, String option) throws RemoteException,DuplicatePersonException;
    
}
