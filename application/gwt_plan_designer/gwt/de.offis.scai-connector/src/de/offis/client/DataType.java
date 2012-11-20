package de.offis.client;

import java.io.Serializable;

/**
 * Scai Data Model for DataType.
 *
 * @author Alexander Funk
 * 
 */
public class DataType extends AbstractScaiDTO implements Serializable {

	private static final long serialVersionUID = 7153006702089100786L;
	private String name;
	private Integer min;
	private Integer max;
	private String regex;
	private String defaultvalue;
	
	private Float minFloat;
	private Float maxFloat;
	private Float scaleFloat;
	private Float defaultvalueFloat;
	
	
	public DataType(String name, Integer min, Integer max, String regex, String defaultvalue) {
		super(ScaiType.DATA_TYPE, Type.STRING);
		this.name = name;
		this.min = min;
		this.max = max;
		this.regex = regex;
		this.defaultvalue = defaultvalue;
	}
	
	public DataType(String name, String defaultvalue) {
		super(ScaiType.DATA_TYPE, Type.BINARY);
		this.name = name;
		this.defaultvalue = defaultvalue;
	}
	
	public DataType(String name, Float min, Float max, Float scale, Float defaultvalue) {
		super(ScaiType.DATA_TYPE, Type.DECIMAL);
		this.name = name;
		this.minFloat = min;
		this.maxFloat = max;
		this.scaleFloat = scale;
		this.defaultvalueFloat = defaultvalue;
	}
	
	protected DataType() {
		super();
		// Serializable
	}

	public String getName() {
		return name;
	}

	public Integer getMin() {
		return min;
	}

	public Integer getMax() {
		return max;
	}

	public String getRegex() {
		return regex;
	}

	public String getDefaultvalue() {
		return defaultvalue;
	}

	public Float getMinFloat() {
		return minFloat;
	}

	public Float getMaxFloat() {
		return maxFloat;
	}

	public Float getScaleFloat() {
		return scaleFloat;
	}

	public Float getDefaultvalueFloat() {
		return defaultvalueFloat;
	}
}
