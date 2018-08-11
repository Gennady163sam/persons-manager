package upload.strategy;

import java.io.File;
import java.sql.SQLException;
import javax.xml.bind.JAXBException;
import parser.impl.JaxbParser;
import parser.impl.Persons;
import server.PersonDAO;
import server.PersonDTO;

public class IgnoreImportStrategy implements AbstractImportStrategy{
    
    @Override
    public void importData(File file) {
        try {
            JaxbParser parser = new JaxbParser();
            Persons persons = (Persons) parser.getObject(file, Persons.class);
            processData(persons);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
    }
    
    public void processData(Persons persons){
        try {
            PersonDAO dao = new PersonDAO();
            for(PersonDTO p: persons.getPersons()){
                if(dao.getPerson(p.getId())==null){
                    dao.importPerson(p);
                }
            }
            dao.destroy();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
