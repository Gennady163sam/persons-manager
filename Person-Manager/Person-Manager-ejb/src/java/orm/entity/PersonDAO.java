package orm.entity;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class PersonDAO {
    
    private static final int FIND_BY_ID = 1;
    private static final int FIND_BY_PARENT_ID = 2;
    private static final int FIND_BY_NAME = 3;
    
    private static final int NOT_A_PARENT = -1;
    
    private Transaction tx = null;
    private Session session = null;
    
    public PersonDAO(){
        session = HibernateUtil.getSessionFactory().openSession();
        tx = session.beginTransaction();
    }

    public List<Person> findAll(int sort){
        loadAttributes();
        List<Person> persons = session.createCriteria(Person.class).
                addOrder(Order.asc(getTypeSort(sort))).
                list();
        return persons;
    }
    
    public String getTypeSort(int type){
        switch(type){
            case 1: return "id";
            case 2: return "parentId";
            default: return "name";
        }
    }
    
    public Criteria getAllCriteria(){
        return session.createCriteria(Person.class);
    }
    
    public Criteria addParameterToCriteria(Criteria criteria,String attrName, String value ){
        if(isInteger(value)){
            return criteria.
               add(Restrictions.like(attrName, Integer.parseInt(value)));
        }else{
            String localName = value.replace('*', '%');
            return criteria.
                add(Restrictions.like(attrName, localName));
        }
    }
    
    public List<Person> getPersonFromCriteria(Criteria criteria){
        return criteria.list();
    }
    
    public List<Person> findPersonByName(String name, int sort){
        List<Person> persons;
        String localName = name.replace('*', '%'); //Для поиска клаузой LIKE
        loadAttributes();
        persons = session.createCriteria(Person.class).
                add(Restrictions.like("name", localName)).
                addOrder(Order.asc(getTypeSort(sort))).
                list();
        return persons;
    }
    
    public List<Person> findPersonByNativeParams(String attrName, String value){
        List<Person> persons;
        loadAttributes();
        if(isInteger(value)){
            persons = session.createCriteria(Person.class).
                add(Restrictions.like(attrName, Integer.parseInt(value))).
                list();
        }else{
            String localName = value.replace('*', '%');
            persons = session.createCriteria(Person.class).
                add(Restrictions.like(attrName, localName)).
                list();
        }
        return persons;
    }
    
    public List<Person> findPersonByAttributeValue(List<Person> persons,int attrId,String value,int sort)
    {
        List<Person> resultPersons = null;
        loadAttributes();
        for(Person person : persons){
            for(Params personParameter: person.getParams()){
                if(personParameter.getAttribute().getId() == attrId)
                    if(isEqualParameter(personParameter, value)){
                        if(resultPersons==null) 
                            resultPersons = new ArrayList<Person>();
                        resultPersons.add(person);
                    }
            }
        }
        return resultPersons;
    }
    
    
    private boolean isEqualParameter(Params personParameter, String value){
        return isEqualDate(personParameter.getValueDate(),value)||
               isEqualInt(personParameter.getValueNumber(),value)||
               isEqualString(personParameter.getValueString(),value);
    }
    
    private boolean isEqualString(String valString, String resValue){
        if(valString == null) return false;
        return valString.equals(resValue);
    }
    
    private boolean isEqualInt(Integer number,String value){
        if(number == null) return false;
        return Integer.parseInt(value) == number;
    }
    
    private boolean isEqualDate(Date date, String value){
        if(date==null) return false;
        return value.equals(date.toString());
    }
    
    public List<Person> findChild(int id) throws SQLException{
        Query query = session.createQuery("from Person p where p.parentId = :id");
        query.setParameter("id", id);
        List<Person> persons = query.list();
        return persons;
    }
 
    public void destroy(){
        session.close();
    }    
    
    public void loadAttributes(){
        List<Attribute> attributes = session.
                createCriteria(Attribute.class).
                list();
        for(Attribute attr:attributes)
            MetaModel.attributes.put(attr.getName(), attr);
    }
    
    public List<Person> getAllPerson(){
        List<Person> persons = findAll(FIND_BY_ID);
        return persons;
    }
    
    public Person getPerson(int id){  
        Person person = (Person) session.get(Person.class, id);
        return person;
    }
    
    public void setPerson(Person person){
        try{
            session.update(person);
            tx.commit();
        }catch(HibernateException ex){
            System.err.println(ex.getMessage());
        }

    }
    
    public int createPerson(Person person){
        int newId = (int) session.save(person);
        tx.commit();
        return newId;
    }
    
    public void deletePerson(int id) throws SQLException{
        Person person = (Person) session.get(Person.class, id);
        deleteParams(id);
        session.delete(person);
        tx.commit();
        deleteFromChild(id);
    }
    
    private boolean isInteger(String value){
        try{
            int res = Integer.parseInt(value);
            return true;
        }catch(NumberFormatException ex){
            return false;
        }
    }
    
    public void deleteParams(int id){
        Query query = session.createQuery("delete Params p where p.person.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
    
    public void deleteFromChild(int id) throws SQLException{
        tx = session.beginTransaction();
        Query query = session.createQuery("from Person p where p.parentId = :id");
        query.setParameter("id", id);
        List<Person> persons = query.list();
        for(Person p: persons){
            p.setParentId(NOT_A_PARENT);
        }
        tx.commit();
    }

    public String[] getPersonInfo(int id){
        String[] info= new String[2];
        Person person = (Person) session.get(Person.class, id);
        if(person!=null){
            info[0] = person.getParentId().toString();
            info[1] = person.getName();
        }else{
            info[0] = Integer.toString(NOT_A_PARENT);
            info[1] = null;
        }
        return info;
    }
    
    public Params createParams(Person person, Attribute attr, String value){
        Params result = null;
        if(value==null || value.equals("")) return null;
        for(Params par:person.getParams())
            if(par.getAttribute().getId() == attr.getId())
                result = setParameter(par, person, attr, value);
        
        if(result==null){
            result = checkParams(person, attr, value);
            session.saveOrUpdate(result);
            tx.commit();
            return result;
        }else{
            session.saveOrUpdate(result);
            tx.commit();
            return null;
        }
    }
    
    private Params setParameter(Params parameter, Person person, Attribute attr, String value){
        Params params = checkParams(person, attr, value);
        parameter.setValueDate(params.getValueDate());
        parameter.setValueNumber(params.getValueNumber());
        parameter.setValueString(params.getValueString());
        return parameter;
    }
    
    public Params checkParams(Person person, Attribute attr, String value){
        Params params = null;
        if(checkInteger(value))
            params = new Params(person, attr, null, Integer.parseInt(value), null);
        else
            if(checkDate(value)){
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                formatter.setLenient(false);
                params = new Params(person, attr, null, null, java.sql.Date.valueOf(value));
            }else
                params = new Params(person, attr, value, null, null);
        
        return params;
    }
    
    
    public static boolean checkInteger(String string) {
        try{
            Integer.parseInt(string);
        }catch (Exception e){
            return false;
        }
        return true;
    }
    
    public static boolean checkDate (String sDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        formatter.setLenient(false);
        try{
            Date date = formatter.parse(sDate);
            if (!formatter.format(date).equals(sDate))
                return false;
        } 
        catch (ParseException e){
            return false;
        }
        return true;
    }

}
