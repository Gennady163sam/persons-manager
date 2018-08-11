package orm.entity;

import java.io.Serializable;
import java.sql.Date;


public class Params implements Serializable{
    
    private Integer id;
    private Person person;
    private Attribute attribute;
    private String valueString;
    private Integer valueNumber;
    private Date valueDate;

    public Params() {
    }

    public Params(Person person, Attribute attribute, String valueString, Integer valueNumber, Date valueDate) {
        this.person = person;
        this.attribute = attribute;
        this.valueString = valueString;
        this.valueNumber = valueNumber;
        this.valueDate = valueDate;
    }
    
    public Params(Integer id,Person person, Attribute attribute, String valueString, Integer valueNumber, Date valueDate) {
        this.id = id;
        this.person = person;
        this.attribute = attribute;
        this.valueString = valueString;
        this.valueNumber = valueNumber;
        this.valueDate = valueDate;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    public Integer getValueNumber() {
        return valueNumber;
    }

    public void setValueNumber(Integer valueNumber) {
        this.valueNumber = valueNumber;
    }

    public Date getValueDate() {
        return valueDate;
    }

    public void setValueDate(Date valueDate) {
        this.valueDate = valueDate;
    }
    
    
}
