package server.source;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ValueDate")
@XmlAccessorType(XmlAccessType.FIELD)
public class ValueDate<Date> extends Value{
    @XmlElement(name = "value")
    private Date value;
    
    public ValueDate() {
    }
    
    public ValueDate(Date value) {
        this.value = value;
    }
    
    public void setValue(Date value) {
        this.value = value;
    }

    @Override
    public Date getValue() {
        return this.value;
    }
}
