package orm.entity;

import java.io.Serializable;

public class Attribute implements Serializable {

    private Integer id;
    private String name;
    private String type;
    private Integer single;

    public Attribute() {

    }

    public Attribute(Integer id, String name, String type, Integer single) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.single = single;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSingle() {
        return single;
    }

    public void setSingle(Integer single) {
        this.single = single;
    }

}
