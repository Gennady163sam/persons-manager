package loader.strategy;

import java.io.File;
import java.util.List;
import server.PersonDTO;

public interface AbstractExportStrategy {
    
    public File export(List<PersonDTO> persons);
    
}
