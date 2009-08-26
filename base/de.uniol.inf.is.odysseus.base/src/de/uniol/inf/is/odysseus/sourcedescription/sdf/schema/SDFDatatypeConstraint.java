package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

public class SDFDatatypeConstraint extends SDFSchemaElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2738703926500426135L;
	private String cType = null;
	
	public SDFDatatypeConstraint(String URI, String cType) {
		super(URI);
		this.cType = cType;
	}

	public String getCType() {
		return cType;
	}
	
	
}