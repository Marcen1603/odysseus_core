package de.uniol.inf.is.odysseus.wrapper.mosaik.mep;

import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.graph.datatype.SDFGraphDatatype;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.keyvalue.datatype.SDFKeyValueDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * MEP function to create a graph out of a mosaik topology json. <br />
 * <br />
 * Example:<br />
 * {<br />
 * "base_mva": 10,<br />
 * "bus": [<br />
 * ["tr_pri", "REF", 20.0],<br />
 * ["node_a1", "PQ", 0.23],<br />
 * ...<br />
 * ],<br />
 * "trafo": [<br />
 * ["transformer", "tr_pri", "tr_sec", 0.25, 4.2, 0.00275, 6.9, 360.8]<br />
 * ],<br />
 * "branch": [<br />
 * ["branch_1", "tr_sec", "node_a1", 0.100, 0.2542, 0.080425, 0.0, 240.0],<br />
 * ["branch_2", "node_a1", "node_a2", 0.375, 0.2542, 0.080425, 0.0,
 * 240.0],<br />
 * ...<br />
 * ]<br />
 * }<br />
 * <br />
 * The first argument is the json that hold the information about the topology.
 * The second argument is the id for the graph object. The output is a
 * {@link Graph} object (SDFDatatype = {@link SDFGraphDatatype}).
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class MosaikTopologyFromJSON extends AbstractFunction<Graph> {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -6603853387076071216L;

	/**
	 * The logger for this class.
	 */
	private static final Logger log = LoggerFactory.getLogger("MosaikWrapper");

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = "MosaikTopologyFromJSON";

	/**
	 * The amount of input values.
	 */
	private static final int numInputs = 2;

	/**
	 * The expected data types of the inputs. One row for each input. Different
	 * data types in a row mark different possible data types for the input.
	 */
	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] {
			{ SDFKeyValueDatatype.KEYVALUEOBJECT }, { SDFDatatype.STRING } };

	/**
	 * The data type of the outputs.
	 */
	private static final SDFDatatype outputType = SDFGraphDatatype.GRAPH;

	/**
	 * Creates a new MEP function.
	 */
	public MosaikTopologyFromJSON() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public Graph getValue() {
		try {
			KeyValueObject<?> input = getInputValue(0);
			String graphId = getInputValue(1);

			Graph graph = new SingleGraph(graphId);
			graph.addAttribute("base_mva", input.getNumberAttribute("base_mva"));

			input.path("bus").stream().forEach(busObj -> {
				List<Object> bus = ((KeyValueObject<?>) busObj).path("$");
				Node node = graph.addNode((String) bus.get(0));
				node.addAttribute("NodeType", (String) bus.get(1));
				node.addAttribute("BaseVoltage_kV", (Double) bus.get(2));
			});

			List<Object> trafo = ((KeyValueObject<?>)((List<Object>) input.path("trafo")).get(0)).path("$");
			Node fromTrafo = graph.getNode((String) trafo.get(1));
			Node toTrafo = graph.getNode((String) trafo.get(2));
			Edge trafoEdge = graph.addEdge((String) trafo.get(0), fromTrafo, toTrafo);
			trafoEdge.addAttribute("Sr_MVA", (Double) trafo.get(3));
			trafoEdge.addAttribute("v1_%", (Double) trafo.get(4));
			trafoEdge.addAttribute("P1_MW", (Double) trafo.get(5));
			trafoEdge.addAttribute("Imax_p_A", (Double) trafo.get(6));
			trafoEdge.addAttribute("Imax_s_A", (Double) trafo.get(7));

			input.path("branch").stream().forEach(branchObj -> {
				List<Object> branch = ((KeyValueObject<?>) branchObj).path("$");
				Node fromBus = graph.getNode((String) branch.get(1));
				Node toBus = graph.getNode((String) branch.get(2));
				Edge edge = graph.addEdge((String) branch.get(0), fromBus, toBus);
				edge.addAttribute("length_km", (Double) branch.get(3));
				edge.addAttribute("R'_ohm/km", (Double) branch.get(4));
				edge.addAttribute("X'_owm/km", (Double) branch.get(5));
				edge.addAttribute("C'_nF/km", (Double) branch.get(6));
				edge.addAttribute("Imax_A", (Double) branch.get(7));
			});
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