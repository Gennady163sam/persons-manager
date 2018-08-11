package upload;

import java.util.HashMap;
import java.util.Map;
import upload.strategy.EraseImportStrategy;
import upload.strategy.AbstractImportStrategy;
import upload.strategy.IgnoreImportStrategy;
import upload.strategy.ThrowMessageImportStrategy;

public class ImportContext {
    
    static Map <String,AbstractImportStrategy> map;
    
    static{
        map = new HashMap<String, AbstractImportStrategy>();
        map.put("delete", new EraseImportStrategy());
        map.put("ignore", new IgnoreImportStrategy());
        map.put("alert", new ThrowMessageImportStrategy());
    }
    
    public static AbstractImportStrategy getStrategy(String key){
        return  map.get(key);
    }
}
