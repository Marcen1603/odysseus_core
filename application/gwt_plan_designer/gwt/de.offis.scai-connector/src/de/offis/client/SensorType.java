package de.offis.client;

import java.io.Serializable;
import java.util.List;

/**
 * Scai Data Model for SensorType.
 *
 * @author Alexander Funk
 * 
 */
public class SensorType extends AbstractScaiDTO implements Serializable {

	private static final long serialVersionUID = 7552776383808594177L;
	private String name;
	private String adapter;
	private String dataStreamTypeName;
	private List<String> configurationParameterNames;
	private List<String> sensorCategoryNames;
	private List<String> sensorDomainNames;
	
	public SensorType(String name, String adapter, String dataStreamTypeName,
			List<String> configurationParameterNames,
			List<String> sensorCategoryNames, List<String> sensorDomainNames) {
		super(ScaiType.SENSOR_TYPE, Type.NONE);
		this.name = name;
		this.adapter = adapter;
		this.dataStreamTypeName = dataStreamTypeName;
		this.configurationParameterNames = configurationParameterNames;
		this.sensorCategoryNames = sensorCategoryNames;
		this.sensorDomainNames = sensorDomainNames;
	}
	
	protected SensorType() {
		super(ScaiType.SENSOR_TYPE, Type.NONE);
	}

	public String getName() {
		return name;
	}

	public String getAdapter() {
		return adapter;
	}

	public String getDataStreamTypeName() {
		return dataStreamTypeName;
	}

	public List<String> getConfigurationParameterNames() {
		return configurationParameterNames;
	}

	public List<String> getSensorCategoryNames() {
		return sensorCategoryNames;
	}

	public List<String> getSensorDomainNames() {
		return sensorDomainNames;
	}
}
