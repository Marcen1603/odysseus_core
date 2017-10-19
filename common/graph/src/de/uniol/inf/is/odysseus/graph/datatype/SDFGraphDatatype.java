package de.uniol.inf.is.odysseus.graph.datatype;

import java.util.ArrayList;
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
	public static final SDFDatatype NODE = new SDFDatatype("GraphNode");
	public static final SDFDatatype NODE_LIST = new SDFDatatype("GraphNode_List");
	public static final SDFDatatype EDGE = new SDFDatatype("GraphEdge");
	public static final SDFDatatype EDGE_LIST = new SDFDatatype("GraphEdge_List");

	@Override
	public List<SDFDatatype> getDatatypes() {
		List<SDFDatatype> list = new ArrayList<>();
		list.add(GRAPH);
		list.add(NODE);
		list.add(NODE_LIST);
		list.add(EDGE);
		list.add(EDGE_LIST);
		return list;
	}

}