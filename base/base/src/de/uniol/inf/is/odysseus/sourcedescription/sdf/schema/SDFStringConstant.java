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
public class SDFStringConstant extends SDFConstant {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3768336548373743717L;

	/**
     * @param URI
     * @param value
     */
    public SDFStringConstant(String URI, String value) {
        super(URI, value, SDFDatatypeFactory.getDatatype(SDFDatatypes.String));
    }

    /**
     * @param constValue
     */
    public SDFStringConstant(SDFConstant constValue) {
        super(constValue.getURI(false), constValue.getString(), SDFDatatypeFactory.getDatatype(SDFDatatypes.String));
    }

	@Override
	public boolean isDouble() {
		return false;
	}

	@Override
	public boolean isString() {
		return true;
	}

}
