package de.uniol.inf.is.odysseus.server.opcua.access;

import java.util.Arrays;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datatype.IDatatypeProvider;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.server.opcua.core.SDFOPCUADatatype;

public class OPCUADatatypeProvider implements IDatatypeProvider {

	@Override
	public List<SDFDatatype> getDatatypes() {
		return Arrays.asList(SDFOPCUADatatype.types);
	}
}