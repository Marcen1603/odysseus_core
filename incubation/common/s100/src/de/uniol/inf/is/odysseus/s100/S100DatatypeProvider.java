package de.uniol.inf.is.odysseus.s100;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datatype.IDatatypeProvider;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.s100.DataType.SDFS100DataType;


public class S100DatatypeProvider implements IDatatypeProvider {
	

	@Override
	public List<SDFDatatype> getDatatypes() {
		List<SDFDatatype> ret = new ArrayList<>();
		ret.add(SDFS100DataType.S100);
		return ret;
	}
}
