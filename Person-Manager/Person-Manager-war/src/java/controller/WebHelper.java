package controller;

import controller.Controller;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.Criteria;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import orm.entity.Attribute;
import orm.entity.Params;
import orm.entity.Person;

public class WebHelper {
    
    private static int NOT_A_PARENT = -1;
    private static int FIND_BY_ID = 1;
    
    public static String getFindedText(HttpServletRequest request){
        String find="";
        if(request.getParameter("find")!=null){ 
            find = request.getParameter("find");
        }
        return find;
    }
    
    public static String getParams(HttpServletRequest request){
        String result="id=" + request.getParameter("id") + "&name=" + 
                request.getParameter("name") + "&oper=" + request.getParameter("oper");
        return result;

    }
    
    public static int getSort(HttpServletRequest request){
        int sort = FIND_BY_ID;
        if(request.getParameter("sort")!=null){ 
            sort = Integer.parseInt(request.getParameter("sort"));
        }
        return sort;
    }
    
    public static String getValue(HttpServletRequest request){
        String value="";
        if(request.getParameter("value")!=null){ 
            value = request.getParameter("value");
        }
        return value;
    }
    
    public static int getInfoParentId(HttpServletRequest request){
        int parentId;
        if(request.getParameter("parentId")!=null){
            parentId = Integer.parseInt(request.getParameter("parentId"));
        }
        else{
            Controller cntr = new Controller();
            String[] info = cntr.getPersonInfo(Integer.parseInt(request.getParameter("id")));
            if(info[0]!= null)
                parentId = Integer.parseInt(info[0]);
            else
                parentId = NOT_A_PARENT;
            cntr.destroy();
        }
        return parentId;
    }
    
    public static String getInfoPerson(HttpServletRequest request){
        Controller cntr = new Controller();
        String res = cntr.getPersonInfo(Integer.parseInt(request.getParameter("id")))[1];
        cntr.destroy();
        return res;
    }
    
    public static String getOperation(HttpServletRequest request){
        String operation = "";
        if(request.getParameter("operation")!=null){
                operation = request.getParameter("operation");
        }
        return operation;
    }
    
    public static String getPersonName(HttpServletRequest request){
        String name = "";
         if(request.getParameter("name")!=null){ 
             name = request.getParameter("name");
         }
         return name;
    }
    
    public int getParentId(HttpServletRequest request){
        int parentId = NOT_A_PARENT;
        if(request.getParameter("parentId")!=null) 
            parentId = Integer.parseInt(request.getParameter("parentId"));
        return parentId;
    }
    
    public static String getPreparedFile(HttpServletRequest request){
        String res = "";
        if(request.getParameter("file")!=null){
            res = request.getParameter("file");
        }
        return res;
    }
    
    public static String getValue(Params param){
        if(param.getValueNumber()!=null)
            return param.getValueNumber().toString();
        else
            if(param.getValueString()!=null)
                return param.getValueString();
            else
                return param.getValueDate().toString();
    }
    
    public static String getValueOfAttribute(Attribute attr, Person person){
        if(person!=null){
            for(Params param: person.getParams()){
                if(param.getAttribute().getId() == attr.getId()){
                    return getValue(param);
                }
            }
        }
        
        return null;
    }
   
    // -------------- FOR FIND PERSONS--------------------------------------------
    
    private static boolean isNativeParams(String attrribute){
        if(attrribute.equals("id") || attrribute.equals("parentId") || attrribute.equals("name"))
            return true;
        else
            return false;
    }
    
    private static List<Person> getPersonsByName(int sort, String name){
        List<Person> persons;
        Controller cntr = new Controller();
        if(name==null || name==""){
            persons = cntr.getAllPerson(sort);
        }else{
            persons = cntr.findPersonByName(name, sort);
        }
        cntr.destroy();
        return persons;
    }
    
    private static List<Person> getPersonByNativeParams(String attribute, String value){
        List<Person> persons = null;
        Controller cntr = new Controller();
        if(value!=null && !value.equals("")){
            persons = cntr.findPersonByNativeParams(attribute, value);
        }
        cntr.destroy();
        return persons;
    }
    
    private static List<Person> getPersonsByAttributeParams(int sort, String attrId, String value){
        List<Person> persons;        
        Controller cntr = new Controller();
        if(value==null || value==""){
            persons = cntr.getAllPerson(sort);
        }else{
            persons = cntr.findPersonByAttributeValue(cntr.getAllPerson(sort),Integer.parseInt(attrId), value, sort);
        }
        cntr.destroy();
        return persons;
    }
    
    public static List<Person> getPersonsByFindAttribute(HttpServletRequest request){
        List<Person> persons = null;
        int sort = getSort(request);
        if(request.getParameter("type")!=null){
            if(isNativeParams(request.getParameter("type")))
                persons = getPersonByNativeParams(request.getParameter("type"), request.getParameter("find"));
            else{
                persons = getPersonsByAttributeParams(sort, request.getParameter("type"), request.getParameter("find"));
            }
        }else{
            persons = getPersonsByName(sort,request.getParameter("find"));
        }
        return persons;
    }
    
    private static List<FindCondition> createListConditions(JSONArray array){
        List<FindCondition> list = new ArrayList<FindCondition>();
        if(array.size()<1) return list;
        for(int i = 0; i<array.size();i++){
            String type = ((JSONObject)array.get(i)).get("type").toString();
            String find = ((JSONObject)array.get(i)).get("find").toString();
            FindCondition condition = new FindCondition(type, find);
            list.add(condition);
        }
        return list;
    } 
    
    public static List<FindCondition> getMap(String map){
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(map);
            JSONObject jsonObj = (JSONObject) obj;
            JSONArray arr = (JSONArray) jsonObj.get("map");
            return createListConditions(arr);
        } catch (ParseException ex) {
            System.err.println(ex.getMessage());
        } 
        return null;
    }
    
    
    private static List<Person> applyCriteriaToNativeParams(List<FindCondition> conditions){
        Controller cntr = new Controller();
        List<Person> persons = null;
        Criteria criteria = cntr.getAllCriteria();
        for(FindCondition condition: conditions){
            if(isNativeParams(condition.getType())){
                criteria = cntr.addParameterToCriteria(criteria, condition.getType(), condition.getFind());
            }
        }
        persons = cntr.getPersonFromCriteria(criteria);
                
        cntr.destroy();
        return persons;
    }
    
    private static List<Person> applyCriteriaToOtherParams(List<Person> persons,List<FindCondition> conditions){
        Controller cntr = new Controller();
        List<Person> localPersons = persons;
        for(FindCondition condition: conditions){
            if(!isNativeParams(condition.getType())){
                localPersons = cntr.findPersonByAttributeValue(localPersons,Integer.parseInt(condition.getType()) , condition.getFind(), FIND_BY_ID);
            }
        }
        cntr.destroy();
        return localPersons;
    }
    
    public static List<Person> getPersonsByFindAttributes(List<FindCondition> conditions){
        List<Person> persons = null;
        
        persons = applyCriteriaToNativeParams(conditions);
        persons = applyCriteriaToOtherParams(persons, conditions);
        return persons;
    }
    
}
