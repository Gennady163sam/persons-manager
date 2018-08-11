package server;

import java.util.ArrayList;
import server.source.Attribute;
import java.util.Comparator;
import java.util.List;
import javax.xml.bind.annotation.*;
import server.source.Entry;

@XmlRootElement
@XmlType(propOrder = {"id","parentId","name","attributes"})
public class PersonDTO implements Comparable<PersonDTO>{
    
    private int id;
    private int parentId;
    private String name;
    
    private List<Entry> attributes;
    
    public PersonDTO(){
        attributes = new ArrayList<Entry>();
    }
    
    public PersonDTO(int id, int parentId, String name){
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        attributes = new ArrayList<Entry>();
    }
    
    public PersonDTO(int id, int parentId, String name, ArrayList<Entry> attributes) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.attributes = attributes;
    }

    public int getId() {
        return id;
    }

    public int getParentId() {
        return parentId;
    }

    public String getName() {
        return name;
    }
    
    public Object getValuesOfAttribute(Attribute attr){
        for(Entry entry:this.getAttributes()){
            if(attr.getId()==entry.getAttribute().getId())
                return entry.getValue().getValue();
        }
        return null;
    }
    
    public List<Entry> getAttributes() {
        return attributes;
    }
    @XmlAttribute
    public void setId(int id) {
        this.id = id;
    }
    @XmlAttribute
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
    @XmlAttribute
    public void setName(String name) {
        this.name = name;
    }
    
    @XmlElementWrapper(name="attributes")
    @XmlElementRef
    public void setAttributes(List<Entry> attributes) {
        this.attributes = attributes;
    }
    
    @Override
    public boolean equals(Object object){
        if (object == this) {
           return true;
        }
        if (object instanceof PersonDTO)
            if (((PersonDTO)object).getId()== this.getId())
                return true;
        return false; 
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        Object result = null;
        result = super.clone();  
        return result;
    }

    @Override
    public int compareTo(PersonDTO person) {
        return this.id - person.id;
    }
    
    public static Comparator<PersonDTO> IdComparator = new Comparator<PersonDTO>() {
    
        @Override
        public int compare(PersonDTO p1, PersonDTO p2){
            return p1.getId() - p2.getId();
        }
    };
    
    public static Comparator<PersonDTO> ParentComparator = new Comparator<PersonDTO>() {
    
        @Override
        public int compare(PersonDTO p1, PersonDTO p2){
            return p1.getParentId() - p2.getParentId();
        }
    };
    
    public static Comparator<PersonDTO> NameComparator = new Comparator<PersonDTO>() {
    
        @Override
        public int compare(PersonDTO p1, PersonDTO p2){
            return p1.getName().compareTo(p2.getName());
        }
    };
    
}
