package de.offis.client;

import java.io.Serializable;

/**
 * Base class for all Scai Data elements.
 *
 * @author Alexander Funk
 * 
 */
public abstract class AbstractScaiDTO implements Serializable {
	private static final long serialVersionUID = -5152153295496986983L;

	public static enum ScaiType {
		CONFIG_PARAM, DATA_ELEMENT, DATASTREAM_TYPE, DATA_TYPE, OPERATOR_GROUP, OPERATOR_TYPE, SENSOR, SENSOR_CATEGORY, SENSOR_DOMAIN, SENSOR_TYPE;
	}
	
	public static enum Type {
		NONE, ATOMIC, COMPLEX, SEQUENCE, STRING, BINARY, DECIMAL;
	}
	
	private ScaiType scaiType;
	private Type type;
	
    public AbstractScaiDTO(ScaiType scaiType, Type type) {
    	this.scaiType = scaiType;
    	this.type = type;
    }
    
    protected AbstractScaiDTO() {
    	// zur serialisierung
	}
    
    public ScaiType getScaiType() {
		return scaiType;
	}
    
    public Type getType() {
		return type;
	}
}
