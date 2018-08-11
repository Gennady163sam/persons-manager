package server.source;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

@XmlSeeAlso({ValueNumber.class,ValueString.class,ValueDate.class})
public abstract class Value<T> {
    
    @XmlTransient
    public abstract T getValue();

}
