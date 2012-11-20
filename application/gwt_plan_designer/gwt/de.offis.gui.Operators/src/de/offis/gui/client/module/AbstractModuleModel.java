package de.offis.gui.client.module;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for models of input, output and operators.
 *
 * @author Alexander Funk
 * 
 */
public abstract class AbstractModuleModel implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7982206984884215922L;

	public enum ModuleType {

        SENSOR, OPERATOR, OUTPUT;
    }
    private ModuleType type;
    protected String id;
    protected String name;
    protected Map<String, String> properties = new HashMap<String, String>();

    @Override
    public String toString() {
        return "[Module." + type.toString() + ": " + id + ", " + name + ", " + properties.toString() + "]";
    }

    protected AbstractModuleModel() {
        // serializable
    }

    public AbstractModuleModel(ModuleType type, String id, String name) {
        assert (type != null);
        this.name = name;
        this.id = id;
        this.type = type;
    }

    public ModuleType getModuleType() {
        return this.type;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public void setName(String name) {
		this.name = name;
	}

    public String getName() {
		return name;
	}
    
    public String getId() {
        return id;
    }
    
	public void setProperty(String key, String value){
		if(properties != null){
			properties.put(key, value);
		}
	}
	
	public String getProperty(String key){		
		if (properties != null && properties.containsKey(key)) {
			return properties.get(key);
		}
		
		return null;
	}
	
    public HashMap<String, String> getProperties() {
        if(properties == null){
            return new HashMap<String, String>();
        }       
        
        return new HashMap<String, String>(properties);
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = new HashMap<String, String>(properties);
    }
}
