package de.uniol.inf.ei.odysseus.nds.mep;

import java.util.List;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.graph.datatype.SDFGraphDatatype;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.keyvalue.datatype.SDFKeyValueDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * MEP function to create a graph out of a very specific string notation of a
 * low voltage topology in the research project NetzDatenStrom. <br />
 * <br />
 * It is assumed that the input is either a string list or a
 * {@link KeyValueObject} list, formated exactly as follows: <br/>
 * <br />
 * [{"source": "a", "target": "b"}, {"source": "c", "target": "d"}, ...] where
 * "source" and "target" are fix.<br />
 * <br />
 * The output is a {@link Graph} object (SDFDatatype =
 * {@link SDFGraphDatatype}).
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class NDSGraphFromJSONString extends AbstractFunction<Graph> {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -2282995951858742772L;

	/**
	 * The logger for this class.
	 */
	private static final Logger log = LoggerFactory.getLogger("NetzDatenStromWrapper");

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = "NDSGraphFromJSONString";

	/**
	 * The amount of input values.
	 */
	private static final int numInputs = 2;

	/**
	 * The expected data types of the inputs. One row for each input. Different
	 * data types in a row mark different possible data types for the input.
	 */
	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] {
			{ SDFDatatype.LIST_STRING, SDFKeyValueDatatype.LIST_KEYVALUEOBJECT }, { SDFDatatype.STRING } };

	/**
	 * The data type of the outputs.
	 */
	private static final SDFDatatype outputType = SDFGraphDatatype.GRAPH;

	/**
	 * The fix keys of the nodes that define an edge.
	 */
	private static final String[] edgeKeys = new String[] { "source", "target" };

	/**
	 * Creates a new MEP function.
	 */
	public NDSGraphFromJSONString() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public Graph getValue() {
		try {
			@SuppressWarnings("unchecked")
			List<String> strEdges = (List<String>) getInputValue(0);
			String graphId = getInputValue(1);

			Graph graph = new SingleGraph(graphId);
			graph.setStrict(false); // true will rise failures when adding
									// nodes/edges, if nodes/edges already exist

			JSONArray edges = new JSONArray(strEdges);
			for (int i = 0; i < edges.length(); i++) {
				// Attention: The elements in the array are Odysseus
				// KeyValueObjects!
				KeyValueObject<?> edge = (KeyValueObject<?>) edges.get(i);
				String strNode0 = edge.getAttribute(edgeKeys[0]);
				String strNode1 = edge.getAttribute(edgeKeys[1]);

				// if node/edge exists, the existing node/edge is returned.
				// nothing is changed in the graph
				Node node0 = graph.addNode(strNode0);
				Node node1 = graph.addNode(strNode1);
				graph.addEdge(strNode0 + "-" + strNode1, node0, node1);
			}
			return graph;
		} catch (Throwable e) {
			log.error("Could not create graph!", e);
			return null;
		}
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}
