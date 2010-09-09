package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;

public class SDFDatatype extends SDFElement {

	private static final long serialVersionUID = 8585322290347489841L;
	public SDFDatatype(String URI) {
		super(URI);
	}
	
	@Override
	// TODO: später wieder entfernen!!
	public String getQualName() {
		if (super.getQualName() != null && super.getQualName().length() > 0){
			return super.getQualName();
		}else{
			return getURI();
		}
	}

}