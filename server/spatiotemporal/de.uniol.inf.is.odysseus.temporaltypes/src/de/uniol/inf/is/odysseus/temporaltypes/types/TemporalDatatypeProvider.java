package de.uniol.inf.is.odysseus.temporaltypes.types;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datatype.IDatatypeProvider;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class TemporalDatatypeProvider implements IDatatypeProvider {

	@Override
	public List<SDFDatatype> getDatatypes() {
		List<SDFDatatype> datatypes = new ArrayList<>();
		datatypes.add(TemporalDatatype.TEMPORAL_INTEGER);
		return datatypes;
	}

}
