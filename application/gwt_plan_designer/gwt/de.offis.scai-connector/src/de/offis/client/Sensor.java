package de.offis.client;

import java.io.Serializable;

/**
 * Scai Data Model for Sensor.
 *
 * @author Alexander Funk
 * 
 */
public class Sensor extends AbstractScaiDTO implements Serializable {

	private static final long serialVersionUID = -2812172535005942353L;
	private String name;
	private String sensorDomainName;
	private String sensorTypeName;
	private boolean virtual;

	public Sensor(String name, String sensorDomainName, String sensorTypeName, boolean virtual) {
		super(ScaiType.SENSOR, Type.NONE);
		this.name = name;
		this.sensorDomainName = sensorDomainName;
		this.sensorTypeName = sensorTypeName;
		this.virtual = virtual;		
	}
	
	protected Sensor(){
		super(ScaiType.SENSOR, Type.NONE);
		// zur serialisierung
	}
	
	public String getName() {
		return name;
	}

	public String getSensorDomainName() {
		return sensorDomainName;
	}

	public String getSensorTypeName() {
		return sensorTypeName;
	}

	public boolean isVirtual() {
		return virtual;
	}
}
