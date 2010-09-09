/*
 * Created on 06.12.2004
 *
 */
package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

/**
 * @author Marco Grawunder
 *
 */
public class SDFNumberConstant extends SDFConstant {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -2412682973178628837L;

	/**
     * @param URI
     * @param value
     */
    public SDFNumberConstant(String URI, double value) {
        super(URI, ""+value,SDFDatatypeFactory.getDatatype(SDFDatatypes.Number));
    }

    /**
     * @param constValue
     */
    public SDFNumberConstant(SDFConstant constValue) {
        super(constValue.getURI(false),constValue.getString(), constValue.getDatatype());
    }

    /**
     * @param URI
     * @param value
     */
    public SDFNumberConstant(String URI, String value) {
        super(URI,value, SDFDatatypeFactory.getDatatype(SDFDatatypes.Number));
    }

    @Override
    protected void setValue(Object value) {
    	if (value instanceof Number) {
    		this.doubleValue = ((Number)value).doubleValue();
    		this.value = null;
    	}
    }
    
	@Override
	public boolean isDouble() {
		return true;
	}
	
	@Override
	public boolean isString() {
		return false;
	}

}
