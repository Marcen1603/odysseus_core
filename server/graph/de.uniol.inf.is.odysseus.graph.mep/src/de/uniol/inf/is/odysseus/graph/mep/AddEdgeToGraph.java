package de.uniol.inf.is.odysseus.graph.mep;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.graph.datatype.SDFGraphDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * MEP function to add an {@link Edge} to a {@link Graph}. The inputs are an
 * {@link Edge} and a {@link Graph}. The output is the updated {@link Graph}. If
 * there already existed an edge with the id of the new one, the existing edge
 * is updated. That means, all existing attributes are removed and the new ones
 * are added.
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class AddEdgeToGraph extends AbstractFunction<Graph> {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -3350132278808363373L;

	/**
	 * The logger for this class.
	 */
	private static final Logger log = LoggerFactory.getLogger("GraphMEP");

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = "AddEdgeToGraph";

	/**
	 * The amount of input values.
	 */
	private static final int numInputs = 2;

	/**
	 * The expected data types of the inputs. One row for each input. Different
	 * data types in a row mark different possible data types for the input.
	 */
	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] { { SDFGraphDatatype.EDGE },
			{ SDFGraphDatatype.GRAPH } };

	/**
	 * The data type of the output.
	 */
	private static final SDFDatatype outputType = SDFGraphDatatype.GRAPH;

	/**
	 * Creates a new MEP function.
	 */
	public AddEdgeToGraph() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public Graph getValue() {
		try {
			Edge edge = getInputValue(0);
			Graph graph = getInputValue(1);
			Map<String, Object> attributes = edge.getAttributeKeySet().stream()
					.collect(Collectors.toMap(key -> key, key -> edge.getAttribute(key)));
			List<Edge> existingEdges = graph.getEdgeSet().stream().filter(e -> e.getId().equals(edge.getId()))
					.collect(Collectors.toList());
			Edge addedEdge = existingEdges.isEmpty()
					? graph.addEdge(edge.getId(), (Node) edge.getNode0(), (Node) edge.getNode1())
					: graph.getEdge(edge.getId());
			addedEdge.addAttributes(attributes);
			return graph;
		} catch (Throwable e) {
			log.error("Could not calculate shortest path length in graph!", e);
			return null;
		}
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}
