package server.source;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ValueString")
@XmlAccessorType(XmlAccessType.FIELD)
public class ValueString<String> extends Value{
    @XmlElement(name = "value")
    private String value;    
    
    public ValueString() {
    }

    public ValueString(String value) {
        this.value = value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public String getValue() {
        return this.value;
    }
}
