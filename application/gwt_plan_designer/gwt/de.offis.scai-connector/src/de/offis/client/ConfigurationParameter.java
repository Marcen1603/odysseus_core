package de.offis.client;

import java.io.Serializable;
import java.util.List;

/**
 * Scai Data Model for ConfigurationParameter.
 *
 * @author Alexander Funk
 * 
 */
public class ConfigurationParameter extends AbstractScaiDTO implements Serializable {
	
	private static final long serialVersionUID = 5860760906441269920L;
	private String name;
	private Boolean optional;
	private String uom;
	private Boolean identifier;
	private String dataTypeName;
	
	private List<String> configurationParameterNames;
	
	private Integer min;
	private Integer max;
	private String configurationParameterName;
	
	public ConfigurationParameter(String name, Boolean optional, String uom, Boolean identifier, String dataTypeName) {
		super(ScaiType.CONFIG_PARAM, Type.ATOMIC);
		this.name = name;
		this.optional = optional;
		this.uom = uom;
		this.identifier = identifier;
		this.dataTypeName = dataTypeName;
	}
	
	public ConfigurationParameter(String name, Boolean optional, List<String> configurationParameterNames) {
		super(ScaiType.CONFIG_PARAM, Type.COMPLEX);
		this.name = name;
		this.optional = optional;
		this.configurationParameterNames = configurationParameterNames;
	}
	
	public ConfigurationParameter(String name, Boolean optional, Integer min, Integer max, String configurationParameterName) {
		super(ScaiType.CONFIG_PARAM, Type.SEQUENCE);
		this.name = name;
		this.optional = optional;
		this.min = min;
		this.max = max;
		this.configurationParameterName = configurationParameterName;
	}

	protected ConfigurationParameter() {
		super();
		// Serializable
	}
	
	public String getName() {
		return name;
	}

	public Boolean getOptional() {
		return optional;
	}

	public String getUom() {
		return uom;
	}

	public Boolean getIdentifier() {
		return identifier;
	}

	public String getDataTypeName() {
		return dataTypeName;
	}

	public List<String> getConfigurationParameterNames() {
		return configurationParameterNames;
	}

	public Integer getMin() {
		return min;
	}

	public Integer getMax() {
		return max;
	}

	public String getConfigurationParameterName() {
		return configurationParameterName;
	}
}
