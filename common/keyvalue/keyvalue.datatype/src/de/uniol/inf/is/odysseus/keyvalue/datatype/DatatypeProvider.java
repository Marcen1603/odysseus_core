package de.uniol.inf.is.odysseus.keyvalue.datatype;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datatype.IDatatypeProvider;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class DatatypeProvider implements IDatatypeProvider {

	@Override
	public List<SDFDatatype> getDatatypes() {
		List<SDFDatatype> ret = new ArrayList<>();
		ret.addAll(SDFKeyValueDatatype.getAll());
		return ret;
	}

}
