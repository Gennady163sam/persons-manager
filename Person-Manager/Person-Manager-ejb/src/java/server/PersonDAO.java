package server;

import server.source.Attribute;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import orm.entity.HibernateUtil;
import orm.entity.Params;
import orm.entity.Person;
import server.source.Entry;
import server.source.Value;
import server.source.ValueDate;
import server.source.ValueNumber;
import server.source.ValueString;

public class PersonDAO {
    
    private static final String SELECT_ALL = "SELECT p.id,NVL(p.parent_id,-1),p.name FROM PERSONS p";
    private static final String SELECT_BY_NAME = "SELECT * FROM PERSONS p WHERE p.name LIKE ?";
    private static final String SELECT_CHILD = "SELECT * FROM PERSONS p WHERE p.parent_id = ?";
    private static final String SELECT_PERSON = "SELECT (SELECT a.name FROM ATTRIBUTES a WHERE a.attr_id=pr.attr_id) as \"ATTRIBUTE\",pr.vstr||pr.vdate||pr.vnumber as \"VALUE\"\n" +
                                            "  FROM PERSONS p LEFT JOIN PARAMS pr ON p.id=pr.id\n" +
                                            " WHERE p.id = ?";
    private static final String UPDATE_PERSON = "UPDATE PERSONS p SET p.parent_id=? , p.name=? WHERE p.id=?";
    private static final String UPDATE_CHILD = "UPDATE PERSONS p SET p.parent_id = NULL WHERE p.parent_id = ?";
    private static final String UPDATE_PARAMETER = "SELECT COUNT(*) \n" +
                                                    "  FROM PARAMS p\n" +
                                                    " WHERE p.id=? AND p.attr_id=?";
    private static final String SELECT_INFO_PERSON = "SELECT p.parent_id, p.name FROM PERSONS p WHERE p.id=?";
    
    private static final String SELECT_CHILD_IDS = "SELECT p.id\n" +
                                                    "FROM PERSONS p\n" +
                                                    "START WITH p.id = ? \n" +
                                                    "CONNECT BY p.parent_id = PRIOR p.id";
    
    private static final String SELECT_PARENT_IDS = "SELECT p.id\n" +
                                                    "FROM PERSONS p\n" +
                                                    "START WITH p.id = ? \n" +
                                                    "CONNECT BY p.id = PRIOR p.parent_id";
    
    private static final String SELECT_NHIERARCHY_IDS = "SELECT p.id FROM PERSONS p WHERE LEVEL< ? \n" +
                                                        "START WITH p.id = ?\n" +
                                                        "CONNECT BY p.parent_id = PRIOR p.id\n" +
                                                        "UNION\n" +
                                                        "SELECT p.name,LEVEL FROM PERSONS p WHERE LEVEL< ? \n" +
                                                        "START WITH p.id = ? \n" +
                                                        "CONNECT BY p.id = PRIOR p.parent_id";
    
    private DataSource dataSource;
    private Connection connect = null;
    private PreparedStatement stat = null;
    
    public PersonDAO() throws SQLException {
        init();
        connect = getConnection();
    }
 
    public void init() {
        try {
            InitialContext ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("jdbc/ConnectionPool");
        } catch (NamingException ex) {
            System.err.println(ex.getMessage());
        }
    }
 
    public Connection getConnection() throws SQLException {
        if (dataSource == null) 
            throw new SQLException("DataSource is null.");
        return dataSource.getConnection();
    }
    
    
    public List<PersonDTO> findAll(int sort){
        test();
        List<PersonDTO> persons;
        loadAttributes();
        try {
            stat = connect.prepareStatement(SELECT_ALL);
            ResultSet rs = stat.executeQuery();
            persons = fillList(rs);
            persons = sort(sort, persons);
            rs.close();
            stat.close();
            return persons;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }
    
//    ------------------TEST--------------------------------
    public void test(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            Person pers = (Person) session.get(Person.class,0);
            orm.entity.Attribute attr = (orm.entity.Attribute)session.get(orm.entity.Attribute.class, 0);
            Params param = new Params( pers, attr, "", 311, null);
            pers.getParams().add(param);
            session.save(param);
            session.save(pers);
            System.out.println(pers.getName());
            Set<Params> params = pers.getParams();
            for(Params p: params){
                System.out.println(p.getValueString() + " " + p.getValueNumber());
            }
            tx.commit();
            
        }catch(HibernateException e){
            System.err.println(e.getMessage());
            e.printStackTrace();
            if(tx!=null)
                tx.rollback();
        }finally{
            session.close();
        }
    }
    
    
    
//    ------------------------------------------------------
    public List<PersonDTO> findPerson(String name, int sort){
        List<PersonDTO> persons;
        loadAttributes();
        try {
            stat = connect.prepareStatement(SELECT_BY_NAME);
            stat.setString(1, name.replace('*', '%'));
            ResultSet rs = stat.executeQuery();
            persons=fillList(rs);
            persons=sort(sort, persons);
            rs.close();
            stat.close();
            return persons;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }
    
    public List<PersonDTO> findPerson(String name, String value, int sort){
        if(name.equals("") || value.equals(""))  return null;
        List<PersonDTO> persons;
        loadAttributes();
        try {
            stat = connect.prepareStatement(getSelectSQL(name));
            stat.setString(1, value.replace('*', '%'));
            ResultSet rs = stat.executeQuery();
            persons=fillList(rs);
            persons=sort(sort, persons);
            rs.close();
            stat.close();
            return persons;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }
    
    public List<PersonDTO> sort(int type,List<PersonDTO> persons){
        switch (type) {
            case 1:
                persons.sort(PersonDTO.IdComparator);
                break; 
            case 2:
                persons.sort(PersonDTO.ParentComparator);
                break;
            default:
                persons.sort(PersonDTO.NameComparator);
                break;
        }
        
        return persons;
    }
    
    public List<PersonDTO> findChild(int id) throws SQLException{
        PreparedStatement st = connect.prepareStatement(SELECT_CHILD);
        st.setInt(1, id);
        ResultSet rs = st.executeQuery();
        List<PersonDTO> persons = fillList(rs);
        rs.close();
        st.close();
        return persons;
    }
    
    
    public List<PersonDTO> fillList(ResultSet rs) throws SQLException{
        List<PersonDTO> persons = new ArrayList<PersonDTO>();
        int id = -1;
        PersonDTO dto;
        while(rs.next()){
            if(id != rs.getInt(1)){
                id = rs.getInt(1);
                dto = new PersonDTO();
                dto.setId(rs.getInt(1));
                dto.setParentId(rs.getInt(2));
                dto.setName(rs.getString(3));
                persons.add(dto);
            }
        }
        return persons;
    }
    
    public void destroy(){
        try {
            connect.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }    
    public void loadAttributes(){
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connect.prepareStatement("SELECT * FROM ATTRIBUTES");
            rs = st.executeQuery();
            while(rs.next()){
                Attribute attr = Attribute.newInstance(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getBoolean(4));
                MetaModel.attributes.put(rs.getString(2), attr);
            }
        } catch (SQLException ex) {
            System.err.println();
        }finally{
            try {
                rs.close();
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(PersonDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public List<PersonDTO> getAllPerson(){
        List<PersonDTO> persons = findAll(1);
        List<PersonDTO> result = new ArrayList<PersonDTO>();
        for(PersonDTO pers: persons){
            pers.setAttributes(getPerson(pers.getId()).getAttributes());
            result.add(pers);
        }
        return result;
    }
    
    public PersonDTO getPerson(int id){  
        PersonDTO person = null;
        try {
            stat = connect.prepareStatement(SELECT_PERSON);
            stat.setString(1, Integer.toString(id));
            try{
                person = fillPersonInfo(id);
            }catch(NumberFormatException ex){
                return null;
            }
            
            ResultSet rs = stat.executeQuery();
            while(rs.next())
                if(MetaModel.attributes.containsKey(rs.getString(1)))
                    person.getAttributes().add(new Entry(MetaModel.attributes.get(rs.getString("ATTRIBUTE")),createValue(rs.getString(2))));
            rs.close();
            stat.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println(ex.getMessage());
        } catch (ParseException ex) {
            System.err.println("Error parsing - " + ex.getMessage());
        }
        return person;
    }
    
    private PersonDTO fillPersonInfo(int id) throws NumberFormatException{
        PersonDTO pers = new PersonDTO();
        if(id!=-1){
            pers.setId(id);
            String[] prop = getPersonInfo(id);
            pers.setName(prop[1]);
            pers.setParentId(Integer.parseInt(prop[0]));
        }
        return pers;
    }
    
    public static Value createValue(String value) throws ParseException{
        if(value==null) return new ValueString("");
        if(checkInteger(value))
            return new ValueNumber(Integer.parseInt(value));
        else
            if(checkDate(value)){
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                formatter.setLenient(false);
                return new ValueDate(formatter.parse(value));
            }
                
            else
                return new ValueString(value);
    }
    
    public void setPerson(PersonDTO person) throws SQLException{ 
        try {
            stat = connect.prepareStatement(UPDATE_PERSON);
            stat.setInt(1, person.getParentId());
            stat.setString(2, person.getName());
            stat.setInt(3, person.getId());
            if(stat.executeUpdate()>0){
                for(Entry entry:person.getAttributes())
                    if(!entry.getValue().toString().equals(""))
                        updateParameter(person.getId(), entry.getAttribute(), entry.getValue().getValue());
            }
            stat.close();
            connect.commit();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            connect.rollback();
        }
    }
    
    public int copyPerson(PersonDTO person)throws SQLException{
        int newId;
        newId = insertPerson(person.getParentId(), person.getName());
        if(newId>-1)
            for(Entry entry:person.getAttributes())
                if(!entry.getValue().toString().equals(""))
                    updateParameter(newId, entry.getAttribute(), entry.getValue().getValue());
        connect.commit();
        return newId;
    }
    
    public int createPerson(PersonDTO person)throws SQLException{
        int newId = insertPerson(person.getParentId(), person.getName());
        if(newId>-1)
            for(Entry entry:person.getAttributes())
                if(!entry.getValue().toString().equals(""))
                    updateParameter(newId, entry.getAttribute(), entry.getValue().getValue());
        connect.commit();
        return newId;
    }
    
    public int importPerson(PersonDTO person)throws SQLException{
        
        int newId = insertPerson(person.getId(),person.getParentId(), person.getName());
        if(newId>-1)
            for(Entry entry:person.getAttributes())
                if(!entry.getValue().toString().equals(""))
                    updateParameter(newId, entry.getAttribute(), entry.getValue().getValue());
        connect.commit();
        return newId;
    }
    
    public void deletePerson(int id) throws SQLException{
        try {
            stat = connect.prepareStatement("DELETE FROM PERSONS p WHERE p.id=?");
            stat.setInt(1, id);
            stat.executeUpdate();
            deleteParams(id);
            deleteFromChild(id);
            stat.close();
            connect.commit();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            connect.rollback();
            //ex.printStackTrace();
        }
    }
    
    public void deleteParams(int id) throws SQLException{
        PreparedStatement st = connect.prepareStatement("DELETE FROM PARAMS p WHERE p.id=?");
        st.setInt(1, id);
        st.executeUpdate();
        st.close();
    }
    
    public void deleteFromChild(int id) throws SQLException{
        PreparedStatement st = connect.prepareStatement(UPDATE_CHILD);
        st.setInt(1, id);
        st.executeUpdate();
        st.close();
    }
    
    
     public int insertPerson(int id,int parentId, String name)throws SQLException{

        PreparedStatement st = connect.prepareStatement("INSERT INTO PERSONS p VALUES(?,?,?)");
        st.setInt(1, id);
        st.setInt(2, parentId);
        st.setString(3, name);
        st.executeUpdate();
        st.close();
        return id;
    }
    
    public int insertPerson(int parentId, String name)throws SQLException{
        int id = -1;
        PreparedStatement st = connect.prepareStatement("select sec_persons.nextval from dual");
        ResultSet rs = st.executeQuery();
        rs.next();
        id = rs.getInt(1);
        if(id!=-1){
            st = connect.prepareStatement("INSERT INTO PERSONS p VALUES(?,?,?)");
            st.setInt(1, id);
            st.setInt(2, parentId);
            st.setString(3, name);
            st.executeUpdate();
        }
        st.close();
        return id;
    }
    
    public String[] getPersonInfo(int id){
        String[] info= new String[2];
        try {
            PreparedStatement statement = connect.prepareStatement(SELECT_INFO_PERSON);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                if(rs.getString(1)!=null)
                    info[0] = rs.getString(1);
                else
                    info[0] = "-1";
                info[1] = rs.getString(2);
            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return info;
    }
    
    
    public void updateParameter(int id, Attribute attr, Object value) throws SQLException{
        PreparedStatement st = connect.prepareStatement(UPDATE_PARAMETER);
        st.setInt(1, id);
        st.setInt(2, attr.getId());
        ResultSet rs = st.executeQuery();
        if(rs.next()){
            int count = rs.getInt(1);
            if(count==1)
                setParameter(id, attr, value.toString());
            else
                insertParameter(id, attr, value.toString());
        }
        rs.close();
        st.close();
    }
    
    public void setParameter(int id, Attribute attr, Object value) throws SQLException{
        PreparedStatement statement = connect.prepareStatement(getUpdateSQL(attr.getType())); 
        statement.setString(1, value.toString());
        statement.setInt(2, id);
        statement.setInt(3, attr.getId());
        
        statement.executeUpdate();
        statement.close();
    }
    
     public void insertParameter(int id, Attribute attr, Object value) throws SQLException{
        PreparedStatement statement = connect.prepareStatement(getInsertSQL(attr.getType()));
        statement.setInt(1, id);
        statement.setInt(2, attr.getId());
        statement.setString(3, value.toString());
        
        statement.executeUpdate();
        statement.close();
     }
    
    public String getUpdateSQL(String type){
        switch (type) {
            case "STRING":
                return "UPDATE PARAMS p\n  SET p.vstr=? \n WHERE p.id=? AND p.attr_id=?";
            case "NUMBER":
                return "UPDATE PARAMS p\n  SET p.vnumber=? \n WHERE p.id=? AND p.attr_id=?";
            default:
                return "UPDATE PARAMS p\n  SET p.date=? \n WHERE p.id=? AND p.attr_id=?";
        }
    }
    
    public String getInsertSQL(String type){
        switch (type) {
            case "STRING":
                return "INSERT INTO PARAMS VALUES(?,?,?,NULL,NULL)";
            case "NUMBER":
                return "INSERT INTO PARAMS VALUES(?,?,NULL,NULL,?)";
            default:
                return "INSERT INTO PARAMS VALUES(?,?,NULL,?,NULL)";
        }
    }
    
    public String getSelectSQL(String name){
        Map<String,String> SQL = new HashMap<String,String>();
        SQL.put("id", "SELECT * FROM PERSONS p WHERE p.id LIKE ?");
        SQL.put("parent_id", "SELECT * FROM PERSONS p WHERE p.parent_id LIKE ?");
        SQL.put("name", "SELECT * FROM PERSONS p WHERE p.name LIKE ?");
        return SQL.get(name);
    }
    
    
    public List getChildIds(int id){
        List child = new ArrayList();
        try {
            PreparedStatement statement = connect.prepareStatement(SELECT_CHILD_IDS);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                child.add(rs.getInt(1));
            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return child;
    }
    
    public List getParentIds(int id){
        List child = new ArrayList();
        try {
            PreparedStatement statement = connect.prepareStatement(SELECT_PARENT_IDS);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                child.add(rs.getInt(1));
            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return child;
    }
    
    public List getNHierarchyId(int id,int n){
        List child = new ArrayList();
        try {
            PreparedStatement statement = connect.prepareStatement(SELECT_NHIERARCHY_IDS);
            statement.setInt(1, n);
            statement.setInt(2, id);
            statement.setInt(3, n);
            statement.setInt(4, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                child.add(rs.getInt(1));
            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return child;
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
