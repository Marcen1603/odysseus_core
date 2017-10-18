package de.uniol.inf.is.odysseus.graph.datatype;

import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datatype.IDatatypeProvider;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class SDFGraphDatatype implements IDatatypeProvider {

	public static final SDFDatatype GRAPH = new SDFDatatype("Graph");

	@Override
	public List<SDFDatatype> getDatatypes() {
		return Collections.singletonList(GRAPH);
	}

}