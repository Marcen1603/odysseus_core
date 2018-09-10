package de.uniol.inf.is.odysseus.complexnumber;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datatype.IDatatypeProvider;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class DatatypeProvider implements IDatatypeProvider{

	@Override
	public List<SDFDatatype> getDatatypes() {
        List<SDFDatatype> ret = new ArrayList<>();
        ret.add(SDFComplexNumberDatatype.COMPLEX_NUMBER);
        ret.add(SDFComplexNumberDatatype.LIST_COMPLEX_NUMBER);
        return ret;	
    }

}
