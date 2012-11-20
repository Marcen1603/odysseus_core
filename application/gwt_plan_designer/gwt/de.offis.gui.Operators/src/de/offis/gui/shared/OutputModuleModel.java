package de.offis.gui.shared;

import java.util.HashMap;
import java.util.Map;

import de.offis.gui.client.module.AbstractModuleModel;

/**
 * Model of a output.
 *
 * @author Alexander Funk
 * 
 */
public class OutputModuleModel extends AbstractModuleModel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5103349062960808521L;
	
	private String vsSensorName;
    private String vsSensorDomain;
    private String vsSensorType = "";
    
    
    private static int COUNTER = 1;
    
    @SuppressWarnings("unused")
	private OutputModuleModel(){
        // serializable
    }
    
    public OutputModuleModel(String id, String name, String sensorName, String sensorDomain, Map<String, String> properties) {
        super(ModuleType.OUTPUT, id, name);
        this.vsSensorName = sensorName;
        this.vsSensorDomain = sensorDomain;
        if(properties != null){
        	setProperties(properties);
        } else {
        	setProperties(new HashMap<String, String>());
        }
    }

    public OutputModuleModel(String id, String name, Map<String, String> properties) {
        this(id, name, "", "", properties);
    }
    
    public OutputModuleModel(String name, Map<String, String> properties) {
        this("Output-Blueprint" + COUNTER++, name, properties);
    }  


    /**
     * Copy-Constructor
     * @param temp
     */
    public OutputModuleModel(OutputModuleModel copy) {
        this(copy.getId(), copy.getName(), copy.getProperties());
        setVsSensorDomain(copy.vsSensorDomain);
        setVsSensorName(copy.vsSensorName);
        setVsSensorType(copy.vsSensorType);
    }
    
    public String getTarget(){
    	if(getProperties().containsKey("target")){
    		return getProperties().get("target");
    	} else {
    		return "";
    	}
    }

    public String getVsSensorDomain() {
        return vsSensorDomain;
    }

    public String getVsSensorName() {
        return vsSensorName;
    }

    public String getVsSensorType() {
        return vsSensorType;
    }

    public void setVsSensorDomain(String vsSensorDomain) {
        this.vsSensorDomain = vsSensorDomain;
    }

    public void setVsSensorType(String vsSensorType) {
        this.vsSensorType = vsSensorType;
    }

    public void setVsSensorName(String vsSensorName) {
        this.vsSensorName = vsSensorName;
    }
}
