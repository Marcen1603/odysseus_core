package de.uniol.inf.is.odysseus.graph.datatype;

import java.util.Collections;
import java.util.List;

import org.graphstream.graph.Graph;

import de.uniol.inf.is.odysseus.core.datatype.IDatatypeProvider;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * Datatype for {@link Graph} objects.
 *
 * @author Michael Brand (michael.brand@uol.de)
 */
public class SDFGraphDatatype implements IDatatypeProvider {

	public static final SDFDatatype GRAPH = new SDFDatatype("Graph");

	@Override
	public List<SDFDatatype> getDatatypes() {
		return Collections.singletonList(GRAPH);
	}

}