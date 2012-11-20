package de.offis.client;

import java.io.Serializable;
import java.util.List;

/**
 * Scai Data Model for SensorCategory.
 *
 * @author Alexander Funk
 * 
 */
public class SensorCategory extends AbstractScaiDTO implements Serializable {

	private static final long serialVersionUID = 8333934917923202219L;
	private String name;
	private String parentSensorReferenceName;
	private List<String> sensorDomainNames;
	
	public SensorCategory(String name, String parentSensorReferenceName,
			List<String> sensorDomainNames) {
		super(ScaiType.SENSOR_CATEGORY, Type.NONE);
		this.name = name;
		this.parentSensorReferenceName = parentSensorReferenceName;
		this.sensorDomainNames = sensorDomainNames;
	}
	
	protected SensorCategory() {
		super(ScaiType.SENSOR_CATEGORY, Type.NONE);
		// Serializable
	}
	
	public String getName() {
		return name;
	}
	
	public String getParentSensorReferenceName() {
		return parentSensorReferenceName;
	}
	
	public List<String> getSensorDomainNames() {
		return sensorDomainNames;
	}
}
