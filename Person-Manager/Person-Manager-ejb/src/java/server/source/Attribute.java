package server.source;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "attribute")
@XmlType(propOrder = {"id","name","type","single"})
@XmlAccessorType(XmlAccessType.FIELD)
public class Attribute {
    
    @XmlAttribute
    private int id;
    @XmlAttribute
    private String name;
    @XmlAttribute
    private String type;
    @XmlAttribute
    private boolean single;
    
    public Attribute(){
        
    }
    
    public static Attribute newInstance(int id,String name,String type,boolean single){
        Attribute attr = new Attribute();
        attr.setName(name);
        attr.setId(id);
        attr.setType(type);
        attr.setSingle(single);
        return attr;
    }
    
    public int getId(){
        return this.id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isSingle() {
        return single;
    }
    
    
    public void setId(int id) {
        this.id = id;
    }
    
    
    public void setName(String name) {
        this.name = name;
    }
    
    
    public void setType(String type) {
        this.type = type;
    }
    
    
    public void setSingle(boolean single) {
        this.single = single;
    }
    
    @Override
    public boolean equals(Object obj) {
        
        if (this == obj) 
            return true;
        
        if (obj == null) 
            return false;
        
        if (getClass() != obj.getClass())
            return false;
        
        final Attribute other = (Attribute) obj;
        
        return this.id == other.id;
    }
}
