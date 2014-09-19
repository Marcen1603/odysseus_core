package de.uniol.inf.is.odysseus.complexnumber;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class SDFComplexNumberDatatype extends SDFDatatype {

    public static final SDFDatatype COMPLEX_NUMBER = new SDFDatatype("ComplexNumber");

	private static final long serialVersionUID = 4072709883642213655L;

	public SDFComplexNumberDatatype(final String URI){
		super(URI);
	}
	
}
