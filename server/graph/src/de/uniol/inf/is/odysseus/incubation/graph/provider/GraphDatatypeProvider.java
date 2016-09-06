package de.uniol.inf.is.odysseus.incubation.graph.provider;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datatype.IDatatypeProvider;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.incubation.graph.sdf.schema.SDFGraphDatatype;

public class GraphDatatypeProvider implements IDatatypeProvider {

	@Override
	public List<SDFDatatype> getDatatypes() {
		List<SDFDatatype> ret = new ArrayList<SDFDatatype>();
		ret.add(SDFGraphDatatype.GRAPH);
		return ret;
	}

}
