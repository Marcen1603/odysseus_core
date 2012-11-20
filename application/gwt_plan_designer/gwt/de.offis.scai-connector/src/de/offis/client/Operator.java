package de.offis.client;

import java.io.Serializable;
import java.util.Map;

/**
 * Scai Data Model for Operator.
 *
 * @author Alexander Funk
 * 
 */
public class Operator implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 390227853290682054L;
	
	public static final int INPUT = 0;
	public static final int OUTPUT = 11;
	public static final int SERVICE = 22;
	
	private String name;
	private int operatorType; // INPUT,OUTPUT,SERVICE
	private Map<String, String> properties;
	private String sensorName = null;
	private String sensorDomain = null;
	private String serviceType = null;
	
	@SuppressWarnings("unused")
	private Operator() {
		// Serializable
	}
	
	public Operator(String name, String sensorName, String sensorDomain, Map<String, String> properties, int operatorType) {
		this.name = name;
		this.sensorName = sensorName;
		this.sensorDomain = sensorDomain;
		this.properties = properties;
		this.operatorType = operatorType;
	}
	
	public Operator(String name, String serviceType, Map<String, String> properties) {
		this.name = name;
		this.serviceType = serviceType;
		this.properties = properties;
		this.operatorType = SERVICE;
	}
	
	public String getName() {
		return name;
	}
	
	public int getOperatorType() {
		return operatorType;
	}
	
	public Map<String, String> getProperties() {
		return properties;
	}
	
	public String getSensorDomain() {
		return sensorDomain;
	}
	
	public String getSensorName() {
		return sensorName;
	}
	
	public String getServiceType() {
		return serviceType;
	}
}
