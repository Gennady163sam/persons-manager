package orm.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Person implements Serializable{
    
    private Integer id;
    private Integer ParentId;
    private String name;
    private Set<Params> params = new HashSet<Params>();

    public Person(){
    }

    public Person(Integer id, Integer ParentId, String name, Set<Params> attributes) {
        this.id = id;
        this.ParentId = ParentId;
        this.name = name;
        this.params = attributes;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return ParentId;
    }

    public void setParentId(Integer ParentId) {
        this.ParentId = ParentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Params> getParams() {
        return params;
    }

    public void setParams(Set<Params> params) {
        this.params = params;
    }


    

    
}
