package de.offis.gui.shared;

import java.util.Map;

import com.google.gwt.core.client.GWT;

import de.offis.gui.client.module.AbstractModuleModel;

/**
 * Model of a operator.
 *
 * @author Alexander Funk
 * 
 */
public class OperatorModuleModel extends AbstractModuleModel {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1019195410387625818L;

	private OperatorMetaTypes metaType;
    private String operatorType;
    
    private static int COUNTER = 1;
    
    @SuppressWarnings("unused")
	private OperatorModuleModel(){
        // serialize gwt
    }

    public OperatorModuleModel(String id, String name, OperatorMetaTypes metaType, String operatorType, Map<String, String> properties) {
        super(ModuleType.OPERATOR, id, name);
        this.metaType = metaType;
        this.operatorType = operatorType;
        setProperties(properties);
    }
    
    public OperatorModuleModel(String name, OperatorMetaTypes metaType, String operatorType, Map<String, String> properties) {
        this("Operator-Blueprint" + COUNTER++, name, metaType, operatorType, properties);
        GWT.log("Operator-Counter++"); // TODO remove
    }

    /**
     * Copy-Constructor.
     * @param temp
     */
    public OperatorModuleModel(OperatorModuleModel copy) {
        this(copy.getId(), copy.getName(), copy.metaType, copy.operatorType, copy.getProperties());
    }

    public int getInputCount() {	
        return Integer.parseInt(getProperty("inputSlots"));
    }

    public int getOutputCount() {
    	return Integer.parseInt(getProperty("outputSlots"));
    }

    public void setMetaType(OperatorMetaTypes metaType) {
		this.metaType = metaType;
	}    
    
    public OperatorMetaTypes getMetaType() {
        return metaType;
    }
    
    public String getOperatorType() {
		return operatorType;
	}
}
