package parser.impl;

import java.util.List;
import javax.xml.bind.annotation.*;
import server.PersonDTO;

@XmlRootElement(name = "company")
@XmlAccessorType(XmlAccessType.FIELD)
public class Persons {
    
    @XmlElement(name = "person")
    private List<PersonDTO> persons = null;
    
    public Persons(){
        
    }
    
    public Persons(List<PersonDTO> persons){
        this.persons = persons;
    }
    
    public List<PersonDTO> getPersons(){
        return this.persons;
    }
}
