package de.offis.gui.shared;

import de.offis.gui.client.module.AbstractModuleModel;

/**
 * Model of a input.
 *
 * @author Alexander Funk
 * 
 */
public class InputModuleModel extends AbstractModuleModel {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2397696680744857746L;
	/**
     * Der zugrundeliegende Sensor, also Datenlieferant.
     */
    private String sensor;
    private String[] dataElements;
    private String domain;
    private String sensorType;
    
    private static int COUNTER = 1;
    
    @SuppressWarnings("unused")
	private InputModuleModel(){

    }
    
    public InputModuleModel(String id, String name, String sensor, String domain, String sensorType, String[] dataElements) {
        super(ModuleType.SENSOR, id, name);
        this.sensor = sensor;
        this.dataElements = dataElements;
        this.domain = domain;
        this.sensorType = sensorType;
    }

    public InputModuleModel(String name, String sensor, String domain, String sensorType, String[] dataElements) {
    	this("Sensor-Blueprint" + COUNTER++, name, sensor, domain, sensorType, dataElements);
    }

    /**
     * Copy-Constructor. 
     * @param temp
     */
    public InputModuleModel(InputModuleModel copy) {
        this(copy.getId(), copy.getName(), copy.sensor, copy.domain, copy.sensorType, copy.dataElements);
    }

    public String getSensor() {
        return sensor;
    }

    public void setDataElements(String[] dataElements) {
		this.dataElements = dataElements;
	}
    
    public void setSensorType(String sensorType) {
		this.sensorType = sensorType;
	}
    
    public String[] getDataElements() {
        return dataElements;
    }
    
    public String getDomain() {
		return domain;
	}
    
    public String getSensorType() {
		return sensorType;
	}
}
