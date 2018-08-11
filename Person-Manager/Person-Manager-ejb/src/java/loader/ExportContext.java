package loader;

import java.util.HashMap;
import java.util.Map;
import loader.strategy.ExportAllStrategy;
import loader.strategy.ExportNHierarchyStrategy;
import loader.strategy.ExportOnlyChildStrategy;
import loader.strategy.ExportOnlyParentStrategy;
import loader.strategy.OnlyNodeStrategy;
import loader.strategy.AbstractExportStrategy;

public class ExportContext {
    static Map <String,AbstractExportStrategy> map;
    
    static{
        map = new HashMap<String, AbstractExportStrategy>();
        
        map.put("0", new ExportAllStrategy());
        map.put("1", new ExportOnlyChildStrategy());
        map.put("2", new ExportOnlyParentStrategy());
        map.put("3", new OnlyNodeStrategy());
        map.put("4", new ExportNHierarchyStrategy());
    }
    
    public static AbstractExportStrategy getStrategy(String key){
        return  map.get(key);
    }
}
