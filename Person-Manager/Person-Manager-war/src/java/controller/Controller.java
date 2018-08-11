package controller;

import java.sql.SQLException;
import java.util.List;
import orm.entity.Attribute;
import orm.entity.Params;
import orm.entity.PersonDAO;
import orm.entity.Person;
import org.hibernate.Criteria;

public class Controller {
    private final PersonDAO dao;
    
    public Controller(){
        dao = new PersonDAO();
    }
    
    public List<Person> getAllPerson(int sort){
        return  dao.findAll(sort);
    }
    
    public List<Person> findPersonByName(String name,int sort){
        return dao.findPersonByName(name,sort);
    }
    
    public List<Person> findPersonByAttributeValue(List<Person> persons,int attrId, String value,int sort){
        return dao.findPersonByAttributeValue(persons,attrId,value,sort);
    }
    
    public List<Person> findPersonByNativeParams(String attr, String value){
        return dao.findPersonByNativeParams(attr, value);
    }
    
    public List<Person> findChild(int id) throws SQLException{
        return dao.findChild(id);
    }
    
    public Person getPerson(int id){
        return dao.getPerson(id);
    }
    
    public void setPerson(Person person) throws SQLException{
        dao.setPerson(person);
    }
    
    public int createPerson(Person person){
        return dao.createPerson(person);
    }
    
    public void deletePerson(int id) throws SQLException{
        dao.deletePerson(id);
    }
    
    public String[] getPersonInfo(int id){
        return dao.getPersonInfo(id);
    }
    
    public Params createParams(Person person, Attribute attr, String value){
        return dao.createParams(person, attr, value);
    }
    
    public Criteria getAllCriteria(){
        return dao.getAllCriteria();
    }
    
    public Criteria addParameterToCriteria(Criteria criteria,String attrName, String value ){
        return dao.addParameterToCriteria(criteria, attrName, value);
    }
    
    public List<Person> getPersonFromCriteria(Criteria criteria){
        return dao.getPersonFromCriteria(criteria);
    }
    
    
    public void destroy(){
        dao.destroy();
    }
    
}
