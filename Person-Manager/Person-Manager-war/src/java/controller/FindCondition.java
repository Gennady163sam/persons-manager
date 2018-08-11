package controller;

public class FindCondition {
    
    private String type;
    private String find;
    
    public FindCondition(){
        
    }
    
    public FindCondition(String type, String find) {
        this.type = type;
        this.find = find;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFind() {
        return find;
    }

    public void setFind(String find) {
        this.find = find;
    }
    
    
}
