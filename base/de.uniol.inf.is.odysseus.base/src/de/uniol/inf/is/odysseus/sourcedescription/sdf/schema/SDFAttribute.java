package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import java.io.Serializable;

public class SDFAttribute extends SDFSchemaElement implements Comparable<SDFAttribute>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5128455072793206061L;

	public SDFAttribute(String URI) {
		super(URI);
	}
	
	public SDFAttribute(SDFAttribute attribute) {
		super(attribute);
	}

	@Override
	public SDFAttribute clone() {
		return new SDFAttribute(this);
	}
	
	@Override
	public int compareTo(SDFAttribute o) {
		return this.getURI(false).compareTo(o.getURI(false));
	}
	
}