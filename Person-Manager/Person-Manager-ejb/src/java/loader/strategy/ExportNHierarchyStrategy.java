package loader.strategy;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.xml.bind.JAXBException;
import org.jboss.weld.util.collections.ArraySet;
import parser.impl.JaxbParser;
import parser.impl.Persons;
import server.PersonDAO;
import server.PersonDTO;

public class ExportNHierarchyStrategy implements AbstractExportStrategy{
    
     @Override
    public File export(List<PersonDTO> persons) {
        
        try {
            JaxbParser parser = new JaxbParser();
            File file = new File("C://source//output.xml");
            file.createNewFile();
            parser.saveObject(file, getPersons(persons));
            return file;
        } catch (JAXBException | IOException | SQLException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }
    
    private Persons getPersons(List<PersonDTO> persons) throws SQLException{
        ArraySet<PersonDTO> pers = new ArraySet<PersonDTO>(persons);
            for(PersonDTO person : persons)
                getNode(pers,person.getId());
            
        return new Persons(new ArrayList(pers));
    }
    
    private void getNode(Set<PersonDTO> persons, int id) throws SQLException{
        PersonDAO dao = new PersonDAO();
        List childId = dao.getNHierarchyId(id,3);
        for(Object child : childId)
            persons.add(dao.getPerson((int) child));
        dao.destroy();
    }
}
