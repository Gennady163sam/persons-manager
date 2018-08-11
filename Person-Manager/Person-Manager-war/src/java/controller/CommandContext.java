package controller;

import java.util.HashMap;
import java.util.Map;
import strategy.OperationCopyStrategy;
import strategy.OperationCreateStrategy;
import strategy.OperationDeleteStrategy;
import strategy.OperationEditStrategy;
import strategy.AbstractOperationStrategy;

public class CommandContext {
    static Map <String,AbstractOperationStrategy> map;
    
    static{
        map = new HashMap<String, AbstractOperationStrategy>();
        
        map.put("Edit", new OperationEditStrategy());
        map.put("Create", new OperationCreateStrategy());
        map.put("Copy", new OperationCopyStrategy());
        map.put("Delete", new OperationDeleteStrategy());
    }
    
    public static AbstractOperationStrategy getStrategy(String key){
        return  map.get(key);
    }
    
    
}
