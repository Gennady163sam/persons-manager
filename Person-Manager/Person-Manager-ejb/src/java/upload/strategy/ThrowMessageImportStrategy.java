package upload.strategy;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;
import parser.impl.JaxbParser;
import parser.impl.Persons;
import server.PersonDAO;
import server.PersonDTO;

public class ThrowMessageImportStrategy implements AbstractImportStrategy{
    
    @Override
    public void importData(File file) throws DuplicatePersonException {
        try {
            JaxbParser parser = new JaxbParser();
            Persons persons = (Persons) parser.getObject(file, Persons.class);
            processData(persons);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
    }
    
    public void processData(Persons persons) throws DuplicatePersonException{
        List<String> messages = new ArrayList<String>();
        try {
            PersonDAO dao = new PersonDAO();
            for(PersonDTO p: persons.getPersons()){
                if(dao.getPerson(p.getId())==null)
                    dao.createPerson(p);
                else
                    messages.add("Person - "+ p.getName() + " already exists! \n");
            }
            dao.destroy();
            isError(messages);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void isError(List<String> messages) throws DuplicatePersonException{
        if(!messages.isEmpty()){
            String errorMessage="";
            for(String message: messages)
                errorMessage+=message+"<br/>";
            throw new DuplicatePersonException(errorMessage);
        }
    }
}
