package loader.strategy;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.xml.bind.JAXBException;
import parser.impl.JaxbParser;
import parser.impl.Persons;
import server.PersonDTO;


public class OnlyNodeStrategy implements AbstractExportStrategy{
    
    @Override
    public File export(List<PersonDTO> persons) {
        
        try {
            JaxbParser parser = new JaxbParser();
            File file = new File("C://source//output.xml");
            file.createNewFile();
            Persons p = new Persons(persons);
            parser.saveObject(file, p);
            return file;
        } catch (JAXBException | IOException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }
}
