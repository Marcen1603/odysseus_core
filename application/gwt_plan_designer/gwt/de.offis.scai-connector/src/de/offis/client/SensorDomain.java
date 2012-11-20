package de.offis.client;

import java.io.Serializable;

/**
 * Scai Data Model for SensorDomain.
 *
 * @author Alexander Funk
 * 
 */
public class SensorDomain extends AbstractScaiDTO implements Serializable {

	private static final long serialVersionUID = 3639779513158021510L;
	private String name;

	public SensorDomain(String name) {
		super(ScaiType.SENSOR_DOMAIN, Type.NONE);
		this.name = name;
	}
	
	protected SensorDomain() {
		super(ScaiType.SENSOR_DOMAIN, Type.NONE);
		// Serializable
	}
	
	public String getName() {
		return name;
	}
}
