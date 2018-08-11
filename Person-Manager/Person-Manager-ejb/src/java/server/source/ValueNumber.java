package server.source;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ValueNumber")
@XmlAccessorType(XmlAccessType.FIELD)
public class ValueNumber<Integer> extends Value{
    @XmlElement(name = "value")
    private Integer value;
    
    public ValueNumber() {
    }
    
    public ValueNumber(Integer value) {
        this.value = value;
    }
    
    public void setValue(Integer value) {
        this.value = value;
    } 
    
    @Override
    public Integer getValue() {
        return this.value;
    }
    
    
}
